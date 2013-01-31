(ns brainfuck-clojure.core-test
  (:require [clojure.test :refer :all]
            [brainfuck-clojure.core :refer :all]
            [echo.test.mock :refer :all]))

(def dummy-state {:pointer 3
                  :cells [125, 110, 145, 68, 89]})

(deftest eval-char-fn
  (testing "Eval of > character"
    (let [{pointer :pointer} (eval-char dummy-state \>)]
      (is (= pointer 4))))

  (testing "Eval of < character"
    (let [{pointer :pointer} (eval-char dummy-state \<)]
      (is (= pointer 2))))

  (testing "Eval of + character"
    (let [{[_ _ _ ch & _] :cells} (eval-char dummy-state \+)]
      (is (= ch 69))))

  (testing "Eval of - character"
    (let [{[_ _ _ ch & _] :cells} (eval-char dummy-state \-)]
      (is (= ch 67))))

  (testing "Eval of . character"
    (expect [print (->>
                     (has-args [\D])
                     (times once))]
            (is (= dummy-state (eval-char dummy-state \.)))))

  (testing "Eval of , character"
    (expect [read-char (->>
                     (times once)
                     (returns 56))]
            (let [{[_ _ _ ch & _] :cells}
                  (eval-char dummy-state \,)]
              (is (= ch 56))))))
