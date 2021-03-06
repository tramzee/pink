;; Test of Events 

(ns pink.demo.demo7
  (:require [pink.engine :refer :all]
            [pink.envelopes :refer [env exp-env adsr xar]]
            [pink.oscillators :refer [oscil sine2]]
            [pink.util :refer [mul swapd! sum const create-buffer getd setd! arg shared let-s reader]]
            [pink.event :refer :all] ))


(defn table-synth [freq]
  (mul
     (oscil 0.5 freq)
     (env [0.0 0.0 0.05 2 0.02 1.5 0.2 1.5 0.2 0])))

(comment

  (let [e (engine-create)
        eng-events 
        (audio-events e
                       (event table-synth 0.0 440.0) 
                       (event table-synth 0.5 550.0) 
                       (event table-synth 1.0 660.0) 
                       (event table-synth 1.5 880.0))
        ]
    
      (engine-start e)
      (engine-add-events e eng-events)

      (Thread/sleep 2200)
      (engine-stop e)
      (engine-clear e))


  )

