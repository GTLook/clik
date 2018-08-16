(ns clik.system
  (:require [com.stuartsierra.component :as component]
            [clik.service :as service]))

(def default-data {:tasks {}
                   :help {:structure "title='title text' note='details text'"
                          :teams {:pipes "something to do with a Mushroom Kingdom"
                                  :stugeon "kinda fishy"
                                  :stitch "my favorite movie"}
                          :lanes {:todo "upcoming work"
                                  :doing "work im doing"
                                  :done "work I have done"}}})

(defn save-data!
  [_ _ _ data]
  (spit "data.edn" (prn-str data)))

(defn load-data
  []
  (try
    (read-string (slurp "data.edn"))
    (catch Exception ex
      default-data)))

(defn initialize
  []
  (component/system-map
   :data-source (add-watch (atom (load-data)) :saver save-data!)
   :server
   (component/using
    (service/server {:port 8080})
    [:data-source])))
