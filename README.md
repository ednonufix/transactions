# _Transaction coordinator_

The application is an example of how a transaction coordinator could be attempted. The approach is intended for a reactive environment. The way it works is as follows:

- When the call to the service is initiated, a transaction is started, in a specific thread.
- The business logics are chained and between each one of them the operations are added to the transaction.
- When all the business logics are finished, these operations are persisted in the DB by means of a commit.
- After that, the connection is closed.

## Features

- Java 21
- Spring Boot 3.3.2
- Spring Data JPA
- Spring Webflux
- Apache Derby
- Apache DBCP2
