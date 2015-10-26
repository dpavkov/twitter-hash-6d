(ns twitter-hash-6d.core
  (:require [twitter-hash-6d.collector :as collector]
            [twitter-hash-6d.score :as score]))

(defn -main
  "Entry method, takes the term from the args, prints the scores"
  [hash-term]
  (println 
    (dissoc ; removes searching term
      (score/score 
        ((collector/collect hash-term) :degrees))
            hash-term)))
