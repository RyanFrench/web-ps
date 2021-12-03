(ns integration.web-ps.routes.ps-test
    (:require [web-ps.core :refer :all]
              [clj-http.client :as client]
              [clojure.test :refer [deftest is testing]]
              [spy.core :as spy]
              [spy.assert :as assert]))

;; (facts "ps route"
;;   (against-background [
;;     (before :contents (initialise))
;;     (after :contents (shutdown))]

;;     (fact "returns ps info on a valid GET request"
;;       (let [response (client/get "http://localhost:8080/ps" {:as :json})
;;             body (response :body)]
;;         (response :status) => 200
;;         (clojure.pprint/pprint (seq body))
;;         (doseq [[pid proc] (seq body)]
;;             (keys proc) => (contains #{:ppid :name :cmdline :environment}))))

;;     (fact "returns a 301 redirect when a trailing slash is used"
;;       (let [response (client/get "http://localhost:8080/ps/")]
;;         (response :status) => 200))))
