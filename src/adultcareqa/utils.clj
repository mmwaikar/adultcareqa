(ns adultcareqa.utils
  (:refer-clojure :exclude [replace reverse])
  (:use [clj-time.core :only [date-time]]
        [clojure.string :as str]))

(defn get-date
  "Returns a java.util.Date object with local time where
   s is in the format of MMYYYY or MYYYY."
  [s]
  (let [month-width (- (count s) 4)
        year-str (subs s month-width)
        month-str (subs s 0 month-width)
        year (if (str/blank? year-str) 0 (Integer/parseInt year-str))
        month (if (str/blank? month-str) 1 (Integer/parseInt month-str))]
    (.toDate (date-time year month))))