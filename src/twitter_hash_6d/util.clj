(ns twitter-hash-6d.util
  (:require [twitter.oauth :as oauth]
            [clojure.edn :as edn]))

;; Reads file properties. Props are stored as maps, so they can be accessed as (config :key)
(def config (edn/read-string (slurp "config.edn")))

(def my-creds (oauth/make-oauth-creds (config :api-consumer-key )
                                (config :api-consumer-secret)
                                (config :user-access-token)
                                (config :user-access-token-secret)))