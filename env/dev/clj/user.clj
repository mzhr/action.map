(ns user
  (:require [event-map.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [event-map.figwheel :refer [start-fw stop-fw cljs]]
            [event-map.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'event-map.core/repl-server))

(defn stop []
  (mount/stop-except #'event-map.core/repl-server))

(defn restart []
  (stop)
  (start))


