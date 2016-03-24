(ns guestbook.routes.home
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]
            [guestbook.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [guestbook.db.core :as db]
            [ring.util.response :as response]))

(defn home-page [{:keys [flash]}]
  (layout/render
    "home.html"
    (merge {:messages (db/get-messages)}
           (select-keys flash [:name :message :errors]))))

(defn validate-message [params]
  (first
    (b/validate
      params
      :name v/required
      :message [v/required [v/min-count 10]])))

(defn save-message! [{:keys [params]}]
  (if-let [errors (validate-message params)]
    (-> (response/redirect "/")
        (assoc :flash (assoc params :errors errors)))
    (do
      (db/save-message!
        (assoc params :timestamp (java.util.Date.))) (response/redirect "/"))))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" request (home-page request))
  (POST "/message" request (save-message! request))
  (GET "/about" [] (about-page)))

