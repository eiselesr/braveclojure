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
;------------------------------------------------------------------------------
(defn connect
  "Form a mutual connection between two positions"
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))

            board ;initial value for reduce

            [[pos destination] [destination pos]]) ;thing reduced over.
    board)) ;return the current board if an illegal connection is requested.

(connect {} 15 1 2 4)
; => {1 {:connections {4 2}} 4 {:connections {1 2}}

(assoc-in {} [1 :connections 4] 2)
(#(assoc-in %1 [(first %2) :connections (last %2)] 2) {} [1 4])
(#(assoc-in %1 [%2[0] :connections %2[1]] 2) {} [1 4])

(def avector [1 4])
(nth avector 1)
(get avector 0)

(def users [{:name "James" :age 26}  {:name "John" :age 43}])
;; update the age of the second (index 1) user
(assoc-in users [1] 44)

(#(assoc-in %1[0])) [1 4]

(reduce
 (fn [new-board [p1 p2]]
     (assoc-in new-board [p1 :connections p2] 2))
 {}
 [[1 4] [4 1]])

(reduce
 #(assoc-in %1 [(first %2) :connections (last %2)] 2)
 {}
 [[1 4] [4 1]])


(reduce
  #(assoc-in %1 [(get %2 0) :connections (get %2 1)] 2)
  {}
  [[1 4] [4 1]])
;------------------------------------------------------------------------------

(defn connect-right
  [board max-pos pos]
  (let [neighbor (inc pos)
        dst (inc neighbor)]
    (if-not (or (triangular? pos) (triangular? neighbor))
      (connect board max-pos pos neighbor dst) ;not true
      board))) ;else

(defn connect-down-left
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ row pos)
        dst (+ 1 row neighbor)];the neighbor is 1 row below the current pos.
    (connect board max-pos pos neighbor dst)))

(defn connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ row pos 1)
        dst (+ 1 row pos neighbor 1)]
    (connect board max-pos pos neighbor dst)))
;----------------------------------------------------------------------------
(assoc-in {} [1 :pegged] true)
(def users [{:name "James" :age 26}  {:name "John" :age 43}])
( assoc-in users [1 :pegged] true)
;;=> [{:name "James", :age 26} {:name "John", :age 44}]

(defn add-pos
  "Pegs the position and adds the connection"
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce
      (fn [new-board connection-creation-fn]
        (connection-creation-fn new-board max-pos pos))
      pegged-board
      [connect-right connect-down-left connect-down-right])))

(add-pos {:rows 5} 15 1)
;-----------------------------------------------------------------------------

(defn new-board
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce
      (fn [board pos] (add-pos board max-pos pos))
      initial-board
      (range 1 (inc max-pos)))))
;-----------------------------------------------------------------------------

(defn pegged?
  "Does the position have a peg in it?"
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  "Take the peg at given position out of the board"
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  "Add a peg at the given postion"
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  "move a peg from one position to another"
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))

;------------------------------------------------------------------------------
(defn valid-moves
  [board pos]
  (into {}
    (filter (fn [[dst jumped]]
              (and (pegged? board jumped)
                (not (pegged? board dst))))
      (get-in board [pos :connections]))))

(def my-board (assoc-in (new-board 5) [4 :pegged] false))

(valid-moves my-board 1)  ; => {4 2}
(valid-moves my-board 6)  ; => {4 5}
(valid-moves my-board 11) ; => {4 7}
(valid-moves my-board 5)  ; => {}
(valid-moves my-board 8)  ; => {}

;------------------------------------------------------------------------------

(defn valid-move?
  "Return jumped position if the move p1 to p2 is valid. nil otherwise"
  [board p1 p2]
  (get (valid-moves board p1) p2))

(valid-move? my-board 8 4) ; => nil
(valid-move? my-board 1 4) ; => 2
;-----------------------------------------------------------------------------

(defn make-move
  [board p1 p2]
  (if-let [jumped (valid-move? board p1 p2)]
    (move-peg (remove-peg board jumped) p1 p2)))
