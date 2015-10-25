(ns twitter-hash-6d.util
  (:require [twitter.oauth :as oauth]
            [clojure.edn :as edn]))

;; Reads file properties. Props are stored as maps, so they can be accessed as (config :key)
(def config (edn/read-string (slurp "config.edn")))

(def my-creds (oauth/make-oauth-creds (config :api-consumer-key )
                                      (config :api-consumer-secret)
                                      (config :user-access-token)
                                      (config :user-access-token-secret)))

(defmulti get-milis 
  "Takes either instance of java.util.Date or java.time.Instant and returns milis from the epoch"
  class)
(defmethod get-milis java.util.Date [date] (.getTime date))
(defmethod get-milis java.time.Instant [date] (get-milis (java.util.Date/from date)))

(defn is-date-older
  "Returns true if the first date entered as an argument is older than another one"
  [one another]
  (<
    (get-milis one)
    (get-milis another)))