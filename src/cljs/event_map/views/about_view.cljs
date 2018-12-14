(ns event-map.views.about
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [markdown.core :refer [md->html]]))

(defn about-view []
  [:div.about-container
   (when-let [docs @(rf/subscribe [:docs-about])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

