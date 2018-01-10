(defproject reframe-utils "0.2.0-SNAPSHOT"
  :description "Utility/helper functions for use with re-frame"
  :url "https://github.com/nikolap/reframe-utils"
  :license {:name "The MIT License (MIT)"
            :url  "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [re-frame "0.10.2"]
                 [cljs-ajax "0.7.3"]]
  :deploy-repositories [["releases" {:sign-releases false :url "https://clojars.org/repo"}]
                        ["snapshots" {:sign-releases false :url "https://clojars.org/repo"}]])
