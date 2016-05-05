(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(map titleize ["Hamsters" "Ragnarok"])
; => ("Hamsters for the Brave and True" "Ragnarok for the Brave and True")

(map titleize '("Empathy" "Decorating"))
; => ("Empathy for the Brave and True" "Decorating for the Brave and True")

(map titleize #{"Elbows" "Soap Carving"})
; => ("Elbows for the Brave and True" "Soap Carving for the Brave and True")
(map #(titleize %)(hash-set "kurt-vonnegut" 20 :icicle))
(map #(titleize (second %)) {:key "Winking" :fun "gil"})
; => ("Winking for the Brave and True")
( #( first %) {:key "Winking" :fun "gil"})
(titleize (#(second %) {:key "Winking" :fun "gil"}))

(map #(titleize (first %)) (hash-set "kurt-vonnegut" :20 :icicle))
(hash-set "kurt-vonnegut" 20 :icicle)
(first (hash-set "kurt-vonnegut" :20 :icicle))
#(titleize (first (hash-set "kurt-vonnegut" :20 :icicle)))
;------------------------------------------------------------------------
(def node3 {:value "last" :next nil})
(def node2 {:value "middle" :next node3})
(def node1 {:value "first" :next node2})

(first node2)



(def human-consumption   [8.1 7.3 6.6 5.0]); creating a vector
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data ; convert the vector into a map.
  [human critter]
  {:human human
   :critter critter})

(unify-diet-data human-consumption critter-consumption)
(map unify-diet-data human-consumption critter-consumption)


;-----------------------------------------------------------------------
(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(count [3 4 10])
(stats [3 4 10])
; Okay. So we've defined the anonymous function 'sum' which adds all the elements of the sequence by calling reduce with the add function and providing the collection
;average is also an anonymous function which uses the sum function previously defined. 'count' is an existing functing that returns the number of nodes in a collection.
; The stats function warps my brain a little bit. The 'function' used is an anonymous function that takes a function as an argument and has a collection to operate on. Then the collection this function operates on is a collection of functions. These values are then returned as a lazy sequence, which is what map always returns.
;-----------------------------------------------------------------------
(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Man" :real "Peter Parker"}
   {:alias "Santa" :real "Your mom"}
   {:alias "Easter Bunny" :real "Your dad"}])

(map :real identities)
; keys can be used as functions with maps if we're operation on a map data structure.
;-----------------------------------------------------------------------
;assoc
;reduce
;implement map using reduce
(map + [1 2 3] [1 2 3])
(map inc '(1 2 3))
(map + [1 2 3] '(1 2 3))
(inc '(1 2 3 4))


(seq
  (reduce
     (fn [newcoll node] (conj newcoll node))
     []
     [1 2 3 4]))

(defn mymap
  [f c1]
  (reduce
    (fn [nc item](conj nc (f item)))
    []
    c1))

(mymap inc [1 2 3 4])

;trying to fully implement map with reduce.
;(defn mymap
;  [f & c]
;  (reduce
;    (fn [nc it] (conj nc (f it)))
;    []
;    (reduce
;      (fn [rows col]
;        (reduce
;          (fn [rows it] (conj row it))
;          rows
;          col
;      [[][]]
;      c)
;---------------------------------------------------
;take
;drop
;take-while
;drop-while
;filter
;some
(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

(some
  #(and
    (> (:critter %) 3) ;1st value to and function. Is the value of :critter greater than 3?
    % food-journal)) ;If true then return %, which is still the same value since we are still in the scope of the anonymous function.
;--------------------------------------------------------
;sort and sort-by
;concat
;-------------------------------------------------------
;Lazy Seq

(def vampire-database
  {0 {:makes-blood-puns? false,}})
