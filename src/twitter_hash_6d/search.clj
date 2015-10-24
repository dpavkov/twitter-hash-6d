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
    (reduce extract-hashes-from-tweet []  twitter-search-results)))


(defn tweets-this-turn 
  "Given how many tweets has to be extracted for the search, determines how many tweets should be  taken in one turn"
  [tweets-to-go]
  (cond (> tweets-to-go 100) 100 ; twitter returns at most 100 results per page
    (<= tweets-to-go 0) 0
    :else tweets-to-go))

(defn do-twitter-search 
  "Performs a single search. First two terms are mandatory: hash term, which determines filter criteria, and tweets-count,
   which determines in how many tweets user is interested. The third term is used for paging, determines upper bound on
   tweets result. If it is not provided, or if nil is provided, returns all tweets"
  ([hash-term tweets-count]
    (do-twitter-search hash-term tweets-count nil))
  ([hash-term tweets-count max-id]
    (let [params (if (nil? max-id)
                   {:q (str "#" hash-term) :count tweets-count}
                   {:q (str "#" hash-term) :count tweets-count :max_id max-id})]
      (twitter-api/search-tweets :oauth-creds util/my-creds 
                                 :params params))))

(defn next-max-id
  "Given twitter search results, finds the upper bound for the next twitter search"
  [result previous-max-id]
  (if (empty? result)
    previous-max-id
    (dec 
      (reduce min
              (map :id result)))))


(def max-minutes-old (util/config :max-minutes-old))

(defn is-newer-tweet 
  "Given a tweet containing a create date (:created_at) and a time when search was made, determines if this tweet is newer than appropriate
   Tweet is older than appropriate if it is older than the time configured with :max-minutes-old key from app configuration"
  [tweet time-of-search]
  (util/is-date-older
    (.minus time-of-search max-minutes-old java.time.temporal.ChronoUnit/MINUTES)
    (.parse (java.text.SimpleDateFormat. "EEE MMM dd HH:mm:ss ZZZZ yyyy") (tweet :created_at))))

(defn do-twitter-search-with-paging 
  "Performs the search on hash-term. Filters the result by excluding tweets older than configured. Returns the list of hashes found."
  [hash-term tweets-to-search max-id]
  (if (> tweets-to-search 0) 
    ; tweets-count: number of tweets to search for in this search. depending on paging, another search may be called recursively via recursion
    (let [tweets-count (tweets-this-turn tweets-to-search)
          now (java.time.Instant/now)
          ; current-tweets - all tweets from the search. newer tweets - tweets filtered
          current-tweets (get-in 
                           (do-twitter-search hash-term tweets-count max-id) 
                           [:body :statuses])
          newer-tweets (filter #(is-newer-tweet % now) current-tweets)
          hashes (extract-hashes newer-tweets hash-term) ; extracted from newer tweets
          tweets-left (- tweets-to-search tweets-count)]  ; used for paging, determines how many tweets should be called overall
      (if (and (every? #(is-newer-tweet % now) current-tweets)
               (> tweets-left 0)
               (> (count current-tweets) 0))
        (concat hashes (do-twitter-search-with-paging hash-term tweets-left (next-max-id current-tweets max-id)))
        hashes))
  '()))
