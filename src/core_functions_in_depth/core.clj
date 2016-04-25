
(def human-consumption   [8.1 7.3 6.6 5.0]); creating a vector
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data ; convert the vector into a map.
  [human critter]
  {:human human
   :critter critter})

(unify-diet-data human-consumption critter-consumption)
(map unify-diet-data human-consumption critter-consumption)

;implement map using reduce
(map + [1 2 3] [1 2 3])
(map inc '(1 2 3))
(map + [1 2 3] '(1 2 3))
(fn [])
