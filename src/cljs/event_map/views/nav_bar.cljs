(ns event-map.views.nav-bar
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(defn nav-link [title page]
  (let [curr-page @(rf/subscribe [:page])
        open? @(rf/subscribe [:right-menu])
        action (if
                 (and open? (= curr-page page))
                 :close-right-menu
                 :toggle-right-menu)]
    [:div.nav-item
     [:a
      {:on-click (fn []
                   (rf/dispatch [action])
                   (rf/dispatch [:navigate page]))}
      title]]))

(defn nav-bar-view []
  (r/with-let [expanded? (r/atom true)]
    [:div.nav-bar
     [:div.nav-bar-title.nav-bar-item
      [:a {:href "#/"} "events."]]
     [:nav.nav-bar-item
      [nav-link "Events" :event-list]
      [nav-link "About" :about]]]))
