(ns event-map.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]))

;;dispatchers

(rf/reg-event-db
  :navigate
  (fn [db [_ page]]
    (assoc db :page page)))

(rf/reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

(rf/reg-event-db
 :set-app-events
 (fn [db [_ app-events]]
   (assoc db :app-events app-events)))

(rf/reg-event-fx
  :fetch-docs
  (fn [_ _]
    {:http-xhrio {:method          :get
                  :uri             "/api/docs"
                  :response-format (ajax/raw-response-format)
                  :on-success      [:set-docs]}}))

(rf/reg-event-fx
 :fetch-app-events
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             "/api/events"
                 :response-format (ajax/raw-response-format)
                 :on-success      [:set-app-events]}}))

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
  :docs
  (fn [db _]
    (:docs db)))

(rf/reg-sub
 :app-events
 (fn [db _]
   (:app-events db)))

(rf/reg-sub
  :common/error
  (fn [db _]
    (:common/error db)))
