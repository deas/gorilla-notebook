(ns pinkgorilla.events.settings
  "events related to the settings dialog"
  (:require
   [taoensso.timbre :refer-macros (info)]
   [re-frame.core :refer [reg-event-db dispatch]]
   [pinkgorilla.components.localstorage :refer [ls-get ls-set!]]
   [pinkgorilla.kernel.cljs :as cljs-kernel]))


;; Dialog Visibility Management


(reg-event-db
 :dialog-show
 (fn [db [_ dialog]]
   (assoc-in db [:dialog dialog] true)))

(reg-event-db
 :dialog-hide
 (fn [db [_ dialog]]
   (assoc-in db [:dialog dialog] false)))

;; Settings Dialog Visibility


(reg-event-db
 :app:hide-settings
 (fn [db _]
   (dispatch [:settings-localstorage-save])                ; save to localstorage on close of dialog.
   (assoc-in db [:dialog :settings] false)))

;; Settings Change

(reg-event-db
 :settings-localstorage-load
 (fn [db [_]]
   (let [_ (info "Notebook Settings: Loading from Localstorage ..")
         settings (ls-get :notebook-settings)]
     (if (nil? settings)
       (do (info "Notebook Settings: localstorage empty!")
           db)
       (do (info "Notebook Settings: successfully loaded from localstorage: " (keys settings))
           (assoc-in db [:settings] settings))))))

(reg-event-db
 :init-cljs
 (fn [db [_]]
   (let [cljs-config (get-in db [:config :cljs :kernel])]
     (cljs-kernel/init! cljs-config))
   db))

(reg-event-db
 :settings-localstorage-save
 (fn [db [_]]
   (let [settings (:settings db)]
     (ls-set! :notebook-settings settings)
     (info "localstorage settings saved: " settings)
     db)))

(reg-event-db
 :settings-set
 (fn [db [_ k v]]
   (info "changing setting " k " to: " v)
   (assoc-in db [:settings k] v)))

(reg-event-db
 :meta-set
 (fn [db [_ k v]]
   (info "changing notebook meta " k " to: " v)
   (assoc-in db [:worksheet :meta k] v)))
