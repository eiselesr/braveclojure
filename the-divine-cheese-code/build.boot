(set-env!
  ;:resource-paths #{"src"} Don't need this... what it for?
  :source-paths #{"src"}
  :dependencies
    '[[org.clojure/clojure "1.8.0"]
      [proto-repl "0.1.2"]
      [proto-repl-charts "0.2.0"]
     ])

(require 'the-divine-cheese-code.core)
(deftask run []
  ;(set-env!
	;	:source-paths #(into % ["visualization"]))
  (with-pass-thru _
    (the-divine-cheese-code.core/-main)))


;How do I track the snapshot version?

(deftask dev
	"Profile setup for development.
	Starting the repl with the dev profile...
	boot dev repl "
	[]
	(set-env!
		:source-paths #(into % ["dev"])
		:dependencies #(conj % '[org.clojure/tools.namespace "0.2.11"]))
  ;; Makes clojure.tools.namespace.repl work per https://github.com/boot-clj/boot/wiki/Repl-reloading
  ;(require 'clojure.tools.namespace.repl)
  ;(eval '(apply clojure.tools.namespace.repl/set-refresh-dirs
  ;              (get-env :directories)))
	;identity;Why is this identity line here?
  )
