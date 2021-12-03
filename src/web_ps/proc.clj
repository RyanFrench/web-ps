(ns web-ps.proc
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [taoensso.timbre :as log]))

(defrecord proc [name ppid cmdline environment])

(defn read-proc-file
  "Read a file for a given PID from /proc/<pid>/<file>"
  [pid file]
  (->
    (str "/proc/" pid "/" file)
    java.io.FileReader.
    slurp))

(defn process-exists?
  "Returns true if the process <pid> exists, otherwise false"
  [pid]
  (.exists (io/file (str "/proc/" pid))))

(defn get-name
  "Get the process name from /proc/<pid>/comm"
  [pid]
  (try
    (->
      (read-proc-file pid "comm")
      (string/split #"\/")
      last
      (string/split #" ")
      first
      string/trim)
    (catch Exception e
      (log/info e "Unable to retrieve name for PID" pid)
      pid)))

(defn get-ppid
  "Get the parent process ID from the 4th column in /proc/<pid>/stat"
  [pid]
  (try
    (->
      (read-proc-file pid "stat")
      (string/split #" ")
      (nth 4)
      string/trim)
    (catch Exception e
      (log/info e "Unable to retrieve PPID for PID" pid)
      "")))

(defn get-cmdline
  "Get the command used to invoke the process in /proc/<pid>/cmdline"
  [pid]
  (try
    (let [cmdline (->
                    (read-proc-file pid "cmdline")
                    (string/replace #"\u0000" " ")
                    string/trim)]
      (if (empty? cmdline)
        false
        cmdline))
      (catch Exception e
        (log/info e "Unable to retrieve cmdline for PID" pid)
        "")))

(defn get-environment-variables
  "Get the environment variables for the process in /proc/<pid>/environ"
  [pid]
  (try
    (string/split (read-proc-file pid "environ") #"\u0000")
    (catch Exception e
      (log/info e "Unable to retrieve Environment variables for PID" pid)
      [])))

(defn get-info
  "Given a PID, retrieve the required details from /proc"
  [pid]
  (if (process-exists? pid)
    {(keyword pid) (map->proc {:name (get-name pid)
                               :ppid (get-ppid pid)
                               :cmdline (get-cmdline pid)
                               :environment (get-environment-variables pid)})}
    (throw (Exception. (str "No process '" pid "' found in /proc")))))
