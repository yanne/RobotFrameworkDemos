(ns dbapp
    (:gen-class))

(import '(javax.swing JFrame JLabel JTextField JButton JList)
        '(java.awt.event ActionListener)
        '(java.awt GridLayout))

(defn store [username userlist]
  (.setListData userlist (to-array [username])))

(defn store-action [username-input userlist]
  (proxy [ActionListener] []
    (actionPerformed [evt]
      (let [username (.getText username-input)]
        (store username userlist)))))

(defn sut []
  (let [frame (JFrame. "Database Application")
        username-input (JTextField.)
        username-label (JLabel. "Username")
        submit-button (JButton. "Add User")
        userlist-label (JLabel. "Users")
        userlist (JList.)]
    (.addActionListener submit-button (store-action username-input userlist))
    (doto frame
      (.setLayout (GridLayout. 2 2 3 3))
      (.add username-label)
      (.add username-input)
      (.add submit-button)
      (.add userlist)
      (.setSize 300 300)
      (.setVisible true))
    frame))

(defn -main [& args]
    (.setDefaultCloseOperation (sut) JFrame/EXIT_ON_CLOSE))
