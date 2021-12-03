(ns unit.web-ps.proc-test
    (:require [web-ps.proc :refer [get-info process-exists? get-name get-ppid get-cmdline get-environment-variables]]
              [clojure.test :refer [deftest is testing]]
              [spy.core :as spy]
              [spy.assert :as assert]
              [clojure.java.io :as io]))

(deftest ^:unit retrieve-process-info
  (testing "retrieving all information about a process"
    (let [pid "1002"
          name "name"
          ppid "1001"
          cmdline "/bin/foo"
          environment "FOO=BAR"]
      (with-redefs [process-exists? (spy/stub true)
                    get-name (spy/stub name)
                    get-ppid (spy/stub ppid)
                    get-cmdline (spy/stub cmdline)
                    get-environment-variables (spy/stub environment)]
        (is (= {(keyword pid) {:name name :ppid ppid :cmdline cmdline :environment environment}}
               (get-info pid)))))))
    
    
    ;; (get-info ..pid..) => (contains { "..pid.." {:name ..name.. :ppid ..ppid.. :cmdline ..cmdline.. :environment ..environment..}})
    ;; (provided
    ;;  (process-exists? ..pid..) => true
    ;;  (keyword ..pid..) => "..pid.."
    ;;  (get-name ..pid..) => ..name..
    ;;  (get-ppid ..pid..) => ..ppid..
    ;;  (get-cmdline ..pid..) => ..cmdline..
    ;;  (get-environment-variables ..pid..) => ..environment..))

  ; (fact "throws an exception if the process PID does not exist"
  ;   (get-info ..pid..) => (throws (str "No process '" ..pid.. "' found in /proc"))
  ;   (provided
  ;     (process-exists? ..pid..) => false))

  ; (facts "retrieving the process name"

    ; (fact "retrieving the process name via /proc/<pid>/comm"
    ;   (get-name ..pid..) => "bash"
    ;   (provided
    ;     (read-proc-file ..pid.. "comm") => "bash")

      ; (get-name ..pid..) => "bash"
      ; (provided
      ;   (read-proc-file ..pid.. "comm") => "/bin/bash")

      ; (get-name ..pid..) => "java"
      ; (provided
      ;   (read-proc-file ..pid.. "comm") => "/usr/local/bin/java")

      ; (get-name ..pid..) => "java"
      ; (provided
      ;   (read-proc-file ..pid.. "comm") => "/usr/local/bin/java\n")

      ; (get-name ..pid..) => "sshd"
      ; (provided
      ;   (read-proc-file ..pid.. "comm") => "/usr/sbin/sshd -D"))

    ; (fact "return the PID if unable to get the process name"
    ;   (get-name ..pid..) => ..pid..
    ;   (provided
    ;     (read-proc-file ..pid.. "comm") =throws=> (ex-info "Fake error while reading /proc/<pid>/comm" {}))))

  ; (facts "retrieving the parent process ID"

    ; (fact "retrieving the process name via /proc/<pid>/stat"
    ;   (get-ppid ..pid..) => "..ppid.."
    ;   (provided
    ;     (read-proc-file ..pid.. "stat") => (str "1032 (dockerd) S 1 " ..ppid.. " 1032 0 -1 1077944576 14678 22815 259 7 304 115 5 7 20 0 10 0 600 447438848 6233 18446744073709551615 1 1 0 0 0 0 0 0 2143420159 18446744073709551615 0 0 17 1 0 0 38 0 0 0 0 0 0 0 0 0 0\n"))

    ; (fact "return an empty string if unable to get the parent process ID"
    ;   (get-ppid ..pid..) => ""
    ;   (provided
    ;     (read-proc-file ..pid.. "stat") =throws=> (ex-info "Fake error while reading /proc/<pid>/stat" {})))))

  ; (facts "retrieving the process cmdline"

    ; (fact "retrieving the process name via /proc/<pid>/cmdline"
    ;   (get-cmdline ..pid..) => "/usr/bin/python-Es/usr/sbin/tuned-l-P"
    ;   (provided
    ;     (read-proc-file ..pid.. "cmdline") => "/usr/bin/python-Es/usr/sbin/tuned-l-P")

    ; (fact "return false if /proc/<pid>/cmdline is empty"
    ;   (get-cmdline ..pid..) => false
    ;   (provided
    ;     (read-proc-file ..pid.. "cmdline") => "")

    ; (fact "stripping out null character and replacing with a space"
    ;   (get-cmdline ..pid..) => "/usr/sbin/VBoxService --pidfile /var/run/vboxadd-service.sh"
    ;   (provided
    ;     (read-proc-file ..pid.. "cmdline") => "/usr/sbin/VBoxService\u0000--pidfile\u0000/var/run/vboxadd-service.sh\u0000")))

    ; (fact "return false if unable to get the process cmdline"
    ;   (get-cmdline ..pid..) => ""
    ;   (provided
    ;     (read-proc-file ..pid.. "cmdline") =throws=> (ex-info "Fake error while reading /proc/<pid>/cmdline" {})))))

  ; (facts "retrieving the parent process ID"

    ; (fact "retrieving the process environment variables via /proc/<pid>/environ"
    ;   (get-environment-variables ..pid..) => ["LANG=en_US.UTF-8" "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin" "NOTIFY_SOCKET=/run/systemd/notify"]
    ;   (provided
    ;     (read-proc-file ..pid.. "environ") => "LANG=en_US.UTF-8\u0000PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin\u0000NOTIFY_SOCKET=/run/systemd/notify")

    ; (fact "return an empty collection if unable to get the environment variables"
    ;   (get-environment-variables ..pid..) => []
    ;   (provided
    ;     (read-proc-file ..pid.. "environ") =throws=> (ex-info "Fake error while reading /proc/<pid>/environ" {}))))))
