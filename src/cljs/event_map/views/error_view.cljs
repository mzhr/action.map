(ns event-map.views.error
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(defn error-view []
  [:div.error-container
   [:h3.error-text "There's nothing here..."]])

