(set-env!
  :source-paths #{"src/main/clj"}
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [proto-repl "0.1.2"]
                  [proto-repl-charts "0.2.0"]])

(println "welcome to boot")


(deftask dev
  "Profile setup for development.
  	Starting the repl with the dev profile...
  	boot dev repl "
  []
  (println "Dev profile running")
  (set-env!
   :init-ns 'user
   ;:source-paths #(into % ["dev" "test"])
   :source-paths #(into % ["dev"])
   :dependencies #(into % '[[org.clojure/tools.namespace "0.2.11"]]))

  ;; Makes clojure.tools.namespace.repl work per https://github.com/boot-clj/boot/wiki/Repl-reloading
  (require 'clojure.tools.namespace.repl)
  (eval '(apply clojure.tools.namespace.repl/set-refresh-dirs
                (get-env :directories)))

  identity)

(use '[pegthing.core])
; (clojure.tools.namespace.repl/refresh-dirs)
