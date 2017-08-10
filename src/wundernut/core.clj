(ns wundernut.core
  (:require [wundernut.nut1 :as nut1]))

(defn -main [file]
  (let [result (nut1/solve file)]
    (println (str "Funniest word is '" (first result)"' (" (last result) ")."))))
