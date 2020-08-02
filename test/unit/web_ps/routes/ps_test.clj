(ns unit.web-ps.routes.ps-test
    (:require [web-ps.routes.ps :refer :all]
              [web-ps.proc :as proc]
              [midje.sweet :refer :all]))

(facts "/ps route"

  (fact "Filters out the current process and all associated processes"
    (ps) => { "..pid-1.." {:ppid "..pid-1.."} "..pid-2.." {:ppid "..pid-2.."}}
    (provided
      (proc/get-info "self") => { :self {:ppid "..pid-3.."}}
      (get-pids) => (seq [..pid-1.. ..pid-2.. ..pid-3..])
      (proc/get-info ..pid-1..) => { "..pid-1.." {:ppid "..pid-1.."}}
      (proc/get-info ..pid-2..) => { "..pid-2.." {:ppid "..pid-2.."}}
      (proc/get-info ..pid-3..) => { "..pid-3.." {:ppid "..pid-3.."}}))

  (fact "Returns a 500 if an error occurs while retrieving the processes"
    (handler ..request..) => {:status 500 :body "Fake exception while getting processes"}
    (provided
      (ps) =throws=> (Exception. "Fake exception while getting processes")))

  (fact "Returns a 500 if an error occurs while retrieving the processes"
    (handler ..request..) => {:status 500 :body {:message "Fake exception while getting processes" :cause "Extra metadata for exception"}}
    (provided
      (ps) =throws=> (ex-info "Fake exception while getting processes" {:message "Fake exception while getting processes" :cause "Extra metadata for exception"}))))
