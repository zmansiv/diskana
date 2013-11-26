(ns diskana.tree)

(defrecord Tree [value children])

(defn do-tree [f tree]
  ((fn -do-tree[f tree parent depth]
    (f (:value tree) parent depth)
    (doseq [sub-tree (:children tree)]
      (-do-tree f sub-tree parent (inc depth))))
  
    f tree nil 0))