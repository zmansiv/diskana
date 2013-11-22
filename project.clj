(defproject diskana "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/math.numeric-tower "0.0.2"]
                 [clojurefx "0.0.10"]]
  :resource-paths ["lib/jfxrt.jar"]
  :aot [diskana.app]
  :main diskana.app)