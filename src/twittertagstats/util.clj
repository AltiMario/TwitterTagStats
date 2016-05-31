(ns twittertagstats.util
  (:require [clojure.tools.logging :as log]
            [clojure.edn :as edn]))

(defn error-manager [x message]
  (let [error-id (str "ERR-" (.toString (java.util.UUID/randomUUID)))]
    (log/error x (str "Exception number: " error-id))
    {:status   "ERROR"
     :message  message
     :reason   (.getMessage x)
     :error-id error-id}))


(defn load-properties [config-file]
  (try
    (edn/read-string (slurp config-file))
    (catch Exception x
      (error-manager x "Error loading config file"))))