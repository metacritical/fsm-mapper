(ns fsm-mapper.project-gen
  (:require [leiningen.new :as lein]
            [clojure.java.io :as io]
            [fsm-mapper.parser-utils :as utils]
            ))

(defn delete-file-recursively
  "delete file f. if it's a directory, recursively delete all its contents.
  raise an exception if any deletion fails unless silently is true."
  [f & [silently]]
  (System/gc) ; this sometimes helps release files for deletion on windows.
  (if (.exists (io/file f)) 
    (let [f (io/file f)]
      (if (.isDirectory f)
        (doseq [child (.listFiles f)]
          (delete-file-recursively child silently)))
      (io/delete-file f silently))))

(defn new-lein-project [file]
  (let [base-name (utils/org-name-normal file)]
    (delete-file-recursively (io/file (str "out/" base-name)))
    (lein/new nil "app" base-name "--to-dir" (str "out/" base-name))
    (printf "Project created at : out/%s\n" base-name)))
