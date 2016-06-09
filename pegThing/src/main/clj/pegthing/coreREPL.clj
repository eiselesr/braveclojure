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
  "Generate a lazy sequence whose values are the ends of rows. Return the last one this is the max-pos in the board"
  [row]
  (last (take row tri)))

(row-tri 5)

(defn row-num
  "Returns which row a particular peg is in"
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
(assoc-in users [1 :password] "njho")

(def assocTest (#(assoc-in %1 [1] {:thing "thing 1"}) [1 4]))
(identity assocTest)
(assoc-in assocTest [1] {:thing 7})

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
        dst (+ 1 row neighbor 1)]
    (connect board max-pos pos neighbor dst)))

(row-num 6)
(+ 3 6 1)
(+ 1 3 10 1)

(def tempBoard {:rows 5})
(connect-down-right tempBoard 15 6)
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

    ;-----------------------------------------------------------------------------
(identity my-board)
(type my-board)
(map #(get % 0) my-board) ;(7 1 4 15 13 :rows 6 3 12 2 11 9 5 14 10 8)
(map #(get % 1) my-board)
    ;({:connections {1 3, 2 4, 9 8}, :pegged true}
    ; {:connections {4 2, 7 3}, :pegged true}
    ; {:connections {1 2, 6 5, 11 7}, :pegged false}
    ; {:connections {13 14}, :pegged true}
    ; {:connections {3 6, 6 9, 11 12, 15 14}, :pegged true}
    ; 5
    ; {:connections {4 5, 13 9}, :pegged true}
    ; {:connections {8 5, 13 6}, :pegged true}
    ; {:connections {5 8, 14 13}, :pegged true}
    ; {:connections {7 4, 11 5}, :pegged true}
    ; {:connections {2 5, 4 7, 13 12}, :pegged true}
    ; {:connections {7 8}, :pegged true}
    ; {:connections {12 8}, :pegged true}
    ; {:connections {12 13}, :pegged true}
    ; {:connections {8 9}, :pegged true}
    ; {:connections {3 5, 10 9}, :pegged true})
(map #(get-in % [1 :pegged]) my-board)
(map #(get (get % 1) :pegged) my-board)
(map #(get (second %) :pegged) my-board)
(filter #(get (second %) :pegged) my-board)
(type (filter #(get (second %) :pegged) my-board))
(map first (filter #(get (second %) :pegged) my-board))
(def pos-with-pegs (map first (filter #(get-in % [1 :pegged]) my-board)))
(def temp (partial valid-moves my-board))
(def moveable-pegs (map temp pos-with-pegs))
(map not-empty moveable-pegs)
(some not-empty moveable-pegs)

(defn can-move?
  "Are there any pegs that have legal moves?"
  [board]
  (some (comp not-empty (partial valid-moves board))
    (map first (filter #(get-in % [1 :pegged]) board))))

    ;------------------------------------------------------------------------------

(def alpha-start 97)
(def alpha-end 123)
(def letters (map (comp str char) (range alpha-start alpha-end)))
(type letters)
(identity letters)

(def pos-chars 3)

(def ansi-styles
  {:red   "[31m"
   :green "[32m"
   :blue  "[34m"
   :reset "[0m"})

(defn ansi
  "Produce a string which will apply an ansi style"
  [style]
  (str \u001b (style ansi-styles)))

(str (str \u001b "[31m") "text" (str \u001b "[0m"))

(defn colorize
  "Apply ansi color to text"
  [text color]
  (str (ansi color) text (ansi :reset)))

(colorize "test" :blue)

(nth letters (dec 1))

(defn render-pos
  [board pos]
  (str (nth letters (dec pos))
       (if (get-in board [pos :pegged])
         (str "0")
         (str "-"))))

(defn row-positions
  "Return all positions in the given row"
  [row-num]
  (range (inc (or (row-tri (dec row-num)) 0))
         (inc (row-tri row-num))))

(defn row-padding
  "String of spaces to add to the beginning of a row to center it"
  [row-num rows]
  (let [pad-length (/ (* (- rows row-num) pos-chars) 2)]
    (apply str (take pad-length (repeat " ")))))

(defn render-row
  [board row-num]
  (str (row-padding row-num (:rows board))
       (clojure.string/join " " (map (partial render-pos board)
                                     (row-positions row-num)))))

    ;-------------------------------------------------------------------------------
(defn print-board
        [board]
        (doseq [row-num (range 1 (inc (:rows board)))]
               (println (render-row board row-num))))

(print-board my-board)


;(int "a"); no good
(first "a"); =>"a"
(int (first "a")); => 97, that works
(identity "a"); =>"a"
;(int (identity "a")); => ClassCastException java.lang.String cannot be cast to java.lang.Character  clojure.lang.RT.intCast. Seems like it should work, but no good
(int (last "a")); => 97, that works
(seq "a");=> ("a")
(sequence "a");=> ("a")
(get "a" 0)
(int (get "a" 0)); => 97, that works
(type (get "a" 0)); => java.lang.Character
(apply val {:key "a"});=> "a"
(type (apply val {:key "a"}));=> java.lang.String
;(int (apply val {:key "a"})); ClassCastException [trace missing]
;(unchecked-char "a")
(type (first "abc"))
(type (seq "abc"))
(first '("abc" "123"))
(type (first '("abc" "123")))
(map first '("abc" "123"))
(type (first (map first '("abc" "123"))))
(identity \a);=> "a"
(type \a);=> java.lang.Character

(defn letter->pos
  "Converts a letter string to the corresponding position number"
  [letter]
  (inc ;since we started the positions at 1, add 1 to the difference
    (- ;subtract the numeric value of a from the current letter's numberic value
      (int (first letter)) ;turn the string into a char, then an int
      alpha-start)))

(letter->pos "a")

(defn get-input
  "mulitple arity function that takes user input and returns the default provided when the function is called or the input of the user."
  ([] (get-input nil))
  ([default]
   (let [input (clojure.string/trim (read-line))]
    (if (empty? input)
      default
      (clojure.string/lower-case input)))))

(defn characters-as-strings
  [string]
  (filter #(not= " " %)
    (clojure.string/split string #"")))

(re-seq #"[a-zA-Z]" "a    bc")
(defn characters-as-strings-book
  [string]
  (re-seq #"[a-zA-Z]" string))

(characters-as-strings "a   bc")
(characters-as-strings-book "a    bc")

(defn user-entered-invalid-move
  "Handles when a user enters an invalid move"
  [board]
  (println "\n!!! That was an invalid move :( \n")
  (prompt-move board))

(defn user-entered-valid-move
  "Handles the step after a user has entered a valid move"
  [board]
  (if (can-move? board)
    (prompt-move board)
    (game-over board)))

(defn prompt-move
  [board]
  (println "\nHere's your board:")
  (print-board board)
  (println "Move from where to where? Enter two letters:")
  (let [input (map letter->pos (characters-as-strings (get-input)))]
    (if-let [new-board (make-move board (first input) (second input))]
      (user-entered-valid-move new-board)
      (user-entered-invalid-move board))))

(defn prompt-empty-peg
  [board]
  (println "Here's your board:")
  (print-board board)
  (println "Remove which peg? [e]")
  (prompt-move (remove-peg board (letter->pos (get-input "e")))))

(defn prompt-rows
  []
  (println "How many rows? [5]")
  (let [rows (Integer. (get-input 5))
        board (new-board rows)]
    (prompt-empty-peg board)))

(defn game-over
  "Announce the game is over and prompt to play again"
  [board]
  (let [remaining-pegs (count (filter :pegged (vals board)))]
    (println "Game over! You had" remaining-pegs "pegs left:")
    (print-board board)
    (println "Play again? y/n [y]")
    (let [input (get-input "y")]
      (if (= "y" input)
        (prompt-rows)
        (do
          (println "Bye!")
          (System/exit 0))))))


(def this-board (new-board 5))
(:rows this-board)
(def this-board1(remove-peg this-board (letter->pos "c")))
(identity this-board1)
(print-board this-board1)
(make-move this-board1 (letter->pos "j") (letter->pos "c"))

(valid-moves this-board1 3)



(comment
 "HAHAHA... no comment")
