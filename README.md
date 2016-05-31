# Twitter-Tag-Stats ver 0.1 #
A simple report of the frequency of hashtag usages on a twitter account.

## Technical Requirements

* Java 8 / Clojure 1.7 / Leiningen 2.x
* Accepts the oauth creds from the environment (env.sh is ready to use):
   TWITTER_HANDLE
   OAUTH_APP_KEY
   OAUTH_APP_SECRET
   OAUTH_CONSUMER_KEY
   OAUTH_CONSUMER_SECRET

## Usage

* To run the app

    $ java -jar twittertagstats-standalone.jar config/config.edn

* To retrieve the stats

    $ curl http://localhost:3000


* Default config values

    max-tweets = 500    ; max tweets to parse from user-timeline

    server port = 3000


## License

Copyright © 2016 Mario Altimari

Distributed under the GNU General Public License version 3.0