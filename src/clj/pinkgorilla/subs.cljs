(ns pinkgorilla.subs
  (:require-macros
   [reagent.ratom :refer [reaction]])
  (:require
   [taoensso.timbre :refer-macros (info)]
   [re-frame.core :refer [reg-sub-raw reg-sub]]))

(reg-sub
 :main
 (fn [db _]
   (:main db)))


(reg-sub
 :docs
 (fn [db _]
   (:docs db)))

(reg-sub
 :config
 (fn [db _]
   (:config db)))

(reg-sub
 :palette
 (fn [db _]
   (:palette db)))

(reg-sub
 :message
 (fn [db _]
   (:message db)))

(reg-sub
 :worksheet
 (fn [db _]
   (get-in db [:worksheet])))

(reg-sub
 :meta
 (fn [db _]
   (get-in db [:worksheet :meta])))


(reg-sub
 :save-dialog
 (fn [db _]
   (get-in db [:save])))


; The dialog subscription can be used by all dialogs to manage dialog-visibility.
(reg-sub
 :dialog
 (fn [db _]
   (get-in db [:dialog])))

(reg-sub
 :settings
 (fn [db _]
   (get-in db [:settings])))

(reg-sub
 :segment-query
 (fn [db [_ seg-id]]
   (get-in db [:worksheet :segments seg-id])))

(reg-sub
 :is-active-query
 (fn [db [_ seg-id]]
   (= seg-id (get-in db [:worksheet :active-segment]))))

(reg-sub
 :is-queued-query
 (fn [db [_ seg-id]]
   (contains? (get-in db [:worksheet :queued-code-segments]) seg-id)))

(reg-sub
 :notifications
 (fn [db _]
   (:notifications db)))


;; navbar

(reg-sub
 ::current-view
 (fn [db _]
   (:current-view db)))

(reg-sub
 ::navbar-visible?
 :<- [::current-view]
 (fn [view _]
   (#{:playlist :home} view)))

(reg-sub
 :navbar-menu-is-active?
 (fn [db _]
   (:navbar-menu-active? db)))


(info "reframe subscriptions loaded.")