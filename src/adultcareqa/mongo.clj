(ns adultcareqa.mongo
  (:refer-clojure :exclude [extend])
  (:use [somnium.congomongo]
        [adultcareqa.utils :only [get-date]]))

(def ^:dynamic *db* "adultcare")
(def ^:dynamic *host* "localhost")
(def ^:dynamic *port* 27017)

(def ^:dynamic *data-collection* "dataqa")
(def ^:dynamic *data-definition-collection* "datadefqa")

(def ^:dynamic conn (make-connection *db* :host *host* :port *port*))

(defn- get-data-definitions
  "Returns (name, type) columns for all records from *data-definition-collection*."
  []
  (with-mongo conn
    (fetch *data-definition-collection*
           :only [:name :type])))

(def get-data-definitions-memo
  "Memoizes the get-data-definitions function."
  (memoize get-data-definitions))
   
(defn mass-inserts
  "Does mass inserts of values in a collection-name collection.
   columns represents the various columns in collection-name."  
  [collection-name columns values]
  (with-mongo conn
    (mass-insert! collection-name
                  (for [row values]
                    (zipmap columns row)))))