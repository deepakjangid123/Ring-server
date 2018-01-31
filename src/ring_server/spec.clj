(ns ring-server.spec
  "Define all the specs"
  (:require [clojure.spec.alpha :as s]))

;; Defining all the specs
(s/def ::coll-of-strings (s/nilable (s/coll-of string?))) ;; Either collection of strings or nil
(s/def ::name string?) ;; String else according to spec parameter does't follow the criteria, is thrown
(s/def ::score int?) ;; Int
(s/def ::number number?) ;; Number
(s/def ::boolean boolean?) ;; Boolean
