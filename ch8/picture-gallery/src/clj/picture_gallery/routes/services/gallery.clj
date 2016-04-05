(ns picture-gallery.routes.services.gallery
  (:require [picture-gallery.layout :refer [error-page]]
            [picture-gallery.db.core :as db]
            [ring.util.http-response :refer :all])
  (:import java.io.ByteArrayInputStream))

(defn get-image [owner name]
  (if-let [{:keys [type data]} (db/get-image {:owner owner :name name})]
    (-> (ByteArrayInputStream. data)
        (ok)
        (content-type type))
    (error-page {:status 404
                 :title "page not found"})))

(defn list-thumbnails [owner]
  (ok (db/list-thumbnails {:owner owner})))