(ns picture-gallery.routes.services
  (:require [picture-gallery.routes.services.auth :as auth]
            [picture-gallery.routes.services.upload :as upload]
            [picture-gallery.routes.services.gallery :as gallery]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [compojure.api.upload :refer [wrap-multipart-params TempFileUpload]]
            [schema.core :as s]))

(s/defschema UserRegistration
  {:id                     String
   :pass                   String
   :pass-confirm           String})

(s/defschema Result
  {:result                   s/Keyword
   (s/optional-key :message) String})

(s/defschema Gallery
  {:owner               String
   :name                String
   (s/optional-key :rk) s/Num})

(defapi service-routes
  {:swagger {:ui   "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version     "1.0.0"
                           :title       "Picture Gallery API"
                           :description "Public Services"}}}}
  (POST "/register" req
        :return Result
        :body [user UserRegistration]
        :summary "register a new user"
        (auth/register! req user))
  (POST "/login" req
        :header-params [authorization :- String]
        :summary "login the user and create a session"
        :return Result
        (auth/login! req authorization))
  (POST "/logout" []
        :summary "remove user session"
        :return Result
        (auth/logout!))
  (GET "/gallery/:owner/:name" []
       :summary "display user image"
       :path-params [owner :- String name :- String]
       (gallery/get-image owner name))
  (GET "/list-thumbnails/:owner" []
       :path-params [owner :- String]
       :summary "list thumbnails for images in the gallery"
       :return [Gallery]
       (gallery/list-thumbnails owner))
  (GET "/list-galleries" []
       :summary "lists a thumbnail for each user"
       :return [Gallery]
       (gallery/list-galleries)))

(defapi restricted-service-routes
  {:swagger {:ui "/swagger-ui-private"
             :spec "/swagger-private.json"
             :data {:info {:version "1.0.0"
                           :title "Picture Gallery API"
                           :description "Private Services"}}}}
  (POST "/upload" req
        :multipart-params [file :- TempFileUpload]
        :middleware [wrap-multipart-params]
        :summary "handles image upload"
        :return Result
        (upload/save-image! (:identity req) file))
  (DELETE "/image/:thumbnail" {:keys [identity]}
          :path-params [thumbnail :- String]
          :summary "delete the specified file from the database"
          :return Result
          (gallery/delete-image!
            identity thumbnail (clojure.string/replace thumbnail #"thumb_" "")))
  (DELETE "/account" {:keys [identity]}
          (auth/delete-account! identity)))
