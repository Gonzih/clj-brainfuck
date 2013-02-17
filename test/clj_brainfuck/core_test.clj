(ns clj-brainfuck.core-test
  (:require [midje.sweet :refer :all]
            [clj-brainfuck.core :refer :all]))

(def dummy-state {:pointer 3
                  :cells [125, 110, 145, 68, 89]})

(fact "parse-code returns array of chars"
      (parse-code ".,.") => [\. \, \.])

(fact "Evaluation of > moves pointer forward"
  (let [{pointer :pointer} (eval-char dummy-state \>)]
    pointer => 4))

(fact "Evaluation of < character moves pointer back"
  (let [{pointer :pointer} (eval-char dummy-state \<)]
    pointer => 2))

(fact "Evaluation of + character increases current cell"
  (let [{[_ _ _ ch & _] :cells} (eval-char dummy-state \+)]
    ch => 69))

(fact "Evaluation of - character decreases current cell"
  (let [{[_ _ _ ch & _] :cells} (eval-char dummy-state \-)]
    ch => 67))

(fact "Evalluation of . character prints character from current cell"
  (eval-char dummy-state \.) => dummy-state
  (provided (print \D) => nil :times 1))

(fact "Evaluation of , character reads character to current cell"
  (-> (eval-char dummy-state \,)
      :cells
      (get 3)) => 56
  (provided (read-char) => 56 :times 1))

(fact "parse-brackets funtion returns code from inside 2 brackets"
  (let [code    (parse-code "[.>>.,]")
        in-code (parse-code ".>>.,"  )]
    ((parse-brackets code) 0) => in-code))

(fact "parse-brackets funtion returns remaining code after brackets"
  (let [code     (parse-code "[.>>.,]>>,.+-")
        out-code (parse-code ">>,.+-"  )]
    ((parse-brackets code) 1) => out-code))

(fact "cell-present return true if cell is non zero"
      (cell-present { :pointer 0 :cells [1] }) => true)

(fact "cell-present return false if cell is nil"
      (cell-present { :pointer 0 :cells [] }) => false)

(fact "cell-present return false if cell is zero"
      (cell-present { :pointer 0 :cells [0] }) => false)
