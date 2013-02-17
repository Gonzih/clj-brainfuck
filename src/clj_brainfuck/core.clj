(ns clj-brainfuck.core)

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

(defn eval-brackets
  "Returns vector of remaining code and state"
  [code state]
  (let [code (rest code)
        pred (partial not= \])
        in-code  (take-while pred code)
        out-code (drop-while pred code)
        result (loop [{:keys [pointer cells] :as state} state]
                 (if (> (cells pointer) 0)
                   (recur (eval-brainfuck in-code state))
                   state))]
    [out-code result]))

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
  (eval-brainfuck (map char (first args)))
  nil)
