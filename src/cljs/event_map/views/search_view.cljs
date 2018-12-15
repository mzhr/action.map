(ns event-map.views.search
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(defn search-view []
  [:div.search-container
   [:input.search-bar
    {:placeholder "search..."
     :type "text"
     :value @(rf/subscribe [:event-search-filter])
     :on-change #(rf/dispatch [:set-event-search-filter (-> % .-target .-value)])}]])
