(ns twitter-hash-6d.core
  (:require [twitter-hash-6d.collector :as collector]))

(defn -main
  "Entry method, takes the term from the args and collects results"
  [hash-term]
  (println 
    (collector/collect hash-term)))
