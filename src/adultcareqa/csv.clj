(ns adultcareqa.csv
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [adultcareqa.utils :as utils]
            [adultcareqa.mongo :as mongo]))

(def ^:dynamic *data1* "../adultcareqa/data/Database Structure-QA-Table 1.csv")
(def ^:dynamic *data2* "../adultcareqa/data/Database Structure-QA-Table 2.csv")
(def ^:dynamic *data-definition-file* "../adultcareqa/data/Data Definition Document-Data Definitions.csv")

(defn- read-csv-data
  "Reads data from a csv file."
  [filename]
  (with-open [in-file (io/reader filename)]
    (doall
     (csv/read-csv in-file))))

(defn- get-column-names
  "Extracts the names of the columns (which are in the form [Field Name]
   etc.) from the data definition csv file."
  [names]
  (let [non-empty-names (remove str/blank? names)
        seconds (map #(str/lower-case (second (re-seq #"\w+" %))) non-empty-names)]
    (map keyword seconds)))

(defn- columns-values [keys values]
  (hash-map :columns keys :values values))

(defn- data-with-type
  "The data read from the csv file is all strings. This
   converts the data to its proper type."
  [value type]
  (cond
   (= type "####") (Integer/parseInt value)
   (= type "%%%%") (Double/parseDouble value)
   (= type "$$$$.$$") (Double/parseDouble value)
   (= type "######") (utils/get-date value)
   :else value))

(defn- get-column-type
  "Fetches the type of the column from the *data-definition-collection*."
  [name]
  (let [column-names-types (mongo/get-data-definitions-memo)
        column-row (first (filter #(= name (:name %)) column-names-types))]
    (if (nil? column-row)
      ""
      (:type column-row))))

(defn- typed-single-row-data
  "Converts a row of string values to a row of typed values."
  [columns values]
  (map #(data-with-type %2 (get-column-type %1)) columns values))

(defn data-definitions
  "Reads the *data-definition-file* and returns the data in a map
   of the form {:columns columns :values values}."
  []
  (let [non-empty-data (remove #(every? str/blank? %) (read-csv-data *data-definition-file*))
        keys (get-column-names (first non-empty-data))
        values (map #(remove str/blank? %) (rest non-empty-data))]
    (columns-values keys values)))

(defn data
  "Returns data from a csv file with a structure similar to
   Database Structure-QA-Table 1.csv file, as all strings."
  [filename]
  (let [qa-data (read-csv-data filename)]
    (columns-values (first qa-data) (rest qa-data))))

(defn typed-data
  "Converts the all-strings data of a csv file as per the
   type mentioned in the *data-definition-file*."
  [data]
  (let [columns (:columns data)
        values (:values data)
        typed-values (map #(typed-single-row-data columns %) values)]
    (columns-values columns typed-values)))