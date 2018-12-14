(ns event-map.views.event-list
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(defn event-list-view []
  [:div.event-list-container
   (when-let [events (vals @(rf/subscribe [:app-events]))]
     (for [event events]
       [:div.event-list-item
        {:key (:event_id event)
         :on-click
         (fn []
           (rf/dispatch [:set-current-event (:event_id event)])
           (rf/dispatch [:navigate :event-item]))}
        [:p.event-list-item-title (:name event)]
        [:div.event-list-item-metainfo
         [:p.event-list-item-time (take 10 (:time event))]
         [:p.event-list-item-place
          (/ (Math.round (* 100 (:lat event))) 100)
          " "
          (/ (Math.round (* 100 (:lon event))) 100)]]]))])
