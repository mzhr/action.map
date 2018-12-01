(ns event-map.core
  (:require [day8.re-frame.http-fx]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [event-map.ajax :as ajax]
            [event-map.events]
            [secretary.core :as secretary]
            [event-map.views :as app-views])
  (:import goog.History))

;; -------------------------
;; Routes

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:navigate :about]))

(secretary/defroute "/events" []
  (rf/dispatch [:toggle-right-menu])
  (rf/dispatch [:navigate :events]))

(secretary/defroute "/events/:id" [id]
  (rf/dispatch [:toggle-right-menu])
  (rf/dispatch [:navigate :event-item])
  (rf/dispatch [:set-current-event id]))

(secretary/defroute "/about" []
  (rf/dispatch [:toggle-right-menu])
  (rf/dispatch [:navigate :about]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'app-views/page-view] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:navigate :map])
  (ajax/load-interceptors!)
  (rf/dispatch [:fetch-docs-about])
  (rf/dispatch [:fetch-app-events])
  (hook-browser-navigation!)
  (mount-components))
