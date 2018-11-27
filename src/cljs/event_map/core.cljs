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
            [event-map.nav :as app-nav]
            [event-map.views :as app-views])
  (:import goog.History))

(def pages
  {:map #'app-views/map-page
   :about #'app-views/about-page})

(defn page []
  [:div
   [app-nav/nav-bar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:navigate :map]))

(secretary/defroute "/about" []
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
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:navigate :map])
  (ajax/load-interceptors!)
  (rf/dispatch [:fetch-docs])
  (rf/dispatch [:fetch-app-events])
  (hook-browser-navigation!)
  (mount-components))
