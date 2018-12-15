(ns event-map.views.event-item
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            ))

(defn event-item-view-back []
  [:div.event-item-back-button
   {:on-click #(secretary/dispatch! "/events")} "back"])

(defn event-item-view [id]
  [:div.event-item-container
   (let [event-id @(rf/subscribe [:current-event])
              event (get @(rf/subscribe [:app-events]) event-id)]
       [:div.event-item
        [:p.event-item-title (:name event)]
        [:div.event-item-metainfo
         [:p.event-item-time (take 10 (:time event))]
         [:p.event-item-place
          (/ (Math.round (* 100 (:lat event))) 100)
          " "
          (/ (Math.round (* 100 (:lon event))) 100)]]
        [:p.event-item-description (:description event)]
        ])])
