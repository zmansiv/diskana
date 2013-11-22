(ns diskana.app
  (:use
    diskana.filesystem)
  (:import
    (javafx.beans.value ChangeListener ObservableValue)
    (javafx.concurrent Worker$State)
    (javafx.event ActionEvent EventHandler)
    (javafx.scene Scene)
    (javafx.scene.control Button)
    (javafx.scene.layout StackPane)
    (javafx.stage Stage)
    (javafx.scene.web WebView)))

(gen-class
  :name com.diskana.Diskana
  :extends javafx.application.Application)

(defn -start [app stage]
  (let [root (StackPane.)
        btn (Button.)
        web-view (WebView.)
        state-prop (.stateProperty (.getLoadWorker (.getEngine web-view)))
        url "http://clojure.org"]

    (.add (.getChildren root) web-view)

    (.addListener state-prop
      (proxy [ChangeListener] []
        (changed [observable-value old-state new-state]
          (println str "Current state:" (.name new-state))
          (if (= new-state Worker$State/SUCCEEDED)
            (println (str "URL \"" url "\" load completed!"))))))

    (.load (.getEngine web-view) url)

    (.setTitle stage "JavaFX app with Clojure")
    (.setText btn "Just a button")
    (.setOnAction btn
      (proxy [EventHandler] []
        (handle [event]
          (println "The button was clicked"))))
    (.add (.getChildren root) btn)

    (.setScene stage (Scene. root 800 600))
    (.show stage)))

(defn -main [& args]
  ;(javafx.application.Application/launch com.diskana.Diskana (into-array String []))
  (print-tree (build-tree (System/getProperty "user.dir"))))