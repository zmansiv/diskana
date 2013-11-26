(ns diskana.ui
  (:import
    (javafx.scene
      Scene)
    (javafx.scene.control
      TreeView
      TreeViewBuilder
      TreeItem
      TreeItemBuilder)))

(defn create-tree-view [tree]
  (let [root
        ((fn create-tree-item[tree]
           (let [file (:value tree)
                 name (:name file)]
             (.. TreeItemBuilder create
               (value name)
               (children (map create-tree-item (:children tree)))
               build)))

          tree)]
    (.. TreeViewBuilder create (root root) build)))

(defn create [stage tree]
  (.setTitle stage "Diskana")
  (.setScene stage (Scene. (create-tree-view tree) 800 600))
  (.show stage))