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
