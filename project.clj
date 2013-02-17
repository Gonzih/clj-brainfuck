(defproject clj-brainfuck "0.1.0-SNAPSHOT"
  :description "Brainfuck in Clojure"
  :url "https://github.com/Gonzih/clj-brainfuck"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[midje "1.4.0"]]
                   :plugins [[lein-midje "2.0.1"]]}}
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :main clj-brainfuck.core)
