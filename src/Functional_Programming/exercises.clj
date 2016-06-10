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
  [f & gs]
  (if (empty? gs)
    f
    (recur
      (fn [& coll] (f (apply (first gs) coll))) ;f
      (rest gs)))) ;&gs
  ;(fn [& coll] (reduce f coll)))

(defn counter []
  (let [tick (atom 0)]
    #(swap! tick inc)))

(def my-add
  (my-comp inc inc inc))

(my-add 1)

;3. Implement the assoc-in function. Hint: use the assoc function and define its parameters as [m [k & ks] v].

(def users
  [{:name "James" :age 26 :Hobbies {:sport "swim" :leisure "read"}}
   {:num 5 :name "John" :stats {:age 43 :height {:in 100 :cm 254}}}])

;; update the age of the second (index 1) user
(assoc-in users [1 :age] 44)
(assoc (get-in users [0 :Hobbies]) :sport "run")
(assoc (get-in users [0]) :Hobbies (assoc (get-in users [0 :Hobbies]) :sport "run"))
;[users [1 '(:Hobbies :fun)] {:sport "run"}]
;(assoc users 1 (my-assoc-in (get users 1) '(:Hobbies :fun) {:sport "run"}))
(get users 1);> {:age 43, :name "John"}
;(assoc {:age 43, :name "John"} :Hobbies (my-assoc-in (get {:age 43, :name "John"} :Hobbies) '(:fun) {:sport "run"}))
(get {:age 43, :name "John"} :Hobbies) ;>nil
;(assoc nil :fun (my-assoc-in (get nil :fun) '() {:sport "run"}))
(get nil :fun) ;>nil
(assoc nil :fun {:sport "run"});>{:fun {:sport "run"}}
(assoc {:age 43 :name "John"} :Hobbies {:fun {:sport "run"}})
;>{:Hobbies {:fun {:sport "run"}}, :age 43, :name "John"}
(assoc users 1 {:Hobbies {:fun {:sport "run"}}, :age 43, :name "John"})

(assoc users 1 (assoc {:age 43 :name "John"} :Hobbies (assoc nil :fun {:sport "run"})))


(defn my-assoc-in
  [m [k & ks] v]
  (if ks
    (assoc m k (my-assoc-in (get m k) ks v))
    (assoc m k v)))

(my-assoc-in users [1 :Hobbies :fun] {:sport "run"})

;4. Look up and use the update-in function.

(get-in users [1 :stats :height :in])
(update-in users [1 :stats :height :in] #(+ 5 %))

;5. Implement update-in.

(defn myp
  [arg]
  (println "\n" arg " "(type arg)"\n")
  (identity arg))

(myp users)

(defn my-update-in
  ([m ks f & args]
   (let [up (fn up [m ks f args]
              (let [[k & ks] ks]
                (if ks
                  (assoc m k (up (get m k) ks f (myp args)))
                  (assoc m k (apply f (get m k) (myp args))))))]
        (up m ks f args))))

(my-update-in users [1 :stats :height :in] + 100 5)
(my-update-in users [1 :num] + 1 2 3)
(my-update-in users [1 :num] #(+ 5 %))


(assoc users 1
  (assoc {:age 48 :name "John"} :age
    (apply #(+ 5 %) (get {:age 48 :name "John"} :age) '())))

;Why did we use let?
; we use it because since we pass args back to the function and if the & is present then args is wrapped in another set of parenthesis.
(defn up [m ks f & args]
  (let [[k & ks] ks]
    (if ks
      (assoc m k (up (get m k) ks (myp f) (myp args)))
      (assoc m k (apply (myp f) (myp (get m k)) (myp args))))))

(up users [1 :num] + 1 2 3)
