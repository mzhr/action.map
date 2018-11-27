(ns event-map.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]
              [mount.core :refer [defstate]]
              [event-map.config :refer [env]]))

(defstate db*
  :start (-> env :database-url mg/connect-via-uri)
  :stop (-> db* :conn mg/disconnect))

(defstate db
  :start (:db db*))

(defn create-event [event]
  (mc/insert db "event_map" event))

(defn update-event [id title description lat lon etime]
  (mc/update db "event_map" {:_id id}
             {$set {:title title
                    :description description
                    :lat lat
                    :lon lon
                    :etime etime}}))

(defn get-event [id]
  (mc/find-one-as-map db "event_map" {:_id id}))

(defn get-all-events []
  (mc/find-maps db "event_map"))
