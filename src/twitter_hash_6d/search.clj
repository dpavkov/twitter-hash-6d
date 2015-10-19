(ns twitter-hash-6d.search
  (:require [twitter.api.restful :as twitter-api]
            [twitter-hash-6d.util :as util]
            [clojure.string :as string]))


(defn extract-hashes 
  "entering search results as provided by twitter: {... :body {:statuses [{:hashes [{:text ...}] 
   ...}...]} ...} returns a list of are all other hashes
   from the tweets provided"
  [twitter-search-results hash-term]
  
  (letfn [(lower-from-hashtag [hashtag]
                              (string/lower-case (hashtag :text)))
          (extract-hashes-from-tweet [acc tweet]
                                     (concat acc 
                                             (map lower-from-hashtag
                                                  (filter #(not (= (lower-from-hashtag %) hash-term)) 
                                                          (get-in tweet [:entities :hashtags])))))]
    (reduce extract-hashes-from-tweet []  (get-in twitter-search-results [:body :statuses]))))


(defn tweets-this-turn 
  "Given how many tweets has to be extracted for the search, determines how many tweets should be  taken in one turn"
  [tweets-to-go]
  (cond (> tweets-to-go 100) 100
    (<= tweets-to-go 0) 0
    :else tweets-to-go))

(defn do-twitter-search [hash-term tweets-count]
  (extract-hashes
    (twitter-api/search-tweets :oauth-creds util/my-creds 
                               :params 
                               {:q (str "#" hash-term) :count tweets-count})
    hash-term))

(defn do-twitter-search-with-paging 
  "Performs the search on hash-term. Returns the result modified by extract-hashes"
  [hash-term tweets-to-search]
  (if (> tweets-to-search 0) 
    (let [tweets-count (tweets-this-turn tweets-to-search)
          current-result (do-twitter-search hash-term tweets-count)
          tweets-left (- tweets-to-search tweets-count)]     
      (if (> tweets-left 0)
        (concat current-result  (do-twitter-search-with-paging hash-term tweets-left))
        current-result))
    '()))
