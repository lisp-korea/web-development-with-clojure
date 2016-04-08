(ns picture-gallery.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [picture-gallery.layout :refer [error-page]]
            [picture-gallery.routes.home :refer [home-routes]]
            [picture-gallery.routes.services
             :refer [service-routes restricted-service-routes]]
            [compojure.route :as route]
            [picture-gallery.middleware :as middleware]))

(def app-routes
  (routes
    #'service-routes
    (wrap-routes #'restricted-service-routes middleware/wrap-auth)
    (wrap-routes #'home-routes middleware/wrap-csrf)
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(def app (middleware/wrap-base #'app-routes))
