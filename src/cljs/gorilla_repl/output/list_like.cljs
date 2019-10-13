(ns gorilla-repl.output.list-like
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   
   [gorilla-repl.output.hack :refer [temp-comp-hack]]
   ))




(declare output-fn)

(defn output-list-like-other
  [output seg-id]
  (let [separator (temp-comp-hack (:separator output))
        open (temp-comp-hack (:open output))
        close (temp-comp-hack (:close output))
        value-comps (->> (map
                          #((output-fn %) % seg-id)
                          (:items output))
                         (interpose separator))
        all (filter some? (-> (into [open] value-comps) (conj close)))

        child-components (map-indexed (fn [i x]
                                        (with-meta x {:key (keyword (str "other-list-item-" i))}))
                                      all)]
    (into [:span.value {:data-value (:value output)}] child-components)))

(defn output-list-like-table
  [output seg-id row-wrap col-wrap]
  ;; (print "like table " seg-id row-wrap col-wrap)
  (let [value-comps (->> (map-indexed
                          (fn [i item]
                            (with-meta
                              (if col-wrap
                                [col-wrap [(output-fn item) item seg-id]]
                                [(output-fn item) item seg-id])
                              {:key (keyword (str "table-list-item-" i))}))
                          (:items output)))]
    (into [row-wrap] value-comps)))

;; TODO : Ugly, should probably rewrite the open, close, separator list like rendering
;; Before going further, we want it working and compatible
(defn output-list-like
  [output seg-id]
  (case (:open output)
    "<tr><td>"
    (output-list-like-table output seg-id :tr :td)
    "<tr><th>"
    (output-list-like-table output seg-id :tr :th)
    "<center><table>"
    (output-list-like-table output seg-id :center>table>tbody nil)
    (output-list-like-other output seg-id)))
;; (map hick/as-hiccup (hick/parse-fragment "<h1>HELLO WORLD!</h1>"))
;; return wrapWithValue(data, data.open + renderedItems.join(data.separator) + data.close);

