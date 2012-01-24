(ns adultcareqa.charts
  (:refer-clojure :exclude [replace reverse])
  (:use [clojure.string :as str]
        [adultcareqa.mongo :as mongo]))

;; (defn- get-label-value [row]
;;   (let [month-year (:Date row)
;;         month (join (take (if (= (count month-year) 6) 2 1) month-year))
;;         val (:RP_N1 row)]
;;     (hash-map :label (str "Month " month) :value val)))

(defn- get-label-value [row]
  (let [month-year (:Date row)
        month (join (take (if (= (count month-year) 6) 2 1) month-year))
        val (get-in row [:column :value])]
    (hash-map :label (str "Month " month) :value val)))

(defn get-charts-map
  "Returns a map, which can be easily converted to the
   JSON representation as expected by Fusion charts."
  [column x-axis-name data-array]
  (let [description (mongo/get-column-description column)]
    {
     :chart {
             :caption (str "Monthly " description " Summary")
             :xAxisName x-axis-name
             :yAxisName description
             }
     :data (map #(get-label-value %) data-array)
     }))