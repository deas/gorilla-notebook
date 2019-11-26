(ns pinkgorilla.events.storage-save-dialog
  (:require
   [taoensso.timbre :refer-macros (info)]
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.storage.storage :refer [Storage storagetype query-params-to-storage gorilla-path]]
   [pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]))


;; Save-Dialog Open/Close

(reg-event-db
 :app:saveas
 [standard-interceptors]
 (fn [db _]
   (assoc-in db [:dialog :save] true)))

(reg-event-db
 :save-dialog-cancel
 [standard-interceptors]
 (fn [db _]
   (assoc-in db [:dialog :save] false)))

;; Respond to Events from Save-Dialog

; change storage settings
(reg-event-db
 :save-as-storage
 [standard-interceptors]
 (fn [db [_ params]]
   (let [storage-type (keyword (:source params))
         _ (info "saving as storage type: " storage-type)
         storage (query-params-to-storage storage-type params)]
     (assoc-in db [:storage] storage))))

; navigate to current storage settings
(reg-event-db
 :nav-to-storage
 [standard-interceptors]
 (fn [db [_ params]]
   (let [storage (:storage db)]
     (routes/nav! (gorilla-path storage))
     db)))


; navigate to ...
(reg-event-fx
 :nav
 [standard-interceptors]
  (fn [{:keys [db]} [_ url]]
   (routes/nav! url)
   ))




