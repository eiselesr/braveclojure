1 ;run the first ctrl + , and s after highlighting the 1
(str "a string")
(+ 1 2 3)
["a" "vector" "of" "strings"]

;do: is used in conjuction with a conditonal, like if. It is useful because it allows multiple things to happen if the condtion is met.

;when I don't really understand the distiction between if and when.
;It seems like either could be used anytime. When just doesn't have a alternate
;response

(nil? nil)

; all the objects in the parenthesis are being compared by the or operator
; the first true one is returned or the last value.
(or false nil :first-truthy-value)
(and nil false :first-truthy-value)


; I don't think this works...
(def severity :mild)
(def error-message "OH GOD! IT'S A DISASTER! WE'RE ")
(if (= severity :mild)
  (def error-message (str error-message "MILDLY INCONVENIENCED!"))
  (def error-message (str error-message "DOOOOOOOMED!")))
; So, contrary to my original supposition, the definition of error-message can
; indeed be over-written so this does work.

(def keys {"string-key" + :subtract -})

((get keys "string-key") 1 2)

((keys "string-key") 1 2)

(("string-key" keys) 1 2) ;this is not valid
((keys "string-key") 1 2) ; this works.
((:subtract keys) 1 2) ;key as function map as argument. Book recommends this way.
((keys :subtract) 1 2) ;map is function, key is argument


;--------------VECTORS-------------------------
(defn announce-treasure-location
  [{:keys [lat lng]}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))

(announce-treasure-location {:lat 28.22 :lng 81.33})

(map (fn [name] (str "Hi, " name))
  ["Darth Vader" "Mr. Magoo"]) ; the fuction has ariety 1 and prints a string with
; hi and the passed argument. Map applies this function to each member of the collection.

; would it work on an map? Looks like it. The " character is escaped with \
(map (fn [name] (str "Hi, " name))
  {:name1 "Darth Vader" :name2 "Mr. Magoo"})


(let [x 3]
  x)

(def dalmatian-list
  ["Pongo" "Perdita" "Puppy 1" "Puppy 2"])
(let [dalmatians (take 2 dalmatian-list)]
  dalmatians)

(let [[sam & dalmatians] dalmatian-list] ; destructure the dalmatian-list into the first one and the rest
  [sam dalmatians])

(re-find #"^left-" "left-eye")
(re-find #"^left-" "cleft-chin")
(re-find #"^left-" "wonglblart")


(defn my-reduce
  ([f initial coll]
   (loop [result initial
          remaining coll]
     (if (empty? remaining)
       result
       (recur (f result (first remaining)) (rest remaining)))))
  ([f [head & tail]]
   (my-reduce f head tail)))


;---------------------------------------------
;-----------------------Exercises
;---------------------------------------------
(defn add100 [number]
  (+ number 100))
;--------------------------------------------
(defn dec-maker
  [dec-by]
  #(- % dec-by))

(defn dec-maker2
  [dec-by]
  (fn [num] (- num dec-by)))

(def dec3 (dec-maker2 3))
(dec3 7)
;--------------------------------------------
(map inc [1 1 2 2])
(hash-set 1 1 2 2)
(get [1 1 2 2])

(defn mapset
  [f collection]
  (set (map f collection)))

(mapset inc [1 3 2 2]) ; I guess this is an acceptable solution assuming order doesn't matter.

;ANOTHER mapset
(defn mapset2
  [f collection]
  (into #{} (map f collection)))

(mapset2 inc [1 1 2 2])
;---------------------------
;Create a function that’s similar to symmetrize-body-parts except that it has to work with weird space aliens with radial symmetry. Instead of two eyes, arms, legs, and so on, they have five.

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  ;      local variable       value           local variable  value
  (loop [remaining-asym-parts asym-body-parts final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
          (into final-body-parts
            (set [part (matching-part part)])))))))
