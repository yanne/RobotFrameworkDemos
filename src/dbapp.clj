(ns dbapp
    (:gen-class))
(use 'user-store)
(import '(javax.swing JFrame JLabel JTextField JButton JList)
        '(java.awt.event ActionListener)
        '(java.awt FlowLayout))

(defn refresh-userlist [userlist]
  (.setListData userlist (to-array (map #(:name %) (all-users)))))

(defn store-action [username-input userlist]
  (proxy [ActionListener] []
    (actionPerformed [evt]
      (let [username (.getText username-input)]
        (add-user username)
        (refresh-userlist userlist)))))

(defn del-action [username-input userlist]
  (proxy [ActionListener] []
    (actionPerformed [evt]
      (let [username (.getSelectedValue userlist)]
        (delete-user username)
        (refresh-userlist userlist)))))

(defn sut []
  (let [frame (JFrame. "Database Application")
        username-input (JTextField. 20)
        username-label (JLabel. "Username")
        submit-button (JButton. "Add User")
        delete-button (JButton. "Delete User")
        userlist-label (JLabel. "Users")
        userlist (JList.)]
    (.addActionListener submit-button (store-action username-input userlist))
    (.addActionListener delete-button (del-action username-input userlist))
    (doto frame
      (.setLayout (FlowLayout.))
      (.add username-label)
      (.add username-input)
      (.add submit-button)
      (.add delete-button)
      (.add userlist)
      (.setSize 400 300)
      (.setVisible true))
    (refresh-userlist userlist)
    frame))

(defn -main [& args]
    (.setDefaultCloseOperation (sut) JFrame/EXIT_ON_CLOSE))
