# Starfish-java

> Starfish is an open-sourced developer toolkit for the data economy.
> [dex.sg](https://dex.sg)

[![Travis (.com)](https://img.shields.io/travis/com/DEX-Company/starfish-java.svg)](https://travis-ci.com/DEX-Company/starfish-java)
[![GitHub contributors](https://img.shields.io/github/contributors/DEX-Company/starfish-java.svg)](https://github.com/DEX-Company/starfish-java/graphs/contributors)

---
## Table of Contents

* [Features](#features)
* [Installation](#installation)
* [Configuration](#configuration)
  * [Using Squid-Java with Barge](#using-squid-java-with-barge)
  * [Dealing with Flowables](#dealing-with-flowables)
* [Documentation](#documentation)
* [Testing](#testing)
  * [Unit Tests](#unit-tests)
  * [Integration Tests](#integration-tests)
* [Mailing Lists](#Mailing Lists)
* [Maintainers](#Maintainers)
* [License](#license)

---

## Features

This library enables to integrate the Ocean Protocol capabilities from JVM clients.


## Installation

Typically in Maven you can add squid-java as a dependency:

```xml
<!-- https://mvnrepository.com/artifact/sg.dex/starfish-java -->
<dependency>
    <groupId>sg.dex</groupId>
    <artifactId>starfish-java</artifactId>
    <version>0.6.0</version>
</dependency>

```

Starfish-java requires Java 11 and Maven >= 3.0

## Configuration

You can configure the library using a Java Properties Object

### Using Squid-Java with Surfer

If you are using [Barge](https://github.com/oceanprotocol/barge/) for playing with the Ocean Protocol stack, you can use the following command to run the components necessary to have a fully functional environment

## Documentation

All the API documentation is hosted of javadoc.io:

- **[http://shrimp.octet.services/starfish/starfish-java/latest/apidocs/](http://shrimp.octet.services/starfish/starfish-java/latest/apidocs/)**


## Testing

You can run both, the unit and integration tests by using:

```bash
mvn clean install -P all-tests
```

### Unit Tests

You can execute the unit tests only using the following command:

```bash
mvn clean install
```

### Integration Tests

You can execute the integration tests using the following command:

```bash
mvn clean install -P integration-test
```

### All the tests

You can run the unit and integration tests running:

```bash
mvn clean install -P all-tests
```

## Mailing Lists

  * [developer@dex.sg][starfish-qa] -- General questions regarding the usage of Starfish.


## Maintainers

 [Developer Dex team][developer@dex.sg]

## License

Copyright 2018 Ocean Protocol Foundation Ltd.
Copyright 2018-2019 DEX Pte. Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[1]: http://www.apache.org/licenses/LICENSE-2.0
[2]: https://www.dex.sg/
[3]: https://github.com/DEX-Company/starfish-java
[4]: https://travis-ci.com/DEX-Company/starfish-java

