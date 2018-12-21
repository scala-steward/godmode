
# GodMode

Set of utilities to help with daily maintenance and operation tasks against live systems.

*Note*: the facilities presented here are not intended, nor designed, for usage in production systems. It is rather
targeted to help with tasks like connecting to multiple services, databases etc and reading / writing to them. Also
please keep in mind that eventual damages caused to your systems are your own responsibility.


### Running the tests

To run the tests, you need to have [Docker Compose](https://docs.docker.com/compose/) installed.

Tests can be run with the following command:

```bash
./sbt test
```


### To release a new version:

If you are a maintainer and want to release a new version, execute the following commands:

```bash
sbt publish
sbt bintrayRelease
```
