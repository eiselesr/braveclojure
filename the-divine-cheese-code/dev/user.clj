(ns user
  (:require [clojure.tools.namespace.repl :as tnr]
            [prc]
            [proto]))


(defn start
 [] (println "Starting"))


(defn reset []
  (tnr/refresh :after 'user/start)
  (in-ns 'the-divine-cheese-code.core))

(println "proto-repl-demo dev/user.clj loaded.")

;(in-ns 'the-divine-cheese-code.core)
