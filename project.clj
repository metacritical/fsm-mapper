(defproject fsm-mapper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [reduce-fsm "0.1.4"]
                 [leiningen "2.8.1"]
                 [cljfmt "0.5.7"]]

  :main fsm-mapper.core
  :plugins [[lein-cljfmt "0.5.7"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :repl {:plugins [[cider/cider-nrepl "0.17.0"]]})
