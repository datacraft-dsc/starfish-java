# Starfish-java

>Starfish is an open-sourced developer toolkit for the data economy. The toolkit allows developers, data scientists and enterprises to create, integrate and manage data supply lines through standardised and simple-to-use APIs. 

>[Developer site](https://developer.dex.sg/)

[![Travis (.com)](https://img.shields.io/travis/com/DEX-Company/starfish-java.svg)](https://travis-ci.com/DEX-Company/starfish-java)
[![GitHub contributors](https://img.shields.io/github/contributors/DEX-Company/starfish-java.svg)](https://github.com/DEX-Company/starfish-java/graphs/contributors)

---
## Table of Contents

* [Features](#features)
* [Installation](#installation)
* [Configuration](#configuration)
  * [Using Starfish-Java with Surfer](#using-squid-java-with-surfer)
* [Documentation](#documentation)
* [Testing](#testing)
  * [Unit Tests](#unit-tests)
  * [Integration Tests](#integration-tests)
* [Maintainers](#Maintainers)
* [License](#license)

---

## Features

This is developer toolkit for the data economy. Based on an underlying data ecosystem standard, Starfish provides high-level APIs for common tasks within the data economy, for example, registering/publishing an asset, for subsequent use in a data supply line. In this case, an asset can be any data set, model or data service.

Starfish works with blockchain networks, such as Ocean Protocol, and common web services through agents, allowing unprecedented flexibility in asset discovery and data supply line management.

### Easiest Way to Build and Manage Data Supply Lines
Starfish provides a common abstraction to enable decentralised data infrastructure to interoperate effectively, allowing data supply lines to be easily created and managed using standardised interface. 

### Data Sharing Made Simple for Everyone
Any existing data resources can be "packaged" into a Data Asset, allowing data exchange possible with any data ecosystem participants. 

### Empowers Innovative Application in the Space of Data
There is no practical limit to the types of operations that can be created, and potentially recombined in interesting ways to create novel data solutions. Orchestration of such operations with Starfish is a perfect way to facilitate rapid innovation in data and AI solutions, especially where these solutions must orchestrate data and services.


---
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

### Using Starfish-Java with Surfer

If you are using [Surfer](https://github.com/DEX-Company/surfer/) for playing with the Starfish , you can refer the developer testcase (https://github.com/DEX-Company/starfish-java/tree/develop/src/test/java/sg/dex/starfish/integration/developerTC)

## Documentation

All the API documentation is hosted of javadoc.io:
- **[https://dex-company.github.io/starfish-java/docs](https://dex-company.github.io/starfish-java/apidocs/)**

## Code Coverage
Code Coverage:
- **[https://dex-company.github.io/starfish-java/coverage](https://dex-company.github.io/starfish-java/jacoco/)**

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

