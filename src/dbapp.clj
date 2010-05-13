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

(defn del-action [task-input tasklist]
  (proxy [ActionListener] []
    (actionPerformed [evt]
      (let [task (.getSelectedValue tasklist)]
        (delete-task task)
        (refresh-tasklist tasklist)))))

(defn sut []
  (let [frame (JFrame. "TODO List")
        name-panel (JPanel.)
        task-input (JTextField. 20)
        task-label (JLabel. "Task: ")
        submit-button (JButton. "Add Task")
        delete-button (JButton. "Task Finished")
        tasklist-label (JLabel. "Tasks")
        tasklist (JList.)]
    (.addActionListener submit-button (store-action task-input tasklist))
    (.addActionListener delete-button (del-action task-input tasklist))
    (doto name-panel
      (.setLayout (BoxLayout. name-panel BoxLayout/X_AXIS))
      (.add task-label)
      (.add task-input)
      (.add submit-button))
    (doto frame
      (.setLayout (BorderLayout.))
      (.add name-panel BorderLayout/NORTH)
      (.add tasklist BorderLayout/CENTER)
      (.add delete-button BorderLayout/SOUTH)
      (.setSize 400 300)
      (.setVisible true))
    (refresh-tasklist tasklist)
    frame))

(defn -main [& args]
  (init-tasks)
  (.setDefaultCloseOperation (sut) JFrame/EXIT_ON_CLOSE))
