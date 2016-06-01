(ns fwpd.core)
(def filename "fwpd/resources/suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(convert :glitter-index "3")

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(parse (slurp filename))

(def vampString(slurp filename))
(identity vampString)

(def vampStringSplitNewLine
  (clojure.string/split (slurp filename) #"\n"))

(identity vampStringSplitNewLine)

(first vampStringSplitNewLine)
(clojure.string/split (first p1) #",")

(def vampStringAllSplit
  (map #(clojure.string/split % #",") p1))

(identity vampStringAllSplit)
(first vampStringAllSplit)

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {};initial value for row-map?
                 (map vector vamp-keys unmapped-row)))
       rows))

;first map goes through each row of vampStringAllSplit
; first result is ["Edward Cullen" "10"]

;second map takes that vector and the vamp-keys vector and creates a new map using the vector function
(map vector vamp-keys ["Edward Cullen" "10"])
;>([:name "Edward Cullen"] [:glitter-index "10"])
(map vector [:name :glitter-index] ["Edward Cullen" "10" "dk"] ["dsf" :fun] {:test "val"}) ; example

;([:name "Edward Cullen"] [:glitter-index "10"]) is iterated over by reduce applyng the custom function which builds up the row-map. That function is passed first the empty map {} and the map [:name "Edward Cullen"]
;the convert function is called generating a value
(convert :name "Edward Cullen")
;>"Edward Cullen"
;then the assoc function is called associating that value with the :name and adding them to the empty map
(assoc {} :name (convert :name "Edward Cullen"))
;>{:name "Edward Cullen"}
;Then we do it again with [:glitter-index "10"]
(convert :glitter-index "10")
;> 10
(assoc {:name "Edward Cullen"} :glitter-index (convert :glitter-index "10"))
;=> {:name "Edward Cullen", :glitter-index 10}

;put it together
(reduce (fn [row-map [vamp-key value]]
          (assoc row-map vamp-key (convert vamp-key value)))
        {}
        (map vector vamp-keys ["Edward Cullen" "10"]))


(first (mapify (parse (slurp filename))))
; => {:name "Edward Cullen", :glitter-index 10}

(def vampMap
  (mapify vampStringAllSplit))
(identity vampMap)

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(def listofvamps
  (glitter-filter 3 (mapify (parse (slurp filename)))))
(identity listofvamps)

(:name (first listofvamps))

(defn glitter-filter2
  [minimum-glitter records]
  (map :name (filter #(>= (:glitter-index %) minimum-glitter) records)))

(glitter-filter2 3 (mapify (parse (slurp filename))))

;Write a function, validate, which will check that :name and :glitter-index are ;present when you append. The validate function should accept two arguments: a ;map of keywords to validating functions, similar to conversions, and the record ;to be validated.

(conj (mapify vampStringAllSplit) {:name "Todd" :glitter-index 7})

(defn append
  [suspectList newSuspect]
  (if (validate newSuspect)
    (conj suspectList newSuspect)
    ;(identity suspectList)))
    false))


(defn validate
  [newSuspect]
  (and (:name newSuspect) (:glitter-index newSuspect)))

;Write a function that will take your list of maps and convert it back to a CSV string. You’ll need to use the clojure.string/join function.

;"Edward Cullen,10\nBella Swan,0\nCharlie Swan,0\nJacob Black,3\nCarlisle Cullen,6\n"

;({:name "Edward Cullen", :glitter-index 10}
; {:name "Bella Swan", :glitter-index 0}
; {:name "Charlie Swan", :glitter-index 0}
; {:name "Jacob Black", :glitter-index 3}
; {:name "Carlisle Cullen", :glitter-index 6})

(clojure.string/join "," (map vals vampMap))

(vals (first vampMap))

(str (:name (first vampMap)) "," (:glitter-index (first vampMap)))

(#(str (:name %) "," (:glitter-index %)) (first vampMap))

(map #(str (:name %) "," (:glitter-index %) "\n") vampMap)

(clojure.string/join (map #(str (:name %) "," (:glitter-index %) "\n") vampMap))

(defn map2csv
  [vampMaps]
  (clojure.string/join (map #(str (:name %) "," (:glitter-index %) "\n") vampMap)))

;-----------Test functions--------------------------------------------
(append (mapify vampStringAllSplit) {:names "Todd" :glitter-index 7})
(validate {:name "Todd" :glitter-index 7})
(validate {:name "Todd"})
(validate {})

(map2csv vampMap)
