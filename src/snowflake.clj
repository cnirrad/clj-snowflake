(ns snowflake)

(def num-sequence-number-bits 12)
(def max-sequence-number 4095)                              ; 2^12 - 1

(def num-node-id-bits 10)
(def max-node-id 1023)                                      ; 2^10 - 1

(def default-epoch 1577836800000)                           ; 2020-01-01T00:00:00Z

(defn create-id [ts node-id sequence]
  (let [ts-bits (bit-shift-left ts (+ num-sequence-number-bits num-node-id-bits))
        node-bits (bit-shift-left node-id num-sequence-number-bits)]
    (bit-or ts-bits node-bits sequence)))

(defn updt-context [context get-time]
  (let [ts (get-time)
        curr-seq (:sequence context)
        new-seq (if (= ts (:ts context))
                  (let [new-seq (mod (inc curr-seq) max-sequence-number)]
                    (when (< new-seq curr-seq)              ; sequence rolled over, need to wait for the next ms
                        (Thread/sleep 1))
                    new-seq)
                  0)]
    (assoc context :ts (get-time) :sequence new-seq)))

(defn create-snowflake-generator [{:keys [epoch node-id get-time]
                                   :or   {epoch    default-epoch
                                          node-id  1
                                          get-time (fn [] (System/currentTimeMillis))}}]
  (if (> node-id max-node-id)
    (throw (IllegalArgumentException. (str "node-id cannot be greater than " max-node-id))))
  (let [context (atom {:ts 0 :sequence 0})]
    (fn []
      (let [{:keys [ts sequence]} (swap! context updt-context get-time)
            ts (- ts epoch)]
        (create-id ts node-id sequence)))))


(defn parse-id [id]
  (let [sequence (bit-and id 0xFFF)
        node-id (bit-shift-right (bit-and id 0x3FF000) num-sequence-number-bits)
        ts (bit-shift-right (bit-and id 0x7FFFFFFFFFC00000) (+ num-sequence-number-bits num-node-id-bits))]
    {:sequence sequence
     :node-id node-id
     :ts ts}))