(ns twitter-hash-6d.collector
  (:require [twitter-hash-6d.search :as search]
            [clojure.string :as string]
            [twitter-hash-6d.util :as util]
            [clojure.set :as set]))

(defn get-new-hashes 
  "Accepts a collection of new terms and a map of old terms grouped by degrees. Returns a set of all items from the new collection
   not contained in the set of old terms"
  [new-terms old-terms]
    (set/difference new-terms (reduce set/union (vals old-terms))))

(defn do-search 
  "Takes hash term, converts it to lower case and calls the search for [configurable] no of tweets"
  [hash-term]
    (search/do-twitter-search-with-paging 
                                      (string/lower-case hash-term) 
                                      (util/config :tweets-per-search) 
                                      nil))

(defn search-and-append-results 
  "Accepts a hash term to search for, current connections degree and results of previous searches.
   Returns results appended by the results of the current searches. Results are in a form of a map, with three keys:
   1) :assoc-hashes - values is a map, where keys are all the terms searched so far, and a values are associate hashes
   2) :degrees - value is a map, where keys are connection degrees, and values are collections of search results in that degree
   3) :unique-hashes - value is a map, where keys are connection degrees, and values are sets of hashes that first appeared in that degree"  
  [hash-term degree results]
  (let [this-term-results (do-search hash-term)
        updated-hashes (assoc-in results 
                                 [:assoc-hashes hash-term] 
                                 this-term-results)
        updated-degrees (update-in updated-hashes 
                                   [:degrees degree] 
                                   concat
                                   this-term-results)
        updated-unique-hashes (update-in updated-degrees 
                                         [:unique-hashes degree] 
                                         set/union 
                                         (get-new-hashes (set this-term-results) (results :unique-hashes)))]
    updated-unique-hashes))

(defn- add-degree 
  "Takes two arguments, current results and next degree. Takes unique hashes from the previous degree and performs searches on them,
    returning aggregate results"
  [results degree]
    (reduce #(search-and-append-results %2 degree %) 
          results 
          (get-in results [:unique-hashes (dec degree)])))

(defn collect
  "Initialized results map and adds degrees one by one. Returns complete map"
  [hash-term]
    (reduce 
      add-degree 
      {:degrees {0 (list hash-term)} :unique-hashes {0 #{hash-term}}} ; initial results
      (range 1 (inc (util/config :degrees-deep)))))