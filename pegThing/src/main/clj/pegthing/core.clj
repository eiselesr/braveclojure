(ns pegthing.core
  (require [clojure.set :as set])
  (:gen-class))

(declare successful-move prompt-move game-over query-rows)

;----What goes into the core.clj file?-------
(defn tri*
  "Generates lazy sequence of triangular numbers"
  ([] (tri* 0 1))
  ([sum n]
   (let [new-sum (+ sum n)]
       (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

(def tri (tri*))

(take 5 tri)

(defn triangular?
  [arg]
  (= arg (last(take-while #(<= % arg) tri))))

(triangular? 6)

(defn row-tri
  [row]
  (last (take row tri)))

(row-tri 3)

(defn row-num
  [hole]
  (inc (count (take-while #(> hole %) tri))))

(row-num 7)

(defn connect)
