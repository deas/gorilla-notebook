(ns pinkgorilla.notebook-ui.app.pages.explorer
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [webly.web.handler :refer [reagent-page]]
   [pinkgorilla.document.collection.component :refer [notebook-explorer]]
   [pinkgorilla.explorer.bidi :refer [goto-notebook!]]
   [pinkgorilla.notebook-ui.app.site :as site]))

;; EXPLORER

(defn open-notebook [nb]
  (info "load-notebook-click" nb)
  (goto-notebook! (:storage nb)))

(defmethod reagent-page :ui/explorer [{:keys [route-params query-params handler] :as route}]
  [:<>
   [site/header]
   [notebook-explorer open-notebook]])
