(ns adultcareqa.core
  (:require [adultcareqa.csv :as csv]
            [adultcareqa.mongo :as mongo]))

(defn- create-collection [collection-name data]
  (mongo/mass-inserts collection-name
                        (:columns data)
                        (:values data)))

(defn create-data-definitions []
  (create-collection *data-definition-collection* (csv/data-definitions *data-definition-file*)))

(defn create-data [filename]
  (let [data (csv/csv-data csv-file-name)
        columns (first (:columns data))]
    (create-collection *data-collection* data)))