(ns ring-server.spec
  "Define all the specs"
  (:require [clojure.spec.alpha :as s]))

;; Defining all the specs
(s/def ::coll-of-strings (s/or :coll (s/coll-of string?) :nil nil?))
(s/def ::name string?)
(s/def ::score int?)
(s/def ::number number?)
(s/def ::boolean boolean?)
