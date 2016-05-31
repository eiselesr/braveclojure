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

(mapify vampStringAllSplit)


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
