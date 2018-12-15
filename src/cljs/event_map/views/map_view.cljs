(ns event-map.views.map
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(defn create-map []
  (let [map-view (.setView (.map js/L "map") #js [-37.8 145] 11)]
    (.addTo
     (.tileLayer js/L "http://b.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png" 
                 (clj->js {:attribution "<a href='https://openstreetmap.org'>OpenStreetMap</a>"
                           :minZoom 4
                           :maxZoom 19}))
     map-view)
    map-view))

(defn get-marker-icon []
  (.icon js/L
         (clj->js {
                   :iconUrl "img/marker.png",
                   :iconSize [36 36]
                   :iconAnchor [16 36]
                   :popupAnchor [2 -40]
                   :shadowUrl "img/marker-shadow.png"
                   :shadowSize [36 36]
                   :shadowAnchor [16 36]})))

(defn get-open-event-dispatch [event]
  (fn [m]
    (rf/dispatch [:toggle-right-menu])
    (rf/dispatch [:navigate :event-item])
    (rf/dispatch [:set-current-event (:event_id event)])))

(defn add-event-markers [map-view app-events]
  (js/console.log app-events)
  (doseq [event (into [] app-events)]
    (let [marker (.marker js/L (clj->js [(:lat event) (:lon event)]) (clj->js {:icon (get-marker-icon)}))]
      (.addTo (.on marker
        "click"
        (get-open-event-dispatch event))
              map-view)
      (.bindPopup marker (:name event)))))

(defn map-did-mount []
  (let [map-view (create-map)]
    (when-let [app-events (vals @(rf/subscribe [:app-events]))]
      (add-event-markers map-view app-events))))

(defn map-render []
  [:div.map-container
   [:div#map]])

(defn map-view []
  (r/create-class {:reagent-render map-render
                   :component-did-mount map-did-mount}))
