(ns clj-brainfuck.core
  (:require [clojure.pprint :refer [pprint]]))

(defn parse-code [code] (map char code))

(defn read-char [& args] (-> System/in .read))

(defn change-in
  "If value exists just applies function on it
  If value not exists initializes it with 0 and applies function then"
  [coll keys function]
  (if (get-in coll keys)
    (update-in coll keys function)
    (assoc-in  coll keys (function 0))))

(defn eval-char [{:keys [pointer cells] :as state} ch]
  (case ch
    \> (change-in state [:pointer] inc)
    \< (change-in state [:pointer] dec)
    \+ (change-in state [:cells pointer] inc)
    \- (change-in state [:cells pointer] dec)
    \, (assoc-in  state [:cells pointer] (read-char))
    \. (do (when (get cells pointer)
             (print (char (cells pointer))))
           state)
    nil))

(declare eval-brainfuck)

(defn parse-brackets [code]
  "Returns vector of code in brackets and remaining code"
  (loop [in-code [] out-code [] code code brackets 0]
    ;(pprint in-code)
    ;(pprint out-code)
    ;(pprint code)
    ;(pprint brackets)
    (if (empty? code)
      [in-code out-code]
      (let [ch (first code)]
        (cond
          (= ch \[) (recur in-code out-code (rest code) (inc brackets))
          (= ch \]) (recur in-code out-code (rest code) (dec brackets))
          :else (if (> brackets 0)
                  (recur (conj in-code ch) out-code (rest code) brackets)
                  (recur in-code (conj out-code ch) (rest code) brackets)))))))

(defn cell-present [{:keys [pointer cells]}]
  (let [v (get cells pointer)]
    (not (or (nil?  v)
             (zero? v)))))

(defn eval-brackets
  "Returns vector of remaining code and state"
  [code state]
  (let [[in-code out-code] (parse-brackets code)]
    (loop [{:keys [pointer cells] :as state} state]
      (if (cell-present state)
        (recur (eval-brainfuck in-code state))
        [out-code state]))))

(defn new-state []
  {:pointer 0
   :cells []})

(defn eval-brainfuck
  "Code should be seq of chars"
  [code & state]
  (loop [code  code
         state (or (first state)
                   (new-state))]

    (if (empty? code)
      state
      (case (first code)
        \[ (let [[code state] (eval-brackets code state)]
             (recur code state))
        \] (recur (rest code) state)
        (recur (rest code) (eval-char state (first code)))))))

(defn -main
  "Evals brainfuck code"
  [& args]
  (eval-brainfuck (parse-code (first args)))
  nil)
