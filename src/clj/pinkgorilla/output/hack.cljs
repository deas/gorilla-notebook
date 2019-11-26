(ns pinkgorilla.output.hack)


(defn temp-comp-hack
  [no-kw]
  (if no-kw (into [(keyword (first no-kw))]
                  (rest no-kw))))

(defn value-wrap
  [value content]
  [:span.value {:data-value value} content])