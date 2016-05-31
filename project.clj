(defproject twittertagstats "0.1.0-SNAPSHOT"
  :description "Utility to produce a report of the frequency of hashtag usages on a twitter account"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [twitter-api "0.7.8"]
                 [org.slf4j/slf4j-log4j12 "1.7.21"]
                 [log4j/log4j "1.2.17"]
                 [cheshire "5.6.1"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]]
  :main twittertagstats.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all :uberjar-name "twittertagstats-standalone.jar"}
             :dev     {:dependencies [[midje "1.8.2"]]
                       :plugins      [[lein-midje "3.2"]]}})

