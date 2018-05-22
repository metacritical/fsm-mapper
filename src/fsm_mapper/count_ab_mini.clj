(ns fsm-mapper.count-ab-mini
  (:require [reduce-fsm :as fsm]))

(defn inc-val [val & _] (inc val))

(fsm/defsm count-ab
  [[:start
    \a -> :found-a]
   [:found-a
    \a -> :found-a
    \b -> {:action inc-val} :start
    _ -> :start]])


(defn init-fsm [sequence]
  (count-ab 0 sequence))

