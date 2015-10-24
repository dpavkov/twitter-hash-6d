(ns twitter-hash-6d.search-test
  (:require [clojure.test :refer :all]
            [twitter-hash-6d.search :refer :all]))

(deftest test-extract-hashes
  (is (= [] (extract-hashes []"search-term")))
  (is (= [] (extract-hashes [{:entities {}}] "search-term")))
  (is (= [] (extract-hashes [{:entities {:hashtags []}}] "search-term")))
  (is (= [] (extract-hashes [{:entities {:hashtags [{:text "search-term"}]}}] "search-term")))
  (is (= ["other-term"] (extract-hashes [{:entities {:hashtags [{:text "search-term"}
                                                                {:text "other-term"}]}}] "search-term")))
  (is (= ["other-term" "third-term"] (extract-hashes [{:entities {:hashtags [{:text "search-term"}
                                                                             {:text "other-term"}]}}
                                                      {:entities {:hashtags [{:text "third-term"}]}}] "search-term")))
  (is (= ["other-term"] (extract-hashes [{:entities {:hashtags [{:text "search-term"}
                                                                {:text "other-term"}]}}
                                         {:entities {:hashtags [{:text "Search-term"}]}}] "search-term"))))

(deftest test-tweets-this-turn
  (is (= 54 (tweets-this-turn 54)))
  (is (= 0 (tweets-this-turn -1)))
  (is (= 100 (tweets-this-turn 107))))

(deftest test-do-twitter-search-with-paging
  (with-redefs [do-twitter-search (fn [x y z] {:body {:statuses [{:id 123451234214}]}})
                extract-hashes (fn [x y] '("oneres" "twores"))
                is-newer-tweet (fn [x y] true)]
    (is (= '() (do-twitter-search-with-paging "hash-term" 0 nil)))
    (is (= '("oneres" "twores") (do-twitter-search-with-paging "hash-term" 1 nil)))
    (is (= '("oneres" "twores") (do-twitter-search-with-paging "hash-term" 100 nil)))
    (is (= '("oneres" "twores" "oneres" "twores") (do-twitter-search-with-paging "hash-term" 101 nil)))
    (is (= '("oneres" "twores" "oneres" "twores") (do-twitter-search-with-paging "hash-term" 200 nil)))))

(deftest test-next-max-id
  (is (= 77 (next-max-id [] 77)))
  (is (= nil (next-max-id [] nil)))
  (is (= 75 (next-max-id [{:id 76}] 77)))
  (is (= 66 (next-max-id [{:id 76} {:id 67}] 77)))
  (is (= 44 (next-max-id [{:id 45} {:id 55}] 77))))

(defn- parse-date [date]
  (.toInstant (.parse (java.text.SimpleDateFormat. "ddMMyyyy HHmm ZZZZ") date)))

(deftest test-is-newer-tweet
  (with-redefs [max-minutes-old 20]
    (is (is-newer-tweet {:created_at "Fri Oct 23 03:49:28 +0000 2015"} (parse-date "23102015 0409 +0000")))
    (is (not (is-newer-tweet {:created_at "Fri Oct 23 03:49:28 +0000 2015"} (parse-date "23102015 0410 +0000"))))))