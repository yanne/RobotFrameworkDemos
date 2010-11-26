(ns dbapp
    (:gen-class))
(use 'task-store)
(import '(javax.swing JFrame JLabel JTextField JButton JList JPanel BoxLayout)
        '(java.awt.event ActionListener)
        '(java.awt BorderLayout))

(defn refresh-tasklist [tasklist]
  (.setListData tasklist (to-array (map #(:name %) (all-tasks)))))

(defn store-action [task-input tasklist]
  (proxy [ActionListener] []
    (actionPerformed [evt]
      (let [task (.getText task-input)]
        (add-task task)
        (refresh-tasklist tasklist)))))

(defn task-finished-action [tasklist]
  (proxy [ActionListener] []
    (actionPerformed [evt]
      (let [task (.getSelectedValue tasklist)]
        (delete-task task)
        (refresh-tasklist tasklist)))))

(defn input-panel [tasklist]
  (let [panel (JPanel.)
        task-label (JLabel. "Task: ")
        task-input (JTextField. 20)
        submit-button (JButton. "Add Task")]
    (.addActionListener submit-button (store-action task-input tasklist))
    (doto panel
      (.setLayout (BoxLayout. panel BoxLayout/X_AXIS))
      (.add task-label)
      (.add task-input)
      (.add submit-button))
  panel))

(defn finished-button [tasklist]
  (let [btn (JButton. "Task Finished")]
    (.addActionListener btn (task-finished-action tasklist))
    btn))

(defn todo-list-app []
  (let [frame (JFrame. "TODO List")
        tasklist (JList.)]
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
