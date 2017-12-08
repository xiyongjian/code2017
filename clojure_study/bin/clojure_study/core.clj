(ns clojure-study.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World xxx!"))


(defmacro inc10 [n] (list `+ 10 n))

(defmacro inc102 [n] `(+ 10 ~n))

(macroexpand '(inc10 5))

(macroexpand '(inc102 5))

(println "inc")

