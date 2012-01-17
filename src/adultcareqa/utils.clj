(ns adultcareqa.utils
  (:use [clj-time.core :only [date-time]]))

(defn get-date [s]
  (let [month-width (- (count s) 4)
        year (Integer/parseInt (subs s month-width))
        month (Integer/parseInt (subs s 0 month-width))]
    (.toDate (date-time year month))))