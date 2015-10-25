(ns twitter-hash-6d.collector-test
  (:require [clojure.test :refer :all]
            [twitter-hash-6d.collector :refer :all]))

(deftest test-get-new-hashes
  (is (= #{} (get-new-hashes #{} {1 #{:a :b}})))
  (is (= #{} (get-new-hashes #{:a} {1 #{:a :b}})))
  (is (= #{:c} (get-new-hashes #{:c} {1 #{:a :b}})))
  (is (= #{:c} (get-new-hashes #{:a :c} {1 #{:a} 2 #{:b}})))
  (is (= #{:a} (get-new-hashes #{:a :b} {1 #{:b}})))
  (is (= #{:a :b} (get-new-hashes #{:a :b} {}))))

(deftest test-search-and-append-results-empty-search-res
  (with-redefs [do-search (fn [x] '())]
    (is (= {:degrees {0 '("term") 1 '()}, 
            :unique-hashes {0 #{"term"}, 1 nil},
            :assoc-hashes {"term" '()}}
           (search-and-append-results "term" 1 
                                      {:degrees {0 '("term")}, 
                                       :unique-hashes {0 #{"term"}}
                                       } ))))
  (with-redefs [do-search (fn [x] '("second-term" "third-term"))]
    (is (= {:degrees {0 '("term") 1 '("second-term" "third-term")}, 
            :unique-hashes {0 #{"term"}, 1 #{"second-term" "third-term"}},
            :assoc-hashes {"term" '("second-term" "third-term")}}
           (search-and-append-results "term" 1 
                                      {:degrees {0 '("term")}, 
                                       :unique-hashes {0 #{"term"}}
                                       } )))
    (is (= {:degrees {0 '("term"), 1 '("forth-term"),  2 '("second-term" "third-term")}, 
            :unique-hashes {0 #{"term"}, 1 #{"forth-term"}, 2 #{"second-term" "third-term"}},
            :assoc-hashes {"term" '("forth-term")
                           "forth-term" '("second-term" "third-term")}}
           (search-and-append-results "forth-term" 2 
                                      {:degrees {0 '("term") 1 '("forth-term")}, 
                                       :unique-hashes {0 #{"term"}  1 #{"forth-term"}}
                                       :assoc-hashes {"term" '("forth-term")}
                                       } )))
    (is (= {:degrees {0 '("term"), 1 '("forth-term" "fifth-term"),  2 '("second-term" "sixth-term" "second-term" "third-term")}, 
            :unique-hashes {0 #{"term"}, 1 #{"forth-term" "fifth-term"}, 2 #{"second-term" "sixth-term" "third-term"}},
            :assoc-hashes {"term" '("forth-term", "fifth-term"),
                           "forth-term" '("second-term" "sixth-term"),
                           "fifth-term" '("second-term" "third-term")}}
           (search-and-append-results "fifth-term" 2 
                                      {:degrees {0 '("term"), 1 '("forth-term" "fifth-term"), 2 '("second-term" "sixth-term")}, 
                                       :unique-hashes {0 #{"term"},  1 #{"forth-term" "fifth-term"}, 2 #{"second-term" "sixth-term"}}
                                       :assoc-hashes {"term" '("forth-term" "fifth-term"), "forth-term" '("second-term" "sixth-term")}
                                       } )))))
