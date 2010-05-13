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

(defn del-action [tasklist]
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

(defn sut []
  (let [frame (JFrame. "TODO List")
        delete-button (JButton. "Task Finished")
        tasklist (JList.)]
    (.addActionListener delete-button (del-action tasklist))
    (doto frame
      (.setLayout (BorderLayout.))
      (.add (input-panel tasklist) BorderLayout/NORTH)
      (.add tasklist BorderLayout/CENTER)
      (.add delete-button BorderLayout/SOUTH)
      (.setSize 400 300)
      (.setVisible true))
    (refresh-tasklist tasklist)
    frame))

(defn -main [& args]
  (init-tasks)
  (.setDefaultCloseOperation (sut) JFrame/EXIT_ON_CLOSE))
