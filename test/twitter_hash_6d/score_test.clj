(ns twitter-hash-6d.score-test
  (:require [clojure.test :refer :all]
            [twitter-hash-6d.score :refer :all]))

(deftest test-score
  (with-redefs [scoring-chart { 1 10, 2 3, 3 1}]
    (is (= {"term" 13, "term2" 11, "term3" 6, "term4" 1, "term5" 1}
           (score {1 '("term" "term2"), 2 '("term" "term3" "term3"), 3 '("term2" "term4" "term5")})))))