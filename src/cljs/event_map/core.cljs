(ns event-map.core
  (:require [day8.re-frame.http-fx]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [event-map.ajax :as ajax]
            [event-map.events]
            [secretary.core :as secretary])
  (:import goog.History))

(defn nav-link [uri title page]
  [:div.nav-item
   [:a
    {:href   uri
     :active (when (= page @(rf/subscribe [:page])) "active")}
    title]])

(defn nav-bar []
  (r/with-let [expanded? (r/atom true)]
    [:div.nav-bar
     [:div.nav-bar-title.nav-bar-item
      [:a {:href "#/"} "events."]]
     [:nav.nav-bar-item
      [nav-link "#/" "Map" :map]
      [nav-link "#/events" "Events" :events]
      [nav-link "#/about" "About" :about]]]))

(defn about-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

(defn map-page []
  (let [app-events @(rf/subscribe [:app-events])]
    [:div.container
     ;[:div#map {:style {:height "360px"}}]
     [:p app-events]]))

(def pages
  {:map #'map-page
   :about #'about-page})

(defn page []
  [:div
   [nav-bar]
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
