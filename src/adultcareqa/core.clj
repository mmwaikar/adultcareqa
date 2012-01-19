(ns adultcareqa.core
  (:require [adultcareqa.csv :as csv]
            [adultcareqa.mongo :as mongo]))

(defn- create-collection [collection-name data]
  (mongo/mass-inserts collection-name
                        (:columns data)
                        (:values data)))

(defn create-data-definitions []
  (create-collection mongo/*data-definition-collection* (csv/data-definitions)))

(defn create-data
  ([filename] (create-data filename false))
  ([filename typed?]
     (let [data (csv/data filename)]
       (if typed?
         (create-collection mongo/*data-collection* (csv/typed-data data))
         (create-collection mongo/*data-collection* data)))))