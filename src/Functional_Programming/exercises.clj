;1. You used (comp :intelligence :attributes) to create a function that returns a character’s intelligence. Create a new function, attr, that you can call like (attr :intelligence) and that does the same thing.

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})
(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(c-int character)

(defn attr [key]
  (fn [character]
    (key (:attributes character))))

(def attr-int (attr :intelligence))
(attr-int character)

;2. Implement the comp function.
(defn my-comp
  [f]
  ;(fn [& coll] (apply f coll)))
  (fn [& coll] (reduce f coll)))

(def my-add
  (my-comp +))

(my-add 1 2 3 4)

;3. Implement the assoc-in function. Hint: use the assoc function and define its parameters as [m [k & ks] v].

;4. Look up and use the update-in function.

;5. Implement update-in.
