(ns clik.system
  (:require [com.stuartsierra.component :as component]
            [clik.service :as service]))

(defn initialize
  []
  (component/system-map
   :data-source (atom [])
   :server
   (component/using
    (service/server {:port 8080})
    [:data-source])))
