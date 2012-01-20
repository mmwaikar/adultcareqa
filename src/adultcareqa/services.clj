(ns adultcareqa.services
  (:use [compojure.core]
        [adultcareqa.mongo :as mongo]
        [clojure.data.json :as json]
        [ring.middleware.json-params])
  (:require [compojure.route :as route]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json;charset=UTF-8"}
   :body (json/json-str data)})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ---- routes ----                                      ;;
;; /qa -> all data                                       ;;
;; /qa/period/2011 -> all data for the year 2011         ;;
;; /qa/period/12011 -> data for 1st month of 2011        ;;
;; /qa/column/RP_N1 -> data for column RP_N1             ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes app
  (GET "/qa" []
       (json-response (mongo/memoized-get-data {:for-json true})))
  
  (GET "/qa/def" []
       (json-response (mongo/memoized-get-data-definitions {:for-json true})))
  
  ;; (GET "/qa/period/:period" [period] (json-response (hash-map :returnVal (str "Hello " period))))
  (GET "/qa/period/:period" [period] (json-response (str "Hello " period)))
  (GET "/qa/period/:period/column/:column" [period column] (str "Hello " period ", column: " column))
  
  (route/files "/" {:root "public"})
  (route/not-found "Page not found")) ;;(mongo/filter-by {:period period})))

;; (defroutes handler
;;   (GET "/" []
;;     (json-response {"hello" "world"}))

;;   (PUT "/" [name]
;;     (json-response {"hello" name})))

;; (def app
;;   (-> handler
;;     wrap-json-params))