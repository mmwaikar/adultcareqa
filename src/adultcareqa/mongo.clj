(ns adultcareqa.mongo
  (:refer-clojure :exclude [extend replace reverse])
  (:use [somnium.congomongo]
        [clojure.string :as str]
        [adultcareqa.utils :only [get-date]]))

(def ^:dynamic *db* "adultcare")
(def ^:dynamic *host* "localhost")
(def ^:dynamic *port* 27017)

(def ^:dynamic *data-collection* "dataqa")
(def ^:dynamic *data-definition-collection* "datadefqa")

(def ^:dynamic conn (make-connection *db* :host *host* :port *port*))

(defn- get-data-without-id-column [data]
  (map #(dissoc % :_id) data))

(defn- get-data
  "Returns all columns for all records from *data-collection*."
  ([] (get-data {:for-json false}))
  ([{for-json :for-json :or {for-json false}}]
     (let [data (with-mongo conn
                  (fetch *data-collection*))]
       (if for-json
         (get-data-without-id-column data)
         data))))

(defn- get-data-definitions
  "Returns (name, type) columns for all records from *data-definition-collection*."
  ([] (get-data-definitions {:for-json false}))
  ([{for-json :for-json :or {for-json false}}]
     (let [data (with-mongo conn
                  (fetch *data-definition-collection*))]
       (if for-json
         (get-data-without-id-column data)
         data))))

(def memoized-get-data
  "Memoizes the get-data function."
  (memoize get-data))

(def memoized-get-data-definitions
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

;; (defn filter-by [{period :period column :column
;;                   :or {period "" column ""}}]
;;   (if-not (str/blank? column)
;;     (list :only [column]))
;;   (if-not (str/blank? period)
;;     (list :where {:Date
;;
;; example of fetching some columns
;; (with-mongo conn (fetch *data-definition-collection* :only [:name :type]))