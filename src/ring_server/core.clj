(ns ring-server.core
  "Starts Server and process requests"
  (:require [compojure.core :refer [defroutes POST]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring-server.check :refer [validate-input]])
  (:gen-class))

(defonce parameters ["a" "b" "c" "d" "e"])

(defn test-API
  "Example"
  [request]
  (try
    (let [validated-input (validate-input parameters (:params request))]
      {:status 200 :body validated-input})
    (catch Exception e
      {:status 500 :body (.getMessage e)})))

(defroutes main-routes
  "Defines routes to the anomaly-detection component"
  (POST "/test" [] test-API))

(def app
  "Adds middlewares to the application"
  (-> main-routes
      wrap-params
      wrap-multipart-params
      wrap-json-response
      (wrap-json-body {:keywords? true})))

(defn -main
  "Starts jetty server"
  [& args]
  (jetty/run-jetty app {:port 4001}))
