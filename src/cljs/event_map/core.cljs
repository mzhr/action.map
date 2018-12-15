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
            [cognitect.transit :as t]
            [event-map.views :as app-views])
  (:import goog.History))

;; -------------------------
;; Routes

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:close-right-menu])
  (rf/dispatch [:navigate :about]))

(secretary/defroute "/events" []
  (rf/dispatch [:toggle-right-menu])
  (rf/dispatch [:navigate :event-list]))

(secretary/defroute "/events/:id" [id]
  (js/console.log (t/read (t/reader :json) (js/Number. id)))
  (rf/dispatch [:toggle-right-menu])
  (rf/dispatch [:navigate :event-item])
  (rf/dispatch [:set-current-event (t/read (t/reader :json) (js/Number. id))]))

(secretary/defroute "/about" []
  (rf/dispatch [:toggle-right-menu])
  (rf/dispatch [:navigate :about]))

(secretary/defroute "*" []
  (rf/dispatch [:toggle-right-menu])
  (rf/dispatch [:navigate :error]))

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
  (let [loading? @(rf/subscribe [:loading?])
        interval (atom nil)]
    (reset! interval (.setInterval
                      js/window
                      (fn []
                        (when loading?
                          (do
                            (.clearInterval js/window @interval)
                            (r/render [#'app-views/page-view] (.getElementById js/document "app")))
                          )) 500))))

(defn init! []
  (ajax/load-interceptors!)
  (rf/dispatch-sync [:fetch-app-events])
  (rf/dispatch-sync [:navigate :map])
  (rf/dispatch [:fetch-docs-about])
  (hook-browser-navigation!)
  (mount-components))
