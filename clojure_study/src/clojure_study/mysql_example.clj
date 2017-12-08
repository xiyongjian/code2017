(ns clojure-study.mysql-example)

(require '[clojure.java.jdbc :as j])

(def db-spec { ;; :classname "com.mysql.jdbc.Driver"
              :subprotocol "mysql"
              :subname "//192.168.0.185:13306/test"
              :user "jxi"
              :password "password"
              :useSSL false
              :autoReconnect true
              })

(j/query db-spec
         ["SELECT * FROM test where id > ?" 0])

(defn query []
  (j/query db-spec
           ["SELECT * FROM test where id > ?" 0])
  )

(defn -main [& args] (println (query)))