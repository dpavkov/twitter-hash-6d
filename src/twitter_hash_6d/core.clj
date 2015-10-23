(ns twitter-hash-6d.core
  (:require [twitter-hash-6d.search :as search]
            [clojure.string :as string]
            [twitter-hash-6d.util :as util]))

(defn -main
  "Takes the term from the api and performs the search"
  [hash-term]
  (println 
    (search/do-twitter-search-with-paging 
      (string/lower-case hash-term) 
      (util/config :tweets-per-search) 
      nil)))
