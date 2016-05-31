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
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(map vector vamp-keys ["Edward Cullen" "10"])
(map vector [:name :glitter-index] ["Edward Cullen" "10" "dk"] ["dsf" :fun] {:test "val"})

(convert :name "Edward Cullen")
(assoc row-map [:name "Edward Cullen"] (convert :name "Edward Cullen"))

(first (mapify (parse (slurp filename))))
; => {:glitter-index 10, :name "Edward Cullen"}

(mapify vampStringAllSplit)
