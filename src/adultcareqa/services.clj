(ns adultcareqa.services
  (:use [compojure.core]
        [adultcareqa.mongo :as mongo]
        [clojure.data.json :as json]
        [ring.middleware.json-params]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/json-str data)})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ---- routes ----                                      ;;
;; /qa -> all data                                       ;;
;; /qa/period/2011 -> all data for the year 2011         ;;
;; /qa/period/12011 -> data for 1st month of 2011        ;;
;; /qa/column/RP_N1 -> data for column RP_N1             ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes app
  (GET "/qa" [] (mongo/memoized-get-data {:for-json true}))
  (GET "/qa/def" [] (mongo/memoized-get-data-definitions {:for-json true}))
  (GET "/qa/period/:period" [period] (str "Hello " period))
  (GET "/qa/period/:period/column/:column" [period column] (str "Hello " period ", column: " column))) ;;(mongo/filter-by {:period period})))

;; (defroutes handler
;;   (GET "/" []
;;     (json-response {"hello" "world"}))

;;   (PUT "/" [name]
;;     (json-response {"hello" name})))

;; (def app
;;   (-> handler
;;     wrap-json-params))