(ns adultcareqa.test.utils
  (:use [clj-time.core :only [year]]
        [adultcareqa.utils]
        [midje.sweet])
  (:import [java.util Date]
           [java.text SimpleDateFormat]))

(def ^:dynamic *YYYY* "2012")
(def ^:dynamic *MYYYY* "022012")
(def ^:dynamic *MMYYYY* "112012")
(def ^:dynamic *simple-date-format* (SimpleDateFormat. "yyyy"))

(defn- get-year [s]
  (Integer/parseInt (.format *simple-date-format* (.parse *simple-date-format* s))))

;; (facts "about conversion to date from a string of the form MMYYYY"
;;        (year (get-date *YYYY*)) => (get-year *YYYY*))