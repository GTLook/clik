(ns clik.system
  (:require [com.stuartsierra.component :as component]))

(defn initialize
  []
  (component/system-map
   :data-source (atom 0)
   :server
   (component/using
    (service/new-pedestal)
    [:data-source])))
