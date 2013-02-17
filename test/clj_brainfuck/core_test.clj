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

(fact "Evaluation of + character increases cell"
  (let [{[_ _ _ ch & _] :cells} (eval-char dummy-state \+)]
    ch => 69))

(fact "Evaluation of - character decreases"
  (let [{[_ _ _ ch & _] :cells} (eval-char dummy-state \-)]
    ch => 67))

(fact "Evalluation of . character"
  (eval-char dummy-state \.) => dummy-state
  (provided (print \D) => nil :times 1))

(fact "Evaluation of , character"
  (-> (eval-char dummy-state \,)
      :cells
      (get 3)) => 56
  (provided (read-char) => 56 :times 1))
