 (ns user
   (:require
    [clojure.repl :refer :all]
    [clojure.stacktrace :refer [print-cause-trace]]
    [clojure.string :as str]
    [com.stuartsierra.component :as component]
    [reloaded.repl :refer [system init start stop go reset clear]]
    [clik.system :as system]))

(reloaded.repl/set-init!
 system/initialize)
