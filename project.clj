(defproject adultcareqa "1.0.0-SNAPSHOT"
  :description "Fetch Adult Care QA data from CSV files and store it in MongoDB."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/data.csv "0.1.0"]
                 [congomongo "0.1.7"]
                 [clj-time "0.3.4"]]
  :dev-dependencies [[swank-clojure "1.3.2"]
                     [midje "1.3.1"]
                     [lein-midje "1.0.7"]])