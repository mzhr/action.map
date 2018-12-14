(ns event-map.views.map
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(defn map-view []
  [:div.map-container
   [:div#map]])

(defn map-did-mount []
  (let [map-view (.setView (.map js/L "map") #js [-37.8 144.96] 9)
        app-events (vals @(rf/subscribe [:app-events]))]
    (.addTo
     (.tileLayer js/L "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                 (clj->js {:attribution "Map data &copy; [...]"
                           :minZoom 9
                           :maxZoom 12}))
     map-view)
    (doseq [e (into [] app-events)]
      (.addTo
       (.on (.marker js/L
                     (clj->js [(:lat e) (:lon e)]))
            "click"
            (fn [m]
              (rf/dispatch [:toggle-right-menu])
              (rf/dispatch [:navigate :event-item])
              (rf/dispatch [:set-current-event (:event_id e)])))
       map-view))))

(defn map-page []
  (r/create-class {:reagent-render map-view
                   :component-did-mount map-did-mount}))
