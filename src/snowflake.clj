(ns snowflake)

(def max-sequence-number 4095)                              ; 2^12 - 1
(def max-node-id 1023)                                      ; 2^10 - 1


(defn updt-context [context get-time]
  (let [ts (get-time)
        curr-seq (:sequence context)
        new-seq (if (= ts (:ts context))
                  (let [new-seq (mod (inc curr-seq) max-sequence-number)]
                    (when (= new-seq 0)
                      (Thread/sleep 1))
                    new-seq)
                  0)]
    (assoc context :ts (get-time) :sequence new-seq)))

(defn create-snowflake-generator [{:keys [epoch node-id get-time]
                                   :or   {epoch    1451606400000
                                          node-id  1
                                          get-time (fn [] (System/currentTimeMillis))}}]
  ; TODO validate inputs
  (let [context (atom {:ts 0 :sequence 0})]
    (fn []
      (let [{:keys [ts sequence]} (swap! context updt-context get-time)
            ts-bits (bit-shift-left (- ts epoch) 22)
            node-bits (bit-shift-left node-id 12)]
        (bit-or ts-bits node-bits sequence)))))