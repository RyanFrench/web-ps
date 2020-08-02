(ns unit.web-ps.core-test
    (:require [web-ps.core :refer :all]
              [midje.sweet :refer :all]))

(facts "initialising the web server"

  (fact "starts the web server"
    (initialise) => irrelevant
    (provided
      (start-server) => irrelevant :times 1)

  (fact "shuts down if an exception occurs during initialisation"
    (initialise) => irrelevant
    (provided
      (start-server) =throws=> (Exception. "Throw error starting web server")
      (shutdown) => irrelevant :times 1))))
