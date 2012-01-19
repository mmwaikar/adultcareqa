(defproject adultcareqa "1.0.0-SNAPSHOT"
  :description "Fetch Adult Care QA data from CSV files and store it in MongoDB."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/data.csv "0.1.0"]
                 [congomongo "0.1.7"]
                 [clj-time "0.3.4"]
                 [compojure "1.0.1"]
                 [ring/ring-core "1.0.1"]
                 [ring/ring-devel "1.0.1"]
                 [ring/ring-jetty-adapter "1.0.1"]
                 [ring-json-params "0.1.3"]
                 [org.clojure/data.json "0.1.1"]]
  :dev-dependencies [[swank-clojure "1.3.2"]
                     [midje "1.3.1"]
                     [lein-ring "0.5.4"]
                     [lein-midje "1.0.7"]]
  :ring {:handler adultcareqa.services/app})









