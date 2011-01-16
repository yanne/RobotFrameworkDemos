(ns TodoApplication
    (:gen-class))
(require 'task-store)
(require 'ui)

(defn -main [& args]
  (task-store/init)
  (ui/create))
