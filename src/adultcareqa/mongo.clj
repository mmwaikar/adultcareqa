(ns adultcareqa.mongo
  (:refer-clojure :exclude [extend])
  (:use [somnium.congomongo]
        [clj-time.core :as cljt]
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

(def get-data-definitions-memo (memoize get-data-definitions))

(defn- data-with-type [value type]
  (cond
   (= type "####") (Integer/parseInt value)
   (= type "%%%%") (Double/parseDouble value)
   (= type "$$$$.$$") (Double/parseDouble value)
   (= type "######") (get-date value)
   :else value))

(defn- get-column-type [name]
  ;; TODO: cache this data for later use
  (let [column-names-types (get-data-definitions-memo)
        column-row (first (filter #(= name (:name %)) column-names-types))]
    (if (nil? column-row)
      ""
      (:type column-row))))

(defn- typed-single-row-data [columns values] ;col %1 val %2
  (map #(data-with-type %2 (get-column-type %1)) columns values))
   
(defn mass-inserts [collection-name columns values]
  (with-mongo conn
    (mass-insert! collection-name
                  (for [row values]
                    (zipmap columns row)))))

(defn typed-data [data]
  (let [columns (:columns data)
        values (:values data)
        t1 (cljt/now)
        typed-values (map #(typed-single-row-data columns %) values)]
    ;; (prn "tt:" (cljt/in-secs (cljt/interval t1 (now))))
    (hash-map :columns columns :values typed-values)))