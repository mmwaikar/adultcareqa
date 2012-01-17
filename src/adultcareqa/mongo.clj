(ns adultcareqa.mongo
  (:use [somnium.congomongo]
        [adultcareqa.utils :only [get-date]]))

(def ^:dynamic *db* "adultcare")
(def ^:dynamic *host* "localhost")
(def ^:dynamic *port* 27017)

(def ^:dynamic *data-collection* "dataqa")
(def ^:dynamic *data-definition-collection* "datadefqa")

(def ^:dynamic conn (make-connection *db* :host *host* :port *port*))

(defn- data-with-type [value type]
  (cond
   (= type "####") (Integer/parseInt value)
   (= type "%%%%") (Double/parseDouble value)
   (= type "$$$$.$$") (Double/parseDouble value)
   (= type "######") (get-date value)
   :else value))

(defn get-column-type [name]
  (let [column-names-types (with-mongo conn
                             (fetch *data-definition-collection*
                                    :only [:name :type]))
        column-row (some #(if (= name (:name %))
                            %
                            {:name "" :type ""}) column-names-types)] (prn column-row)
    (:type column-row)))
   
(defn mass-inserts [collection-name columns values]
  (with-mongo conn
    (mass-insert! collection-name
                  (for [row values]
                    (zipmap columns row)))))

;; (defn typed-data [columns values]
;;   (let [column-types ))