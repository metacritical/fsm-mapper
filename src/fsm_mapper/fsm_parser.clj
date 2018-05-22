(ns fsm-mapper.fsm-parser
  (:require [clojure.spec.alpha :as s]
            [fsm-mapper.count-ab-mini :as cab-mini]
            [fsm-mapper.parser-utils :as utils]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [cljfmt.core :refer [reformat-string]]))


(s/def :fsm/fsm-data
  (s/cat :fsm-name keyword?
         :init-val number?
         :init-data string?))

(s/def :fsm/def_fsm :fsm/fsm-data)

(s/def :fsm/transitions 
  (s/cat :start-node first
         :steps vector?))

(s/def :fsm/fsm-spec
  (s/keys :req [:fsm/def_fsm :fsm/transitions]))

(s/def :fsm/start-node 
  (s/cat :start-state keyword?
         :trans-data char?
         :trans-state keyword?))

(defmulti fsm->clj first)

(defmethod fsm->clj :fsm/def_fsm 
  [[_ {:keys [fsm-name init-val init-data]} v]]
    (list 'fsm/defsm (-> fsm-name name symbol) init-val init-data))

(defmethod fsm->clj :start-node [[_  v]]
  (let [{:keys [start-state trans-data trans-state]} 
        (s/conform :fsm/start-node v)]
    [start-state trans-data '-> trans-state]))

(defmethod fsm->clj :fsm/transitions [[_ transition]]
  (map #(fsm->clj %) transition))

(defmethod fsm->clj :steps [[_ v]]
  (let [steps (map #(apply utils/steps->clj %) (rest v))]
    [(first v) (symbol "\n") (utils/symbolize-steps steps)]))

(defn parse-ast [spec]
  (map #(fsm->clj %) (s/conform :fsm/fsm-spec spec)))

(defn parse->fsm [spec]
  (let [[defsm steps] (parse-ast spec)]
    (reformat-string (utils/concat-defsm->steps defsm steps))))

(defn parse-fsm-to-clojure [file]
  (parse->fsm (utils/edn-data file)))
