(ns adultcareqa.mongo
  (:refer-clojure :exclude [extend replace reverse])
  (:use [somnium.congomongo]
        [clojure.string :as str]
        [clojure.set :only [intersection]]
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

(defn columns-in-data
  "Returns all the columns present in the data in the QA table."
  ([] (columns-in-data {:for-json false}))
  ([{for-json :for-json :or {for-json false}}]
     (map #(name %) (keys (first (memoized-get-data {:for-json for-json}))))))

(defn columns-in-data-definitions
  "Returns all the columns present in the data definitions table."
  ([] (columns-in-data-definitions {:for-json false}))
  ([{for-json :for-json :or {for-json false}}]
     (map #(:name %) (memoized-get-data-definitions {:for-json for-json}))))

(defn common-columns-in-data-and-definitions
  "Returns columns from data definitions table for which
   there is some data in QA table."
  ([] (common-columns-in-data-and-definitions {:for-json false}))
  ([{for-json :for-json :or {for-json false}}]
     (intersection (into #{} (columns-in-data {:for-json for-json}))
                   (into #{} (columns-in-data-definitions {:for-json for-json})))))

(defn get-data-definitions-for-columns-in-data
  "Returns the data definitions for only those columns
   which are actually present in the data."
  ([] (get-data-definitions-for-columns-in-data {:for-json false}))
  ([{for-json :for-json :or {for-json false}}]
     (let [common-columns (common-columns-in-data-and-definitions {:for-json for-json})]
       (filter #(contains? common-columns (:name %)) (memoized-get-data-definitions {:for-json for-json})))))

(defn mass-inserts
  "Does mass inserts of values in a collection-name collection.
   columns represents the various columns in collection-name."
  [collection-name columns values]
  (with-mongo conn
    (mass-insert! collection-name
                  (for [row values]
                    (zipmap columns row)))))

(defn get-filtered-data
  "Returns data filtered by column for a particular period."
  [{period :period column :column
    :or {period "" column ""}}]
  (let [data (memoized-get-data)
        for-period (filter #(> (.indexOf (:Date %) period) -1) data)]
    (map #(select-keys % (vector (keyword column) :Date)) for-period)))

;; (defmacro filter-by [collection {period :period column :column
;;                              :or {period "" column ""}}]
;;   (let [col-list (if-not (str/blank? column)
;;                    (list :only [column]))
;;         where-list (if-not (str/blank? period)
;;                      (list :where {:Date (re-pattern period)}))
;;         y (concat '(somnium.congomongo/with-mongo conn) (list (concat '(somnium.congomongo/fetch) (list collection) col-list where-list)))]
;;     (prn y)
;;     (eval y)))

;; example of fetching some columns
;; (with-mongo conn (fetch *data-definition-collection* :only [:name :type]))
