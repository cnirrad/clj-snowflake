(ns snowflake-test
  (:require [clojure.test :refer :all]
            [snowflake :refer [create-id create-snowflake-generator parse-id]]))

(deftest generate-snowflakes
  (let [id-gen (create-snowflake-generator {})]
    (testing "ID's should always be incrementing"
      (is (< (id-gen) (id-gen) (id-gen))))


    (testing "Generation and parsing Id's should round trip."
      (is (= {:ts 123456789 :node-id 777 :sequence 1234}
            (parse-id (create-id 123456789 777 1234)))))

    (testing "ID's should always be unique"
      (is (= 50000 (count (into #{} (for [n (range 0 50000)] (id-gen)))))))))
