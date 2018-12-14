(ns event-map.views.right-menu
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [event-map.views.about :as about]
            [event-map.views.event-list :as event-list]
            [event-map.views.event-item :as event-item]))

(def right-menu-views
  {:about (fn [_] about/about-view)
   :event-list (fn [_] event-list/event-list-view)
   :event-item (fn [_] event-item/event-item-view)})

(defn right-menu-view []
  (when-let [open @(rf/subscribe [:right-menu])]
    (if open
      (let [content @(rf/subscribe [:page])]
        [:div.right-menu
         [:div.close-button
          {:on-click (fn [] (rf/dispatch [:close-right-menu]))}]
         [(content right-menu-views)]
         ])
      [])))
