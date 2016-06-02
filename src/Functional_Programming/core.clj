(defn sum
  ([vals] (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
    accumulating-total
    (sum (rest vals) (+ (first vals) accumulating-total)))))

(sum [39 5 1])

(defn sum
  ([vals] (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
    accumulating-total
    (recur (rest vals) (+ (first vals) accumulating-total)))))

; Function that creates a function that applies funtiong g then function f.
(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))

(def add-inc (two-comp inc +))

(apply add-inc '(1 2 3 4 5))

(defn myComp
  [& fns]
  (fn [& args]
    ()))

(myComp inc + -)
