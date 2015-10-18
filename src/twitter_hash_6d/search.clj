(ns twitter-hash-6d.search
  (:require [twitter.api.restful :as twitter-api]
            [twitter-hash-6d.util :as util]))


(defn do-twitter-search 
  "Performs the search on hash-term. Returns the full result, as returned by twitter api"
  [hash-term]
  (twitter-api/search-tweets :oauth-creds util/my-creds 
                             :params 
                             {:q (str "#" hash-term) :count (util/config :tweets-per-search)}))
