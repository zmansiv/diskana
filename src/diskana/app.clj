(ns diskana.app
  (:require
    [diskana.filesystem :as fs]
    [diskana.ui :as ui]))

(gen-class
  :name com.diskana.Diskana
  :extends javafx.application.Application)

(def dir (str (System/getProperty "user.home") "/Downloads"))

(defn -start [app stage]
  (println "Building tree...")
  (let [tree (fs/build-file-tree dir)]
    (println "Done!")
    (ui/create stage tree)))

(defn -stop [app]
  (println "Shutting down")
  (shutdown-agents))

(defn -main [& args]
  (javafx.application.Application/launch com.diskana.Diskana nil))
  ;(print-file-tree (build-file-tree dir)))