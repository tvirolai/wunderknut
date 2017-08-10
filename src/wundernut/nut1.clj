(ns wundernut.nut1
  (:require [clojure.string :as s]
            [opennlp.nlp :as nlp]))

(defn extract-words [text]
  ((nlp/make-sentence-detector "./data/fi-sent.bin") text))

(defn- tokenize [se]
  (->> se (apply str) (re-seq #"[\p{L}\p{M}]+") (map s/lower-case)))

(defn get-unique-words [text]
  (->> text extract-words (map tokenize) flatten set seq))

(defn get-vowel-chain [word]
  (->> word (re-seq #"[aeiouyäöå]*") (filter not-empty)))

(def xf
  (comp
    (map count)
    (map #(* % (Math/pow 2 %)))))

(defn word-points [word]
  (->> word get-vowel-chain (transduce xf +)))

(defn find-funniest [text]
  (loop [words (get-unique-words text)
         topwords {"ja" 2}]
    (if (empty? words)
      (apply max-key val topwords)
      (let [curr (word-points (first words))]
        (if (> curr (reduce max (vals topwords)))
          (recur (rest words)
                 (assoc topwords (first words) curr))
          (recur (rest words) topwords))))))

(defn solve [file]
  (->> file slurp find-funniest))
