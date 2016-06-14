(ns user
  (:require [clojure.tools.namespace.repl :as tnr]
            [prc]
            [proto]))


(defn start
 [] (println "Starting"))


(defn reset []
  (tnr/refresh :after 'user/start)
  (in-ns 'pegthing.core))

(println "proto-repl-demo dev/user.clj loaded.")
