(ns web-ps.routes.ps
  (:require
    [web-ps.proc :as proc]
    [clojure.java.io :as io]
    [taoensso.timbre :as log]
    [cheshire.core :as json]))

(defn get-pids
  "Get a sequence of PIDs for all processes currently running"
  []
  (->>
    (io/file "/proc")
    .list
    (filter #(re-matches #"[0-9]+" %))))

(defn dissoc-by
  "This will remove keys from a map [m] given a predicate function [f]"
  [f m]
  (->>
    m
    (filter #(f (first %)))
    (into {})))

(defn ps
  "Get a list of PIDs and their associated details via /proc"
  []
  (let [self (proc/get-info "self")]
  (->>
    (get-pids)
    (map #(proc/get-info %))
    (dissoc-by (fn [[k v]] (not= (keyword (:ppid v)) (keyword (:ppid (:self self)))))))))

(defn handler
  "Get the processes running on the server"
  [request]
  (try
    (let [procs (ps)]
      {:status 200 :body (json/encode procs)})
  (catch Exception e
    (log/error e)
    {:status 500 :body (or (ex-data e) (.getMessage e))})))
