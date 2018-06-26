(ns clik.service
  (:require
    [clojure.string :as str]
    (compojure
      [core :refer [ANY GET POST routes]]
      [route :refer [not-found]])
    [com.stuartsierra.component :as component]
    [hiccup.core :as hiccup]
    [ring.adapter.jetty :as jetty]
    ; (ring.middleware
    ;   [content-type :refer [wrap-content-type]]
    ;   [keyword-params :refer [wrap-keyword-params]]
    ;   [not-modified :refer [wrap-not-modified]]
    ;   [params :refer [wrap-params]]
    ;   [resource :refer [wrap-resource]])
    [ring.util.response :as ring])
  (:import
    org.eclipse.jetty.server.Server))



  (defn- app-handler
    "Constructs a fully-wrapped application handler to serve with Jetty."
    [event-channel state-agent options]
    (let [handler (wrap-middleware (app-routes event-channel state-agent))]
      (if-let [wrapper (:ring/wrapper options)]
        (wrapper handler)
        handler)))

(defrecord WebServer
  [options event-channel state-agent ^Server server]

  component/Lifecycle

  (start
    [this]
    (if server
      (do
        (if-not (.isStarted server)
          (do
            (log/info "Restarting WebServer...")
            (.start server))
          (log/info "WebServer is already started"))
        this)
      (let [handler (app-handler event-channel state-agent options)
            options (assoc options :host "localhost" :join? false)]
        (log/info (str "Starting WebServer on port " (:port options) "..."))
        (assoc this :server (jetty/run-jetty handler options)))))


  (stop
    [this]
    (log/info "Stopping WebServer...")
    (when (and server (not (.isStopped server)))
      (.stop server))
    this))


(defn server
  "Constructs a new web server component with the given options."
  [options]
  (WebServer. options nil nil nil))
