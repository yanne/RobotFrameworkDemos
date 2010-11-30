(ns TodoApplication
    (:gen-class))
(use 'task-store)
(import '(javax.swing JFrame JLabel JTextField JButton JList JPanel BoxLayout)
        '(java.awt.event ActionListener)
        '(java.awt BorderLayout))

(defmacro with-action [component event & body]
  `(. ~component addActionListener
     (proxy [java.awt.event.ActionListener] []
       (actionPerformed [~event] ~@body))))

(defn refresh-tasklist [tasklist]
  (.setListData tasklist (to-array (map #(:name %) (all-tasks)))))

(defn submit-button [tasklist input-field]
  (doto (JButton. "add task")
    (.setName "add-task")
    (with-action evt
      (add-task (.getText input-field))
      (refresh-tasklist tasklist))))

(defn textfield [name size]
  (doto (JTextField. size)
    (.setName name)))

(defn input-panel [tasklist]
  (let [panel (JPanel.)
        task-input (textfield "task-name" 20)]
    (doto panel
      (.setLayout (BoxLayout. panel BoxLayout/X_AXIS))
      (.add (JLabel. "Task: "))
      (.add task-input)
      (.add (submit-button tasklist task-input)))
  panel))

(defn finished-button [tasklist]
  (doto (JButton. "Task Finished")
    (.setName "finsih-task")
    (with-action evt
      (delete-task (.getSelectedValue tasklist))
      (refresh-tasklist tasklist))))

(defn todo-list-app []
  (let [frame (JFrame. "TODO List")
        tasklist (JList.)]
    (doto tasklist
      (.setName "task-list"))
    (doto frame
      (.setLayout (BorderLayout.))
      (.add (input-panel tasklist) BorderLayout/NORTH)
      (.add tasklist BorderLayout/CENTER)
      (.add (finished-button tasklist) BorderLayout/SOUTH)
      (.setSize 400 300)
      (.setVisible true))
    (refresh-tasklist tasklist)
    frame))

(defn -main [& args]
  (init-tasks)
  (.setDefaultCloseOperation (todo-list-app) JFrame/EXIT_ON_CLOSE))
