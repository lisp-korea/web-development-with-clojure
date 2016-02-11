(ns guestbook.test.db.core
  (:require [guestbook.db.core :as db]
            [guestbook.db.migrations :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [environ.core :refer [env]]))

(use-fixtures
  :once
  (fn [f]
    (migrations/migrate ["migrate"])
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn db/conn]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1 (db/save-message!
               {:name      "Bob"
                :message   "Hello World"
                :timestamp #inst "2015-01-18T16:22:10.010000000-00:00"} {:connection t-conn})))
    (is (= [{:name      "Bob"
             :message   "Hello World"
             :timestamp #inst "2015-01-18T16:22:10.010000000-00:00"}]
           (map
             #(select-keys % [:name :message :timestamp])
             (db/get-messages nil {:connection t-conn}))))))
