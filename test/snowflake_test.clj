(ns snowflake-test
  (:require [clojure.test :refer :all]
            [snowflake :refer [create-snowflake-generator]]))

(deftest generate-unique-id-test
  (let [id-gen (create-snowflake-generator {})]
    (testing "ID's should always be incrementing"
      (is (< (id-gen) (id-gen) (id-gen))))))
