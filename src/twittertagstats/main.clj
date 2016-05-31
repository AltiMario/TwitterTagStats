(ns twittertagstats.main
  (:use [ring.adapter.jetty])
  (:require [twittertagstats.core :as core]
            [twittertagstats.util :as ut]
            [clojure.tools.logging :as log])
  (:gen-class))


(defn -main [config-file]
  (try
    (if (System/getenv "TWITTER_HANDLE")
      (if (System/getenv "OAUTH_APP_KEY")
        (if (System/getenv "OAUTH_APP_SECRET")
          (if (System/getenv "OAUTH_CONSUMER_KEY")
            (if (System/getenv "OAUTH_CONSUMER_SECRET")
              (let [config (ut/load-properties config-file)
                    creds (core/twitter-authentication)
                    timeline-frequencies (core/tag-frequencies-user-timeline creds config)]
                (run-jetty core/handler {:port (:port config) :join? false})
                (swap! core/frequencies-val core/combine-frequencies timeline-frequencies)
                (core/user-streaming creds (System/getenv "TWITTER_HANDLE")))
              (log/warn (format "Please set the env variables")))))))
    (catch Exception x
      (ut/error-manager x "Error during initialization process"))))

(comment
  (-main "config/config.edn"))
