(ns event-map.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [markdown.core :refer [md->html]]))


(defn about-view []
  [:div.about-container
   (when-let [docs @(rf/subscribe [:docs-about])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])


(defn events-list-view []
  [:div.events-list-container
   (when-let [events (vals @(rf/subscribe [:app-events]))]
     (for [event events]
       [:div.event-list-item
        {:key (:event_id event)
         :on-click
         (fn []
           (rf/dispatch [:set-current-event (:event_id event)])
           (rf/dispatch [:navigate :event-item]))}
        [:p.event-list-item-title (:name event)]
        [:div.event-list-item-metainfo
         [:p.event-list-item-time (take 10 (:time event))]
         [:p.event-list-item-place
          (/ (Math.round (* 100 (:lat event))) 100)
          " "
          (/ (Math.round (* 100 (:lon event))) 100)]]]))])

(defn event-item-view [id]
  [:div.event-item-container
   (let [event-id @(rf/subscribe [:current-event])
              event (get @(rf/subscribe [:app-events]) event-id)]
       [:div.event-item
        [:div.event-item-back-button
         {:on-click #(rf/dispatch [:navigate :events])}]
        [:p.event-item-title (:name event)]
        [:div.event-item-metainfo
         [:p.event-item-time (take 10 (:time event))]
         [:p.event-item-place
          (/ (Math.round (* 100 (:lat event))) 100)
          " "
          (/ (Math.round (* 100 (:lon event))) 100)]]
        [:p.event-item-description (:description event)]
        ])])

(def right-menu-views
  {:about (fn [_] about-view)
   :events (fn [_] events-list-view)
   :event-item (fn [_] event-item-view)})

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

(defn map-view []
  [:div.map-container
   [:div#map]])

(defn map-did-mount []
  (let [map-view (.setView (.map js/L "map") #js [-37.8 144.96] 9)
        app-events (vals @(rf/subscribe [:app-events]))]
    (.addTo (.tileLayer js/L "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
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
      [nav-link "Events" :events]
      [nav-link "About" :about]]]))

(defn app-view []
  [:div.app-container
   [map-page]
   [right-menu-view]])

(defn page-view []
  [:div.page-container
   [nav-bar-view]
   [app-view]])
