(ns twittertagstats.core-test
  (:require [twittertagstats.core :refer :all])
  (:use midje.sweet))


(facts "about `extract-tags-tweet`"
       (fact "it normally returns a list"
             (extract-tags-tweet "word #ciao word2 #blackout word3 #testing WORD4") => ["#ciao" "#blackout" "#testing"]
             (extract-tags-tweet "word http://www.url.com#ciao word2") => ["#ciao"]
             (extract-tags-tweet "#singletag1") => ["#singletag1"]
             (extract-tags-tweet "#tag1#tag2") => ["#tag1" "#tag2"]
             (extract-tags-tweet "#a") => ["#a"]
             (extract-tags-tweet "#1") => nil
             (extract-tags-tweet "#1ciaocisoasdif") => nil))

       (fact "I'm not sure if it's the right expected result"
             (extract-tags-tweet "##b") => ["#b"])


(facts "about `normalize-list`"
       (fact "should return a simple list of right value, from a nested structure with nil inside"
             (normalize-list [nil ["#tag1" "#tag2"] nil nil "#tag1"]) => ["#tag1" "#tag2" "#tag1"]
             (normalize-list ["#tag1"]) => ["#tag1"]
             (normalize-list [nil]) => []
             (normalize-list []) => []))


(facts "about `combine-frequencies`"
       (fact "should sum all frequencies for tag in 2 maps"
             (combine-frequencies {"#tag1" 5 "#tag2" 1 "#tag3" 1, "#tag4" 4} {"#tag4" 5 "#tag2" 1}) => {"#tag1" 5 "#tag2" 2 "#tag3" 1 "#tag4" 9}
             (combine-frequencies nil {"#tag1" 3}) => {"#tag1" 3}
             (combine-frequencies {} {"#tag1" 3}) => {"#tag1" 3}))


(facts "about `tag-frequencies`"
       (fact "should return a list of unique tags with the frequencies"
             (tag-frequencies ["first #tag" "second #tag" "new #tagnew"]) => {"#tag" 2 "#tagnew" 1}
             (tag-frequencies [""]) => {}))