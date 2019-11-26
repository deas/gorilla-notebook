(ns pinkgorilla.db
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]
   [cognitect.transit :as t]
   [re-frame.core :refer [dispatch-sync]]
   [cljs.reader]
   [pinkgorilla.keybindings :refer [all-commands visible-commands]]
    [pinkgorilla.explore.data]
    ))


(def debug?
  ^boolean js/goog.DEBUG)

;; -- Schema -----------------------------------------------------------------

;; This is a clojure.spec specification for the value in app-db. It is like a
;; Schema. See: http://clojure.org/guides/spec
;;
;; The value in app-db should always match this spec. Only event handlers
;; can change the value in app-db so, after each event handler
;; has run, we re-check app-db for correctness (compliance with the Schema).
;;
;; How is this done? Look in events.cljs and you'll notice that all handers
;; have an "after" interceptor which does the spec re-check.
;;
;; None of this is strictly necessary. It could be omitted. But we find it
;; good practice.

(s/def ::config map?)
(s/def ::docs map?)
(s/def ::worksheet map?)
;; (s/def ::segment-order vector?)
#_(s/def ::id int?)
#_(s/def ::title string?)
#_(s/def ::done boolean?)
#_(s/def ::todo (s/keys :req-un [::id ::title ::done]))
#_(s/def ::todos (s/and                                     ;; should use the :kind kw to s/map-of (not supported yet)
                  (s/map-of ::id ::todo)                   ;; in this map, each todo is keyed by its :id
                  #(instance? PersistentTreeMap %)         ;; is a sorted-map (not just a map)
                  ))
#_(s/def ::showing                                          ;; what todos are shown to the user?
    #{:all                                                  ;; all todos are shown
      :active                                               ;; only todos whose :done is false
      :done                                                 ;; only todos whose :done is true
      })
(s/def ::db (s/keys :req-un
                    ;; [::docs ::config ::segments ::segment-order]
                    [::config ::worksheet ::docs]
                    ;; [::todos ::showing]
                    ))





(defn ck
  []
  (if (re-matches #".*Win|Linux.*" (.-platform js/navigator))
    "alt"
    "ctrl"))



; explore:
(def form-default {:data    {}
                   :errors  {}
                   :state   :sleeping})

;; -- Initial app-db Value  ---------------------------------------------------
(def initial-db
  {:docs
   {:content  ""
    :position []}
   :all-commands all-commands
   :palette      {;; TODO: We are (ab)using it for files and commands, "inherited" from js version
                  ;; Should probably be two instances
                  :visible-items        nil
                  :show                 false
                  :all-visible-commands visible-commands
                  :all-items            nil
                  :filter               ""
                  :label                ""
                  :highlight            0}
   :worksheet    {:meta {}}
   :config       {:read-only true
                  }
   :base-path    nil
   :message      nil
   :dialog {:settings false
            :save false
            :meta false}
   :settings
   {:default-kernel :clj
    :editor :text
    :github-token ""}
   :storage nil
   ; explore:
   :main :notebook
   :projects     {:selected nil}
   :forms {:projects {:create form-default
                      :update form-default
                      :search form-default}}
   :data {:projects pinkgorilla.explore.data/projects}
   :nav {}
   :initialized true})



(defn swap [v i1 i2]
  (assoc v i2 (v i1) i1 (v i2)))
