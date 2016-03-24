(ns picture-gallery.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[picture-gallery started successfully]=-"))
   :middleware identity})
