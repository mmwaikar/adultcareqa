(ns adultcareqa.core
  (:require [adultcareqa.csv :as csv]
            [adultcareqa.mongo :as mongo]))

(defn- create-collection [collection-name data]
  (mongo/mass-inserts collection-name
                        (:columns data)
                        (:values data)))

(defn create-data-definitions []
  (create-collection mongo/*data-definition-collection* (csv/data-definitions)))

(defn create-data [filename]
  (create-collection mongo/*data-collection* (csv/typed-data (csv/data filename))))