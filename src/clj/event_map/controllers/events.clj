(ns event-map.controllers.events
  (:require [event-map.db.core :as db]))

(defn events-get []
  {:body (map #(dissoc % :_id) (db/get-all-events))})

