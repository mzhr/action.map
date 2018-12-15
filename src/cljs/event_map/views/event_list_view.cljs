(ns event-map.views.event-list
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [event-map.views.search :as search]))


(defn filter-events [filter-text events]
  (let [filter-pattern (re-pattern (if (nil? filter-text) "" (clojure.string/lower-case filter-text)))]
    (filter #(not (nil? (re-find filter-pattern (clojure.string/lower-case (:name %))))) events)))

(defn event-list-view []
  [:div.event-list-container
   (search/search-view)
   (let [events-all (vals @(rf/subscribe [:app-events]))
         search-filter @(rf/subscribe [:event-search-filter])
         events (filter-events search-filter events-all)]
     (if (empty? events)
       [:div.event-list-empty
        [:h3 "nothing matches.."]]
       (for [event events]
         [:div.event-list-item {:key (:event_id event)}
          [:a.event-list-item-select {:href (str "#/events/" (:event_id event))}
        [:p.event-list-item-title (:name event)]
        [:div.event-list-item-metainfo
         [:p.event-list-item-time (take 10 (:time event))]
         [:p.event-list-item-place
          (/ (Math.round (* 100 (:lat event))) 100)
          " "
          (/ (Math.round (* 100 (:lon event))) 100)]]]])))])
