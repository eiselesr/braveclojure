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

(apply add-inc '(1 2 3 4 ))

(defn myComp
  [f & fns]
  (if (empty? fns)
    f
    (recur (fn [& args]
            (f (apply (first fns) args)))
           (rest fns))))

(def myCompfunc
  (myComp inc inc inc))

(map myCompfunc [1 2])

(def testcomp
  (comp inc + -))

(testcomp 1)

;--------------------------------------------------
;memoize
;; Fibonacci number with recursion.
(defn fib [n]
  (condp = n
    0 1
    1 1
    (+ (fib (dec n)) (fib (- n 2)))))

(time (fib 30))
;; "Elapsed time: 8179.04028 msecs"

;; Fibonacci number with recursion and memoize.
(def m-fib
  (memoize (fn [n]
             (condp = n
               0 1
               1 1
               (+ (m-fib (dec n)) (m-fib (- n 2)))))))

(time (m-fib 90))
;; "Elapsed time: 1.282557 msecs"


(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)
(time
  (sleepy-identity "Mr. Fantastico"))
; => "Mr. Fantastico" after 1 second

(time
  (sleepy-identity "Mr. Fantastico"))
; => "Mr. Fantastico" after 1 second

(def memo-sleepy-identity (memoize sleepy-identity))
(time
  (memo-sleepy-identity "Mr. Fantastico"))
; => "Mr. Fantastico" after 1 second

(time
  (memo-sleepy-identity "Mr. Fantastico"))
; => "Mr. Fantastico" immediately
