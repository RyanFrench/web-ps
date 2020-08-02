(ns web-ps.core
  (:require
    ;; Core
    [clojure.java.io :as io]
    ;; 3rd-party
    [compojure.core :refer [defroutes GET POST wrap-routes]]
    [compojure.route :as route]
    [compojure.middleware :refer [wrap-canonical-redirect]]
    [ring.middleware.params :refer [wrap-params]]
    [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [org.httpkit.server :as httpkit]
    [taoensso.timbre :as log]
    [clojure.java.io :as io]
    ;; Routes
    [web-ps.routes.ps :as ps-route]
    )
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Routes
;;
(defroutes routes
  (GET "/ps" [] ps-route/handler))

(defn handler
  [routes]
  (->
    routes
    (wrap-routes wrap-json-params)
    (wrap-routes wrap-json-response)
    (wrap-routes wrap-params)
    (wrap-routes wrap-keyword-params)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Webserver
;;
(defonce server (atom nil))

(defn start-server
  []
  (reset!
    server
    (httpkit/run-server (wrap-canonical-redirect (handler #'routes)) {:port 8080})))

(defn stop-server
  "Gracefully stop the web server"
  []
  (when-not (nil? (@server :timeout 100))
    (reset! server nil)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialisation
;;

(defn shutdown
  []
  (log/info "Service shutting down")
  (stop-server)
  (log/info "Service shut down"))

(defn initialise
  []
  (log/info "Starting web server")
  (.addShutdownHook (Runtime/getRuntime) (Thread. (fn [] shutdown)))
  (try
    (start-server)
    (log/info "Web server started")
  (catch Exception e
    (log/error (or (ex-data e) e) "Unable to start server")
    (shutdown))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Entrypoint
;;
(defn -main []
  (initialise))
