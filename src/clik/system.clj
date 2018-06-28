(ns clik.system
  (:require [com.stuartsierra.component :as component]
            [clik.service :as service]))

(defn initialize
  []
  (component/system-map
   :data-source (atom {:tasks {}
                       :help {:teams { :pipes "something to do with a Mushroom Kingdom"
                                       :stugeon "kinda fishy"
                                       :stitch "my favorite movie"}
                              :lanes { :todo "upcoming work"
                                       :doing "work im doing"
                                       :done "work I have done"}}})
   :server
   (component/using
    (service/server {:port 8080})
    [:data-source])))
