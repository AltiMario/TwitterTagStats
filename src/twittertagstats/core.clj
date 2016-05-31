(ns twittertagstats.core
  (:require [cheshire.core :as json]
            [twitter.api.restful :refer [statuses-user-timeline]]
            [twitter.api.streaming :refer [user-stream]]
            [twitter.oauth :as auth])
  (:require [twittertagstats.util :as ut])
  (:import [twitter.callbacks.protocols SyncSingleCallback AsyncStreamingCallback]))


(defonce frequencies-val (atom {}))


(defn extract-tags-tweet
  "Extract tags from a tweet with a basic regex"
  [tweet]
  (re-seq #"#[a-zA-Z]\w*" tweet))


(defn normalize-list
  "Return a normalized list"
  [unclean-list]
  (flatten (filter (complement nil?) unclean-list)))


(defn frequencies-stats
  "For a nice result display"
  [frequencies-map]
  (with-out-str
    (clojure.pprint/print-table
      (map (fn [[t c]] {:hashtag t :count c})
           (sort-by second >
                    frequencies-map)))))


(defn combine-frequencies
  "Combine the timeline frequencies map with the streaming frequencies map"
  [frequency-map1 frequency-map2]
  (merge-with + frequency-map1 frequency-map2))


(defn twitter-authentication
  "Return the credentials after authentication"
  []
  (auth/make-oauth-creds (System/getenv "OAUTH_CONSUMER_KEY")
                         (System/getenv "OAUTH_CONSUMER_SECRET")
                         (System/getenv "OAUTH_APP_KEY")
                         (System/getenv "OAUTH_APP_SECRET")))


(defn user-timeline
  "Ask the user timeline (past tweets) through RESTful-API"
  [credentials account count]
  (try
    (:body
      (statuses-user-timeline :oauth-creds credentials
                              :params {:screen-name account :count count}
                              ))
    (catch Exception x
      (ut/error-manager x "Error during the RESTful-API access"))))


(defn tag-frequencies
  "Return the hashtags frequencies from the full JSON twitter"
  [tweets]
  (frequencies
    (normalize-list
      (map extract-tags-tweet tweets))))


(defn tag-frequencies-user-timeline
  "Return the tag frequencies of the specified user timeline"
  [creds data-config]
  (tag-frequencies
    (map :text
         (user-timeline creds
                        (System/getenv "TWITTER_HANDLE")
                        (:max-tweets data-config)))))


(defn from-json [stream]
  (when stream
    (let [body (String. (.toByteArray stream) "utf8")]
      (json/parse-string body true))))


(defn user-streaming
  "retrieves the user streaming tweet"
  [creds account]
  (user-stream
    :oauth-creds creds
    :params {:screen-name account}
    :callbacks (AsyncStreamingCallback.
                 (fn [_ cnt]
                   (try
                     (some->> cnt
                              from-json
                              :text
                              vector
                              tag-frequencies
                              (swap! frequencies-val combine-frequencies))
                     (catch Exception x
                       (ut/error-manager x "Unable to read message from server"))))
                 (fn [& x] (prn "ERROR" x))
                 (fn [& x] (prn "ERROR" x)))))


(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/plain"}
   :body    (frequencies-stats @frequencies-val)})