(ns the-divine-cheese-code.visualization.svg
  (:require [clojure.string :as s])
  (:refer-clojure :exclude [min max]))

(defn myp
  [arg]
  ;(println "\n" arg "\n")
  (identity arg))

(defn comparator-over-maps
  [comparison-fn ks]
  (fn [maps]
    (zipmap ks
       (map
         (fn [k]
           (apply comparison-fn (myp (map (myp k) (myp maps)))))
         ks))))

(def min (comparator-over-maps clojure.core/min [:lat :lng]))
(def max (comparator-over-maps clojure.core/max [:lat :lng]))

(comment
 "Hahahah no comment")
;------EXAMPLE
;(min [{:a 1 :b 3} {:a 5 :b 0}])
(apply clojure.core/max (map :a [{:a 7 :b 3} {:a 5 :b 0}]))
(map (fn [k]
      (apply clojure.core/min
       (map k [{:a 1 :b 3} {:a 5 :b 0}])))
     [:a :b])

(zipmap [:a :b] '(1 0))

(min [{:lat 1 :lng 3} {:lat 5 :lng 0}])
;------END EXAMPLE

(defn translate-to-00
  [locations]
  (let [mincoords (min locations)]
    (map #(merge-with - % mincoords) locations)))

;example (min heists)
;=> {:lat 41.9, :lng 5.37}

(defn scale
 [width height locations]
 (let [maxcoords (max locations)
       ratio {:lat (/ height (:lat maxcoords))
              :lng (/ width (:lng maxcoords))}]
   (map #(merge-with * % ratio) locations)))

(defn latlng->point
  "Convert lat/lan map to comma-separated string"
  [latlng]
  (str (:lat latlng) "," (:lng latlng)))
  ;(latlng->point (first heists))
  ;=>"50.95,6.97"

(defn points
  "Given a seq of lat/lng maps, return string of points joined by space"
  [locations]
  (s/join " " (map latlng->point locations)))
  ;(points heists)
  ;=> "50.95,6.97 47.37,8.55 43.3,5.37 47.37,8.55 41.9,12.45")

(defn line
  [points]
  (str "<polyline points=\"" points "\" />"));the backslashes are excape
;(line (points heists))
;=>  "<polyline points=\"50.95,6.97 47.37,8.55 43.3,5.37 47.37,8.55 41.9,12.45\" />"characters on the quote marks around the points)

(defn transform
  "chains other functions"
  [width height locations]
  (->> locations
       translate-to-00
       (scale width height)))

(defn xml
  "svg template which also flips the coordinate system"
  [width height locations]
  (str "<svg height=\"" height "\"width=\"" width "\">"
    ;; These two <g> tags change the coordinate system so that
    ;; 0,0 is in the lower-left corner, instead of SVG's default
    ;; upper-left corner
    "<g transform=\"translate(0," height ")\">"
    "<g transform=\"rotate(-90)\">"
    (-> (transform width height locations)
        (points)
        (line))
    "</g></g>"
    "</svg>"))
