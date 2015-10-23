(ns twitter-hash-6d.search-test
  (:require [clojure.test :refer :all]
            [twitter-hash-6d.search :refer :all]))

(deftest test-extract-hashes
  (is (= [] (extract-hashes {} "search-term")))
  (is (= [] (extract-hashes {:body {}} "search-term")))
  (is (= [] (extract-hashes {:body {:statuses []}} "search-term")))
  (is (= [] (extract-hashes {:body {:statuses [{:entities {}}]}} "search-term")))
  (is (= [] (extract-hashes {:body {:statuses [{:entities {:hashtags []}}]}} "search-term")))
  (is (= [] (extract-hashes {:body {:statuses [{:entities {:hashtags [{:text "search-term"}]}}]}} "search-term")))
  (is (= ["other-term"] (extract-hashes {:body {:statuses [{:entities {:hashtags [{:text "search-term"}
                                                                                  {:text "other-term"}]}}]}} "search-term")))
  (is (= ["other-term" "third-term"] (extract-hashes {:body {:statuses [{:entities {:hashtags [{:text "search-term"}
                                                                                               {:text "other-term"}]}}
                                                                        {:entities {:hashtags [{:text "third-term"}]}}]}} "search-term")))
  (is (= ["other-term"] (extract-hashes {:body {:statuses [{:entities {:hashtags [{:text "search-term"}
                                                                                  {:text "other-term"}]}}
                                                           {:entities {:hashtags [{:text "Search-term"}]}}]}} "search-term"))))

(deftest test-tweets-this-turn
  (is (= 54 (tweets-this-turn 54)))
  (is (= 0 (tweets-this-turn -1)))
  (is (= 100 (tweets-this-turn 107))))

(deftest test-do-twitter-search-with-paging
  (with-redefs [do-twitter-search (fn [x y z] {})
                extract-hashes (fn [x y] '("oneres" "twores"))]
    (is (= '() (do-twitter-search-with-paging "hash-term" 0 nil)))
    (is (= '("oneres" "twores") (do-twitter-search-with-paging "hash-term" 1 nil)))
    (is (= '("oneres" "twores") (do-twitter-search-with-paging "hash-term" 100 nil)))
    (is (= '("oneres" "twores" "oneres" "twores") (do-twitter-search-with-paging "hash-term" 101 nil)))
    (is (= '("oneres" "twores" "oneres" "twores") (do-twitter-search-with-paging "hash-term" 200 nil)))))

(deftest test-next-max-id
  (is (= 77 (next-max-id {:body {:statuses []}} 77)))
  (is (= nil (next-max-id {:body {:statuses []}} nil)))
  (is (= 75 (next-max-id {:body {:statuses [{:id 76}]}} 77)))
  (is (= 66 (next-max-id {:body {:statuses [{:id 76} {:id 67}]}} 77)))
  (is (= 44 (next-max-id {:body {:statuses [{:id 45} {:id 55}] }} 77))))