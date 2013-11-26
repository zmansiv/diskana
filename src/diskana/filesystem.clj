(ns diskana.filesystem
  (:require
    [diskana.tree :refer :all]
    [clojure.math.numeric-tower :as math])
  (:import
    (diskana.tree
      Tree)))

(defrecord File [name size dir?])

(defn- parallel-map [f coll]
  (if (> (count coll) 3)
    (pmap f coll)
    (map f coll)))

(defn build-file-tree [file]
  (let [file (if (string? file) (java.io.File. file) file)
        name (.getName file)
        dir? (.isDirectory file)
        children (.listFiles file)
        sub-trees (parallel-map build-file-tree children)]
    (Tree.
      (File.
        name
        (if dir?
          (reduce + (parallel-map #(:size (:value %)) sub-trees))
          (.length file))
        dir?)
      sub-trees)))

(def ^:private units {"TB" (math/expt 2 40)
            "GB" (math/expt 2 30)
            "MB" (math/expt 2 20)
            "KB" (math/expt 2 10)
            "B" 0})

(defn format-size [bytes]
  (let [unit (some #(if (>= bytes (val %)) %) units)
        unit-name (key unit)
        unit-size (val unit)]
    (str (format "%.1f" (float (if (pos? unit-size) (/ bytes unit-size) bytes))) unit-name)))

(defn print-file-tree [tree]
  (do-tree
    (fn [file parent depth]
      (let [prefix (apply str (repeat depth "-"))
            name (:name file)
            size (:size file)]
        (println (str prefix name " (" (format-size size) ")"))))
    tree))