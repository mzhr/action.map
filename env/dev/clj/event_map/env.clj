(ns event-map.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [event-map.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[event-map started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[event-map has shut down successfully]=-"))
   :middleware wrap-dev})
