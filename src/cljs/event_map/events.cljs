(ns event-map.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [secretary.core :as secretary]
            [cognitect.transit :as t]))

;;dispatchers
(rf/reg-event-db
  :navigate
  (fn [db [_ page]]
    (assoc db :page page)))

(rf/reg-event-db
  :set-docs-about
  (fn [db [_ about]]
    (assoc db :docs-about about)))

(rf/reg-event-db
 :set-current-event
 (fn [db [_ id]]
   (assoc db :current-event id)))

(rf/reg-event-db
 :set-event-search-filter
 (fn [db [_ filter]]
   (assoc db :event-search-filter filter)))

(rf/reg-event-db
 :toggle-right-menu
 (fn [db [_ ]]
   (assoc db :right-menu true)))

(rf/reg-event-db
 :close-right-menu
 (fn [db [_ ]]
   (assoc db :right-menu false)))

(defn reformat-event [event]
  (zipmap (map #(keyword %) (keys event)) (vals event)))

(defn reformat-events [events]
  (zipmap (map #(:event_id %) events) events))

(rf/reg-event-db
 :set-app-events
 (fn [db [_ app-events]]
   (assoc db
          :app-events
          (reformat-events (map reformat-event
                                (t/read (t/reader :json) app-events)))
          :loading? false)))

(rf/reg-event-fx
  :fetch-docs-about
  (fn [_ _]
    {:http-xhrio {:method          :get
                  :uri             "/api/docs/about"
                  :response-format (ajax/raw-response-format)
                  :on-success      [:set-docs-about]}}))

(rf/reg-event-fx
 :fetch-app-events
 (fn [{db :db} _]
   {:http-xhrio {:method          :get
                 :uri             "/api/events"
                 :response-format (ajax/raw-response-format)
                 :on-success      [:set-app-events]}
    :db  (assoc db :loading? true)}))

(rf/reg-event-db
  :common/set-error
  (fn [db [_ error]]
    (assoc db :common/error error)))

;;subscriptions
(rf/reg-sub
  :page
  (fn [db _]
    (:page db)))

(rf/reg-sub
  :docs-about
  (fn [db _]
    (:docs-about db)))

(rf/reg-sub
 :right-menu
 (fn [db _]
   (:right-menu db)))

(rf/reg-sub
 :app-events
 (fn [db _]
   (:app-events db)))

(rf/reg-sub
 :loading?
 (fn [db _]
   (:loading? db)))

(rf/reg-sub
 :event-search-filter
 (fn [db _]
   (:event-search-filter db)))

(rf/reg-sub
 :current-event
 (fn [db _]
   (:current-event db)))

(rf/reg-sub
  :common/error
  (fn [db _]
    (:common/error db)))
