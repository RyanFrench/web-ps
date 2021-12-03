(ns unit.web-ps.routes.ps-test
  (:require [web-ps.routes.ps :refer [ps get-pids handler]]
            [web-ps.proc :as proc]
            [clojure.test :refer [deftest is testing]]
            [spy.core :as spy]
            [spy.assert :as assert]))

(deftest ^:unit ps-route
  (testing "Filters out the current process and all associated processes"
    (let [pid-1 "1000"
          pid-2 "1001"
          pid-3 "1002"]
      (with-redefs [get-pids (spy/stub (seq [pid-1 pid-2 pid-3]))
                    proc/get-info (spy/mock (fn [pid] 
                                              (if (= pid "self")
                                                {:self {:ppid pid-3}}
                                                {pid {:ppid pid}})))]
        (is (= {pid-1 {:ppid pid-1} pid-2 {:ppid pid-2}} (ps)))
        (assert/called-with? get-pids)
        (assert/called-with? proc/get-info pid-1)
        (assert/called-with? proc/get-info pid-2)
        (assert/called-with? proc/get-info pid-3))))

  (testing "Returns a 500 if an error occurs while retrieving the processes"
    (with-redefs [ps (spy/stub-throws (Exception. "Fake exception while getting processes"))]
      (is (= {:status 500 :body "Fake exception while getting processes"} (handler "..request.."))))))