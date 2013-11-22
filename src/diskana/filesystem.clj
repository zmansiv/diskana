(ns diskana.filesystem
  (:require
    [clojure.math.numeric-tower :as math]))

(defrecord File [name size dir?])
(defrecord Tree [value children])

(defn build-tree [file]
  (let [file (if (string? file) (java.io.File. file) file)
        name (.getName file)
        dir? (.isDirectory file)
        children (.listFiles file)
        sub-trees (map build-tree children)]
    (Tree.
      (File.
        name
        (if dir? (reduce + (map #(:size (:value %)) sub-trees)) (.length file))
        dir?)
      sub-trees)))

(def base-unit (clojure.lang.MapEntry. "B" 1))
(def units {"KB" (math/expt 2 10)
            "MB" (math/expt 2 20)
            "GB" (math/expt 2 30)
            "TB" (math/expt 2 40)})

(defn format-size [bytes]
  (let [units (filter #(>= bytes (val %)) units)
        unit (if (seq units) (last units) base-unit)]
    (str (format "%.1f" (float (/ bytes (val unit)))) (key unit))))

(defn print-tree
  ([tree]
    (print-tree tree 0))
  ([tree nest-level]
    (let [prefix (apply str (repeat nest-level "-"))
          file (:value tree)
          name (:name file)
          size (:size file)
          children (:children tree)]
      (println (str prefix name " (" (format-size size) ")"))
      (doseq [child children]
        (print-tree child (inc nest-level))))))