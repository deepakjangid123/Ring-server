(ns ring-server.core-test
  "Test cases"
  (:require [clojure.test :refer :all]
            [ring-server.core :refer [test-API parameters]]
            [clojure.spec.gen.alpha :as gen]
            [ring-server.spec :as sp]
            [ring-server.check :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.test.check :as tc]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gn]))

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

(def email-gen
  (gn/fmap (fn [[name domain tld]]
             (str name "@" domain "." tld))
          (gn/tuple (gn/not-empty gn/string-alphanumeric)
                    (gn/not-empty gn/string-alphanumeric)
                    (gn/not-empty gn/string-alphanumeric))))

(tc/quick-check 100 email-gen)

(def sort-idempotent-prop
  (prop/for-all [v (gn/vector gn/int)]
    (= (sort v) (sort (sort v)))))

(def prop-sorted-first-less-than-last
  (prop/for-all [v (gn/not-empty (gn/vector gn/int))]
    (let [s (sort v)]
      (< (first s) (last s)))))

(tc/quick-check 100 sort-idempotent-prop)
(tc/quick-check 100 prop-sorted-first-less-than-last)
