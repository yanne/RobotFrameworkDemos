(ns task-store)
(use 'clojure.contrib.sql)

(def db {:classname "org.hsqldb.jdbcDriver"
         :subprotocol "hsqldb"
         :subname "mem:testdb"})

(defn db-action [action]
  (with-connection db (action)))

(defn create-tasks-table []
  (create-table :tasks
    [:id :int "IDENTITY" "PRIMARY KEY"]
    [:name :varchar "NOT NULL"]))

(defn init-tasks []
  (db-action create-tasks-table))

(defn sql-query [sql]
  (with-query-results res [sql] (doall res)))

(defn last-created-id []
  (first (vals (first (sql-query "CALL IDENTITY()")))))

(defn insert-task [name]
  (transaction
    (insert-values :tasks
      [:name]
      [name])
    (last-created-id)))

(defn add-task [name]
  (db-action #(insert-task name)))

(defn get-all-tasks []
  (sql-query "select * from tasks"))

(defn all-tasks []
  (db-action get-all-tasks))

(defn delete-task [name]
  (db-action #(delete-rows :tasks [(str "name='" name "'")])))
