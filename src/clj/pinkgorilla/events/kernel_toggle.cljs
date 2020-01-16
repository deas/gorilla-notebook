(ns pinkgorilla.events.kernel-toggle
  "Events related to kernel switch"
  ;; TODO : Not ready for master yet
  (:require
   [taoensso.timbre :refer-macros (info)]
   [re-frame.core :as re-frame :refer [reg-event-db]]
   ;[pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]
   [pinkgorilla.notifications :refer [add-notification notification]]))

(defn kernel-toggle [current-kernel]
  (case current-kernel
    :clj :cljs
    :clj))

(reg-event-db
 :app:kernel-toggle
 (fn [db _]
   (let [active-id (get-in db [:worksheet :active-segment])

         active-segment (get-in db [:worksheet :segments active-id])
         _ (info "active segment")
         current-kernel (:kernel active-segment)
         _ (info "multi-kernel-toggle for segment " active-id " current kernel: " current-kernel)
         new-kernel (kernel-toggle current-kernel)
         _ (info "new kernel: " new-kernel)]
     (if (nil? new-kernel) ; free segments dont have a kernel, so we wont toggle
       db
       (do
         (add-notification (notification :info (str "cell kernel is now" new-kernel)))
         (assoc-in db [:worksheet :segments active-id :kernel] new-kernel))))))



