(ns clik.service
  (:require
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    (compojure
      [core :refer [ANY GET POST routes]]
      [route :refer [not-found]])
    [com.stuartsierra.component :as component]
    [ring.adapter.jetty :as jetty]
    (ring.middleware
      [content-type :refer [wrap-content-type]]
      [keyword-params :refer [wrap-keyword-params]]
      [not-modified :refer [wrap-not-modified]]
      [params :refer [wrap-params]]
      [resource :refer [wrap-resource]])
    [ring.util.response :as ring])
  (:import
    org.eclipse.jetty.server.Server))


;; APPLICATION CONSTRUCTORS

    (defn create-todo
      "Creates a new post with passed in title task and team"
      [team title body]
      {:team team, :title title, :body body, :lane "todo"})


    (defn app-routes
      "Constructs a new Ring handler implementing the website application."
      [data-source]
      (routes
        (GET "/" []
          {:body (pr-str "Here are the items in the todo list:"
            (map #(str "\n" %) @data-source))})
    ;GET all by team name /team/:team-name
    ;GET all by lane  /team/:lane

        ; (POST "/test" []
        ;     (let [result (swap! data-source ["is new!"])]
        ;     {:body (str result)}))
        (POST "/new" [team title body]
            (let [result (swap! data-source conj (create-todo title team body))])
            {:body (str "Created New Task:" (create-todo title team body))})))

    (defn- wrap-middleware
      "Wraps the application routes in middleware."
      [handler]
      (-> handler
        wrap-keyword-params
        wrap-params
        ; (wrap-resource "/public")
        ; wrap-content-type
        ; (wrap-cache-control #{"text/css" "text/javascript"} :max-age 300)
        ; wrap-not-modified
        ; wrap-exception-handler
        ; wrap-request-logger
        ; wrap-x-forwarded-for
        ))


  (defn- app-handler
    "Constructs a fully-wrapped application handler to serve with Jetty."
    [data-source]
    (wrap-middleware (app-routes data-source)))


;; Webserver Lifecycle

(defrecord WebServer
  [options data-source ^Server server]

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
      (let [handler (app-handler data-source)
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
  (WebServer. options nil nil))
