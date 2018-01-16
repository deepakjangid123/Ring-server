(ns ring-server.core-test
  "Test cases"
  (:require [clojure.test :refer :all]
            [ring-server.core :refer :all]
            [clojure.spec.gen.alpha :as gen]
            [ring-server.spec :as sp]
            [ring-server.check :refer :all]
            [clojure.spec.alpha :as s]))

(def testcase-params
  (gen/sample
    (s/gen
      (s/cat
        :a (s/coll-of string?)
        :b number?
        :c boolean?
        :d (s/or :s string? :n nil?)))))


(deftest test-testing
  (testing "Testing main function"
    (is (= #{200}
           (set
            (mapv
             #(:status (test-API (assoc {} :params %)))
             (mapv #(zipmap parameters (map str %)) testcase-params)))))))

(deftest test2
  (testing "Exception"
    (is (= 500
           (:status (test-API {:params {"a" nil "b" "ASA" "c" "asd" "d" "po"}}))))))
