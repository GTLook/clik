(ns clik.main
  (:gen-class) ; for -main method in uberjar
  (:require [clik.service :as service]))

;; This is an adapted service map, that can be started and stopped
;; From the REPL you can call server/start and server/stop on this service

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "\nCreating your server...")
  #_(server/start runnable-service))
