(ns clik.service
  (:require
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [clojure.walk :as walk]
   [cheshire.core :as json]
   (compojure
    [core :refer [ANY GET POST PATCH DELETE routes]]
    [route :refer [not-found]])
   [com.stuartsierra.component :as component]
   [ring.adapter.jetty :as jetty]
   (ring.middleware
    [json :refer [wrap-json-response wrap-json-body]]
    [content-type :refer [wrap-content-type]]
    [keyword-params :refer [wrap-keyword-params]]
    [not-modified :refer [wrap-not-modified]]
    [params :refer [wrap-params]]
    [resource :refer [wrap-resource]])
   [ring.util.response :as ring])
  (:import
   org.eclipse.jetty.server.Server
   java.util.UUID))

;; APPLICATION CONSTRUCTORS

(defn create-todo
  "Creates a new post with passed in title task and team"
  [body]
  (merge {:lane "todo"}
         (select-keys (walk/keywordize-keys body) [:title :note :lane :team])))
(defn app-routes
  "Constructs a new Ring handler implementing the website application."
  [data-source]
  (routes
        ;; get all tasks and help texts
   (GET "/" []
        (ring/response @data-source))

   (GET "/help" []
        (ring/response (get @data-source :help)))

   (GET "/tasks" [team]
        (let [tasks (cond->> (:tasks @data-source)
                      team (filter (fn [[id task]] (= team (:team task))))
                      true (map (fn [[id task]] (assoc task :id id))))]
          (ring/response tasks)))

        ;; get one task by ID
   (GET "/tasks/:id" [id]
        (if-let [task (get-in @data-source [:tasks id])]
          (ring/response (assoc task :id id))
          (ring/not-found {:message "task not found"})))

        ;; creates new task item
   (POST "/tasks" request
         (let [body (:body request)
               new-todo (create-todo body)
               new-id (str (UUID/randomUUID))
               result (swap! data-source assoc-in [:tasks new-id] new-todo)]
           (ring/response (assoc new-todo :id new-id))))

        ;; modifies a task item
   (PATCH "/tasks/:id" [id :as request]
          (let [body (:body request)
                old-task (get-in @data-source [:tasks id])
                updates (select-keys (walk/keywordize-keys body) [:title :note :lane :team])
                new-task (merge old-task updates)]
            (swap! data-source assoc-in [:tasks id] new-task)
            (ring/response (assoc new-task :id id))))

        ;; deletes task item
   (DELETE "/tasks/:id" [id]
           (swap! data-source update :tasks dissoc id)
           (ring/response "deleted"))))

(defn- wrap-middleware
  "Wraps the application routes in middleware."
  [handler]
  (-> handler
      wrap-keyword-params
      wrap-params
      wrap-json-body
      wrap-json-response
        ; wrap-request-logger
      ))(defn- app-handler
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
