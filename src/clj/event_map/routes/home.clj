(ns event-map.routes.home
  (:require [event-map.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [event-map.db.core :as db]
            [event-map.controllers.home :as home-controller]
            [event-map.controllers.events :as events-controller]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" []
       (home-controller/home-get))

  (GET "/api/events" []
       (events-controller/events-get))
  (GET "/api/docs/about" []
       (-> (response/ok (-> "docs/about.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8"))))

