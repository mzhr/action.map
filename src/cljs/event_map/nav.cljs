(ns event-map.nav
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

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
