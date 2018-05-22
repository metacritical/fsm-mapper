(ns fsm-mapper.core
  (:require [fsm-mapper.command-line-parser :as cli]))


(defn -main [& args]  
  (let [{:keys [file show]} (cli/parse-args args)]
    (cond
      ;; Show FSM if commanline argument --show
      show (cli/show-fsm file)
      :else 
      (cli/create-project file))))

