(ns twitter-hash-6d.search
  (:require [twitter.api.restful :as twitter-api]
            [twitter-hash-6d.util :as util]
            [clojure.string :as string]))


(defn extract-hashes 
  "entering search results as provided by twitter: {... :body {:statuses [{:hashes [{:text ...}] 
   ...}...]} ...} returns a map where key is a hash-term provided, and values are all other hashes
   from the tweets provided"
  [twitter-search-results hash-term]
  
  (letfn [(lower-from-hashtag [hashtag]
                              (string/lower-case (hashtag :text)))
          (extract-hashes-from-tweet [acc tweet]
                                     (concat acc 
                                             (map lower-from-hashtag
                                                  (filter #(not (= (lower-from-hashtag %) hash-term)) 
                                                          (get-in tweet [:entities :hashtags])))))]
    {hash-term 
     (reduce extract-hashes-from-tweet []  (get-in twitter-search-results [:body :statuses]))}))


(defn do-twitter-search 
  "Performs the search on hash-term. Returns the result modified by extract-hashes"
  [hash-term]
  (extract-hashes
    (twitter-api/search-tweets :oauth-creds util/my-creds 
                               :params 
                               {:q (str "#" hash-term) :count (util/config :tweets-per-search)})
    hash-term))
