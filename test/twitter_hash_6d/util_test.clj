(ns twitter-hash-6d.util-test
  (:require [clojure.test :refer :all]
            [twitter-hash-6d.util :refer :all]))

(deftest test-is-date-older
  (is (is-date-older (new java.util.Date) (.minusSeconds (java.time.Instant/now) -1)))
  (is (not (is-date-older (new java.util.Date) (.minusSeconds (java.time.Instant/now) 1))))
  (is (is-date-older (.minusSeconds (java.time.Instant/now) 1) (new java.util.Date) ))
  (is (not (is-date-older (.minusSeconds (java.time.Instant/now) -1) (new java.util.Date) ))))