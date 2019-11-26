(ns pinkgorilla.events.storage
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   [ajax.core :as ajax :refer [GET POST]]
   [taoensso.timbre :refer-macros (info)]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.notebook.core :refer [load-notebook-hydrated save-notebook-hydrated]]
   [pinkgorilla.storage.storage :refer [Storage storagetype query-params-to-storage gorilla-path]]
   [pinkgorilla.storage.direct.direct :refer [Direct load-url decode-content]]
   [pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]))


;; Load File (from URL Parameters) - in view or edit mode

;; http://localhost:3449/worksheet.html#/view?source=github&user=JonyEpsilon&repo=gorilla-test&path=ws/graph-examples.clj
;; http://localhost:3449/worksheet.html#/view?source=gist&id=2c210794185e9d8c0c80564db581b136&filename=new-render.clj


#_(reg-event-fx
 :view-file
 (fn [{:keys [db]} [_ params]]
   (let [storage-type (keyword (:source params))
         storage (query-params-to-storage storage-type params)
         url (load-url storage (:base-path db))
         _ (info "loading from storage:" storage-type " url: " url)]
     {:db         db
      :http-xhrio {:method          :get
                   :uri             url
                   :timeout         5000
                   :response-format (ajax/json-response-format)
                   :on-success      [:process-load-file-response storage]
                   :on-failure      [:process-error-response]}})))


(reg-event-fx
 :edit-file
 (fn [{:keys [db]} [_ params]]
   (let [_ (info "edit-file params:" params)
         stype (keyword (:source params))
         storage (query-params-to-storage stype params)
         ;url (load-url storage (:base-path db))
         ;_ (info "loading from storage:" stype " url: " url)
         tokens (:settings db)
         url  (str (:base-path db) "load")
         params (assoc storage
                       :storagetype stype
                       :tokens tokens)]
     {:db         (assoc-in db [:main] :notebook) ; notebook view on loading
      :http-xhrio {:method          :get
                   :uri             url
                   :params          params
                   :timeout         15000
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:process-load-file-response storage]
                   :on-failure      [:process-error-response]}})))


(reg-event-db
 :process-load-file-response
 [standard-interceptors]
 (fn
   [db [_ storage response-body]]
   (let [
         _ (info "Load Response:\n" response-body)
         content (:content response-body)
         ;content (decode-content storage content)
         _ (info "Content Only:\n" content)
         notebook (if (nil? content)
                    nil
                    (load-notebook-hydrated content))
         _ (info "notebook: " notebook)]
     (assoc db
            :worksheet notebook
            :storage storage))))

;; SAVE File

(reg-event-db
 :save-notebook
 [standard-interceptors]
 (fn [db _]
   (if-let [storage (get-in db [:storage])]
     (dispatch [:save-storage storage]) ; just save to storage if we have a storage
     (dispatch [:app:saveas])) ;otherwise open save dialog
   db))

;; save using the storage protocol
(reg-event-fx
 :save-storage
 (fn [{:keys [db]} [_ storage]]
   (let [stype (storagetype storage)
         _ (info "notebook saving to storage " stype)
         notebook (save-notebook-hydrated (:worksheet db))
         tokens (:settings db)
         url  (str (:base-path db) "save")
         params (assoc storage
                       :storagetype stype
                       :notebook notebook
                       :tokens tokens)]
     {:db         (assoc-in db [:dialog :save] false)
      :http-xhrio {:method          :post
                   :uri             url
                   :params          params
                   :timeout         5000                     ;; optional see API docs
                   :format          (ajax/url-request-format) ; request encoding POST body url-encoded
                   :response-format (ajax/json-response-format {:keywords? true}) ;(ajax/transit-response-format) ;; response encoding TRANSIT
                   :on-success      [:after-save-success storage]
                   :on-failure      [:process-error-response]}})))


(defn hack-gist [storage result db]
  (if (and (= (:id storage) nil)
           (= :gist (storagetype storage)))
    (do
      (routes/nav! (gorilla-path (assoc storage :id (:id result))))
      (assoc-in db [:storage :id] (:id result)))
    db))

;; display success message when saving was successful
(reg-event-db
 :after-save-success
 [standard-interceptors]
 (fn [db [_ storage result]]
   (info "storage is:" storage ", result is: " result)
   (do
     (dispatch [:display-message (str "saved.") 2000])
     (hack-gist storage result db))
   ;(routes/nav! (str "/edit?source=local&filename=" filename))
   ))







