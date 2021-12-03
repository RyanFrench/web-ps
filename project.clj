(defproject web-ps "1.0.0"
  :description "A web service for retrieving details of the processes running on the server"

  :dependencies [;; Core
                 [org.clojure/clojure "1.10.1"]
                 ;; 3rd Party
                 [http-kit "2.3.0"]
                 [compojure "1.6.1"]
                 [ring/ring-core "1.8.1"]
                 [ring/ring-json "0.5.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [cheshire "5.10.0"]]

  :profiles {
    :dev {
      :dependencies [[clj-http "3.10.1"]
                     [tortue/spy "2.0.0"]]
    }
  }

  :plugins [[lein-cloverage "1.1.2"]
            [lein-eftest "0.5.9"]
            [lein-kibit "0.1.8"]]

  :aliases {"test" ["eftest"]
            "unit" ["eftest" ":unit"]
            "integration" ["eftest" ":integration"]
            "coverage" ["cloverage"]
            "lint" ["kibit"]}

  :uberjar-name "web-ps.jar"
  :main web-ps.core)
