(ns fsm-mapper.parser-utils
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn symbolize-steps [steps]
  (symbol (str/join "" (map #(symbol %) steps))))

(defn require-fsm-libs [project-name name-space]
  (format 
     "(ns %s.%s
       (:require [reduce-fsm :as fsm]))\n\n" project-name name-space))

(defn dependency-template []
  (str '[:dependencies [[org.clojure/clojure "1.9.0"]
                 [reduce-fsm "0.1.4"]
                 [leiningen "2.8.1"]
                 [cljfmt "0.5.7"]]]))

(defn main-func-template [func-str]
  (str ":main ^:skip-aot " func-str "/init-fsm"))

(defn gen-fsm->defn [definition steps]
  (->> (concat definition "[" steps "]")
       (interpose "\n")
       (apply str)))

(defn stringify-fsm->defn [definition steps]
  (str "(" (gen-fsm->defn definition steps) ")" "\n"))

(defn org-name [file]
  (str/replace (-> file (io/file) (.getName) (str/split #"\." ) first) "-" "_"))

(defn org-name-normal [file]
  (str/replace (-> file (io/file) (.getName) (str/split #"\." ) first) "_" "-"))

(defn fsm-name [edn-data]
  (str/replace (-> edn-data :fsm/def_fsm first name) "-" "_"))

(defn fsm-name-normal [edn-data]
  (str/replace (-> edn-data :fsm/def_fsm first name) "_" "-"))

(def template-library-function 
  "(defn inc-val [val & _] (inc val))\n")

(defn init-fsm [def data]
  (str "(defn init-fsm [] (println " (cons (last def) data) "))\n"))

(defn concat-defsm->steps [defsm steps]
  (let [[definition data] (split-at 2 defsm) 
        project-name (org-name-normal (last *command-line-args*))]
    
    (str
     (require-fsm-libs project-name (last definition))
     template-library-function 
     (stringify-fsm->defn definition steps)
     (init-fsm definition data))))

(defn fsm->clj-org-path [org file]
  (str "out/" org  "/src/" (org-name org)  "/" file ".clj"))

(defn get-fsm-file-name [org-name fsm-edn]
  (let [fsm-name (fsm-name fsm-edn)]
    (fsm->clj-org-path org-name fsm-name)))

(defn edn-data [file]
  (edn/read-string (slurp file)))

(defn steps->clj
  ([first last] (prn-str first '-> last))
  ([first middle last] (prn-str first '-> middle last)))
