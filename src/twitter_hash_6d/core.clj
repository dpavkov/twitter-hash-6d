(ns twitter-hash-6d.core
  (:require [twitter-hash-6d.search :as search]
            [clojure.string :as string]))

(defn -main
  "Takes the term from the api and perorms the search"
  [hash-term]
  (println (search/do-twitter-search (string/lower-case hash-term))))
