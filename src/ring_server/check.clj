(ns ring-server.check
  "Validation of input and output"
  (:require [clojure.spec.alpha :as s]
            [ring-server.spec :as sp]))

(defmacro <try>
  ([body] `(<try> nil ~body))
  ([input & body]
   `(try
      (do ~@body)
      (catch Exception ex#
        (or ~input ex#)))))

(defn check-input
  "Checks whether input is in the expected form or not else throws an error"
  [input spec]
  (if (= ::s/invalid (s/conform spec input))
    (throw (ex-info (str "Invalid Input! " input) (s/explain-data spec input)))
    input))

(defn input-or-nil
  "Checks if input is in the expected form or not else returns nil"
  [input spec]
  (<try>
    (when-not (= ::s/invalid (s/conform spec input))
      input)))

(defn read-string-except
  "1). Returns read-string'ed output
   2). If it returns a symbol then converts it into string
   3). If exception is there then returns the input itself"
  [in]
  (<try>
    in
    (let [x (read-string in)]
      (cond
       (symbol? x) (str x)
       :else x))))

(defn validate-input
  "Validates input and returns result"
  [parameters input]
  (let [flds (check-input
              (read-string-except (get input "a"))
              ::sp/coll-of-strings)
        num-fld (check-input
                 (read-string-except (get input "b"))
                 ::sp/number)
        boolean-fld (check-input
                     (read-string-except (get input "c"))
                     ::sp/boolean)
        nil-fld (input-or-nil
                  (read-string-except (get input "d"))
                  ::sp/score)
        str-fld (input-or-nil
                  (read-string-except (get input "e"))
                  ::sp/name)]
    (zipmap parameters [flds num-fld boolean-fld nil-fld str-fld])))
