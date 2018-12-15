(ns event-map.views.right-menu
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [event-map.views.about :as about]
            [event-map.views.event-list :as event-list]
            [event-map.views.event-item :as event-item]
            [event-map.views.error :as error]))

(def right-menu-views
  {:about {:view (fn [_] about/about-view) :back [:a]}
   :event-list {:view (fn [_] event-list/event-list-view) :back [:a]}
   :error {:view (fn [_] error/error-view) :back [:a]}
   :event-item {:view (fn [_] event-item/event-item-view) :back (event-item/event-item-view-back)}})

(defn menu-content []
  (let [content @(rf/subscribe [:page])]
    [:div.right-menu
     [:div.right-menu-nav
      (:back (content right-menu-views))
      [:a.close-button
       {:href "#/"} "close"]]
     [:div.right-menu-content
      [(:view (content right-menu-views))]]]))

(defn right-menu-view []
   (let [open @(rf/subscribe [:right-menu])]
     (if open (menu-content) [:div.right-menu-removing])))
