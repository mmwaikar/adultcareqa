(ns adultcareqa.charts
  (:refer-clojure :exclude [replace])
  (:use [clojure.string :as str]))

(defn- get-label-value [row]
  (let [month-year (:Date row)
        month (join (take (if (= (count month-year) 6) 2 1) month-year))
        val (:RP_N1 row)]
    (hash-map :label (str "Month " month) :value val)))

(defn get-charts-map
  "Returns a map, which can be easily converted to the
   JSON representation as expected by Fusion charts."
  [caption x-axis-name y-axis-name data-array]
  {
   :chart {
           :caption caption
           :xAxisName x-axis-name
           :yAxisName y-axis-name
           }
   :data (map #(get-label-value %) data-array)
   })