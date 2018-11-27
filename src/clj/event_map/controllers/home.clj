(ns event-map.controllers.home
  (:require [event-map.layout :as layout]))

(defn home-get []
  (layout/render "home.html"))

