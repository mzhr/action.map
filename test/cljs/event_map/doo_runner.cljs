(ns event-map.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [event-map.core-test]))

(doo-tests 'event-map.core-test)

