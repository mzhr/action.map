(ns event-map.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [markdown.core :refer [md->html]]
            [event-map.views.nav-bar :as nav-bar]
            [event-map.views.right-menu :as right-menu]
            [event-map.views.map :as map]))

(defn app-view []
  [:div.app-container
   [map/map-view]
   [right-menu/right-menu-view]])

(defn page-view []
  [:div.page-container
   [nav-bar/nav-bar-view]
   [app-view]])
