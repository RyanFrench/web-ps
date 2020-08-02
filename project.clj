(defproject web-ps "1.0.0"
  :description "A web service for retrieving details of the processes running on the server"

  :dependencies [;; Core
                 [org.clojure/clojure "1.8.0"]
                 ;; 3rd Party
                 [http-kit "2.2.0"]
                 [compojure "1.6.0"]
                 [ring/ring-core "1.6.2"]
                 [ring/ring-json "0.4.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [cheshire "5.8.0"]]

  :profiles {
    :dev {
      :dependencies [[clj-http "3.7.0"] [midje "1.8.3"]]
    }
  }

  :plugins [[lein-cloverage "1.0.9"]
            [lein-kibit "0.1.2"]
            [lein-midje "3.2.1"]]

  :aliases {
    "test" ["midje" "unit.web-ps.*" "integration.web-ps.*"]
    "unit" ["midje" "unit.web-ps.*"]
    "integration" ["midje" "integration.web-ps.*"]
    "coverage" ["cloverage"]
    "lint" ["kibit"]
  }

  :uberjar-name "web-ps.jar"
  :aot :all
  :main web-ps.core
  :resource-paths ["config"])
