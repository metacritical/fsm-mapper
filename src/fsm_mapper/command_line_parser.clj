(ns fsm-mapper.command-line-parser
  (:require [clojure.string :as str]
            [reduce-fsm :as fsm]
            [fsm-mapper.fsm-parser :as parser]
            [fsm-mapper.project-gen :as project]
            [fsm-mapper.parser-utils :as utils]
            [cljfmt.core :refer [reformat-string]]))

(def arg-summary
  [["FSM Mapper Example :\n"]
   ["lein run --file [test-fsm.edn]"]
   ["--file" "Input fsm file in edn"]
   ["--show" "Show FSM Diagram"]])

(defn symbolize-args [arg]
  (cond (re-find #"^--" arg)
        (keyword (str/replace arg #"^--" ""))
        :else arg))

(defn hashify-args [args]
  (let [arg-list (map symbolize-args args)]
    (apply hash-map arg-list)))

(defn parse-args [args]
  (when (empty? args) 
    (doseq [i arg-summary]
      (println (str/join " " i)))
    (System/exit 0))
  (hashify-args args))

(defn new-project [file]
  (project/new-lein-project file))

(defn fsm->clj [file]
  (-> file 
      (utils/edn-data)
      (parser/parse->fsm)))

(defn get-fsm-name [file]
  (-> file
      (utils/edn-data)
      (utils/fsm-name-normal)))

(defn get-fsm-name-normal [file]
  (-> file
      (utils/edn-data)
      (utils/fsm-name)))

(defn get-ns [file]
  (str (utils/org-name-normal file) "." (get-fsm-name file)))

(defn show-fsm-fn [file]
  (str "(fsm/show-fsm " (get-fsm-name file) ")"))

(defn show-fsm [file]
  (let [clj-code (fsm->clj file) name-space (get-ns file)]
    (load-string clj-code)
    (in-ns (symbol name-space))
    (load-string (show-fsm-fn file))))


(defn update-project-clj [org-name file-name]
  (let [template (str/join " " (read-string (utils/dependency-template)))
        project-file (slurp (str "out/" org-name "/project.clj"))
        main-func (utils/main-func-template (get-ns file-name))]
    (let [updates (-> project-file    
                      (str/replace #"(:dependencies.*)" template)
                      (str/replace #"(:main.*)" main-func))]
      
      (spit (str "out/" org-name "/project.clj") updates))))
    
    

(defn write-file [file]
  (let [clj-code (fsm->clj file) org-name (utils/org-name-normal file)
        fsm-name (get-fsm-name-normal file)]
    (spit (utils/fsm->clj-org-path org-name fsm-name) clj-code)
    (update-project-clj org-name file)))

(defn create-project [file]
  (new-project file)
  (write-file file))
  

