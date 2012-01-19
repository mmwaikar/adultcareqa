(ns adultcareqa.run
  (:use [ring.adapter.jetty]
        [adultcareqa.services]))

(run-jetty #'web/app {:port 3000})