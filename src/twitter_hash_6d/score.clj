(ns twitter-hash-6d.score
  (:require [twitter-hash-6d.util :as util]))

(defn- sort-by-values 
  "sorts map by values. @https://clojuredocs.org/clojure.core/sorted-map-by#into"
  [unsorted-map]
  (into (sorted-map-by (fn [key1 key2]
                         (compare [(get unsorted-map key2) key2]
                                  [(get unsorted-map key1) key1])))
        unsorted-map))

(def scoring-chart (util/config :scoring-chart))

(defn score 
  "Given a map of results gruped by degrees, calculates scoring per [configurable] scoring chart.
   Values not in the scoring chart are filtered"
  [results-per-degrees]
  (sort-by-values
    (reduce #(merge-with + % %2)
            (for [degree (keys results-per-degrees)
                  :let [degree-val (scoring-chart degree)]
                  :when (not (nil? degree-val))]
              (reduce (fn [scoring-map item] 
                        (if (nil? (scoring-map item))
                          (assoc scoring-map item degree-val)
                          (update scoring-map item + degree-val)))
                      {} 
                      (results-per-degrees degree))))))