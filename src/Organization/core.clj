(ns-interns *ns*)

;-These need to be run for everything else to work.
(in-ns 'cheese.taxonomy)
(def cheddars ["mild" "medium" "strong" "sharp" "extra sharp"])
(def bries ["Wisconsin" "Somerset" "Brie de Meaux" "Brie de Melun"])
(in-ns 'cheese.analysis)
(clojure.core/refer 'cheese.taxonomy)
(clojure.core/refer-clojure)
;---------------------------------------------------------------------
;-----------------------------------------------------------------------
(defn- private-function
  "Example of a private function. It does nothing"
  [])

(in-ns 'cheese.taxonomy)
(clojure.core/refer-clojure)
(cheese.analysis/private-function)
(refer 'cheese.analysis :only ['private-function])
;-----------------------------------------------------------------------
; alias
;-----------------------------------------------------------------
(alias 'taxonomy 'cheese.taxonomy)
(identity taxonomy/bries)
