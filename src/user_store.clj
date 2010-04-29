(ns user-store)
(use 'clojure.contrib.sql)

(def db {:classname "org.hsqldb.jdbcDriver"
         :subprotocol "hsqldb"
         :subname "mem:testdb"})

(defn db-action [action]
  (with-connection db (action)))

(defn create-users-table []
  (create-table :users
    [:id :int "IDENTITY" "PRIMARY KEY"]
    [:name :varchar "NOT NULL"]))

(defn init-users []
  (db-action create-users-table))

(defn sql-query [sql]
  (with-query-results res [sql] (doall res)))

(defn last-created-id []
  (first (vals (first (sql-query "CALL IDENTITY()")))))

(defn insert-user [name]
  (transaction
    (insert-values :users
      [:name]
      [name])
    (last-created-id)))

(defn add-user [name]
  (db-action #(insert-user name)))

(defn get-all-users []
  (sql-query "select * from users"))

(defn all-users []
  (db-action get-all-users))

(defn delete-user [name]
  (db-action #(delete-rows :users [(str "name='" name "'")])))
