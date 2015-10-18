(ns twitter-hash-6d.search-test
  (:require [clojure.test :refer :all]
            [twitter-hash-6d.search :refer :all]))

(deftest test-extract-hashes
  (is (= {"search-term" []} (extract-hashes {} "search-term")))
  (is (= {"search-term" []} (extract-hashes {:body {}} "search-term")))
  (is (= {"search-term" []} (extract-hashes {:body {:statuses []}} "search-term")))
  (is (= {"search-term" []} (extract-hashes {:body {:statuses [{:entities {}}]}} "search-term")))
  (is (= {"search-term" []} (extract-hashes {:body {:statuses [{:entities {:hashtags []}}]}} "search-term")))
  (is (= {"search-term" []} (extract-hashes {:body {:statuses [{:entities {:hashtags [{:text "search-term"}]}}]}} "search-term")))
  (is (= {"search-term" ["other-term"]} (extract-hashes {:body {:statuses [{:entities {:hashtags [{:text "search-term"}
                                                                                      {:text "other-term"}]}}]}} "search-term")))
  (is (= {"search-term" ["other-term" "third-term"]} (extract-hashes {:body {:statuses [{:entities {:hashtags [{:text "search-term"}
                                                                                      {:text "other-term"}]}}
                                                                           {:entities {:hashtags [{:text "third-term"}]}}]}} "search-term")))
  (is (= {"search-term" ["other-term"]} (extract-hashes {:body {:statuses [{:entities {:hashtags [{:text "search-term"}
                                                                                      {:text "other-term"}]}}
                                                                           {:entities {:hashtags [{:text "Search-term"}]}}]}} "search-term"))))