(ns event-map.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [markdown.core :refer [md->html]]))

(defn about-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

(defn map-view []
  [:div.container
   [:div#map {:style {:height "360px"}}]])

(defn map-did-mount []
  (let [map-view (.setView (.map js/L "map") #js [-37.8 144.96] 9)
        app-events @(rf/subscribe [:app-events])]
    (.addTo (.tileLayer js/L "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :minZoom 9
                                  :maxZoom 12}))
            map-view)
    (doseq [e (into [] app-events)]
      (.addTo
        (.marker js/L
                 (clj->js [(get e "lat") (get e "lon")]))
        map-view))))

(defn map-page []
  (r/create-class {:reagent-render map-view
                   :component-did-mount map-did-mount}))
