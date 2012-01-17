(ns adultcareqa.csv
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(def ^:dynamic *data1* "../adultcareqa/data/Database Structure-QA-Table 1.csv")
(def ^:dynamic *data2* "../adultcareqa/data/Database Structure-QA-Table 2.csv")
(def ^:dynamic *data-definition-file* "../adultcareqa/data/Data Definition Document-Data Definitions.csv")

(defn- read-csv-data [filename]
  (with-open [in-file (io/reader filename)]
    (doall
     (csv/read-csv in-file))))

(defn- get-column-names [names]
  (let [non-empty-names (remove str/blank? names)
        seconds (map #(str/lower-case (second (re-seq #"\w+" %))) non-empty-names)]
    (map keyword seconds)))

(defn- columns-values [keys values]
  (hash-map :columns keys :values values))

(defn data-definitions []
  (let [non-empty-data (remove #(every? str/blank? %) (read-csv-data *data-definition-file*))
        keys (get-column-names (first non-empty-data))
        values (map #(remove str/blank? %) (rest non-empty-data))]
    (columns-values keys values)))

(defn data [filename]
  (let [qa-data (read-csv-data filename)]
    (columns-values (first qa-data) (rest qa-data))))