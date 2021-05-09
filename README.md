# snowflake

This is a Clojure implementation of Twitter's Snowflake ID's, which generate unique 64-bit ID's in a distributed manner.
The ID's generated are roughly time sortable and made up of a 41 bit timestamp, 10 bit node ID, and a 12 bit sequence number.

## Usage

Create one ID generator per server:

    (defonce id-gen (create-snowflake-generator {:node-id 5}))

The node-id parameter should be unique for each instance of your application.

## Development

Invoke a library API function from the command-line:

    $ clojure -X cnirrad.snowflake/foo :a 1 :b '"two"'
    {:a 1, :b "two"} "Hello, World!"

Run the project's tests (they'll fail until you edit them):

    $ clojure -M:test:runner

Build a deployable jar of this library:

    $ clojure -X:jar

This will update the generated `pom.xml` file to keep the dependencies synchronized with
your `deps.edn` file. You can update the version (and SCM tag) information in the `pom.xml` using the
`:version` argument:

    $ clojure -X:jar :version '"1.2.3"'

Install it locally (requires the `pom.xml` file):

    $ clojure -X:install

Deploy it to Clojars -- needs `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` environment
variables (requires the `pom.xml` file):

    $ clojure -X:deploy

Your library will be deployed to net.clojars.cnirrad/snowflake on clojars.org by default.

If you don't plan to install/deploy the library, you can remove the
`pom.xml` file but you will also need to remove `:sync-pom true` from the `deps.edn`
file (in the `:exec-args` for `depstar`).

## License

Copyright © 2021 Darrin Collins

Distributed under the Eclipse Public License version 1.0.
