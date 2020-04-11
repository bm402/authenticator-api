# Authenticator API
[![GitHub Actions Status](https://github.com/bncrypted/authenticator-api/workflows/build/badge.svg)](https://github.com/bncrypted/authenticator-api/actions?query=workflow%3Abuild)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bncrypted_authenticator-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=bncrypted_authenticator-api)
[![Docker Pulls](https://img.shields.io/docker/pulls/bncrypted/authenticator-api)](https://hub.docker.com/r/bncrypted/authenticator-api)

A JWT-based Spring Boot authentication server for user and session token management:
- stores user credentials and associated roles
- leases and verifies session tokens based on user credentials and a signing key
- hashes passwords at rest using [bcrypt](https://en.wikipedia.org/wiki/Bcrypt)
- enforces two-factor authentication using [Google Authenticator](https://en.wikipedia.org/wiki/Google_Authenticator)

## Configuring the server

### Setting the server port

The API runs on port 8800 by default, but the port can be changed by setting the following environment variable.

```
AUTHENTICATOR_SERVER_PORT=8801
```

### Setting the data source

The API requires a PostgreSQL database to store the user credentials and associated roles. The host name, port,
username, password, and database name must be passed to the application through the following environment variables.

```
AUTHENTICATOR_DB_HOST=localhost
AUTHENTICATOR_DB_PORT=5432
AUTHENTICATOR_DB_NAME=authenticator
AUTHENTICATOR_DB_USERNAME=authuser
AUTHENTICATOR_DB_PASSWORD=authpass
```

### Configuring the JWTs

#### Setting the JWT signing key

The security of JWTs in ensured by a signature, which is created by a cryptographic hashing algorithm and a
secret signing key. The signing key must be strong and remain secret otherwise an adversary could forge the
signature and create valid JWTs with arbitrary data inside.

Your JWT signing key should be pseudo-randomly generated and should abide by the cryptographic hashing algorithm
key size recommendations. By default the API uses [HMAC-SHA512](https://en.wikipedia.org/wiki/HMAC) for JWT
signatures, so the signing key should be at least 64 bytes of [Base64](https://en.wikipedia.org/wiki/Base64)
encoded text.

The JWT signing key must be passed to the application through the following environment variable.

```
AUTHENTICATOR_JWT_SIGNING_KEY=YWdqa25hIG9ldmF3bWVuZmFzZGdodmNzeWRjYiBha2pkZm52YWVyZnYgYW5tYmZtIGthc2pkaCB2YQ==
```

#### Setting the JWT TTL

The JWT time-to-live (TTL) determines how long the token is valid for after its creation. Once the TTL has passed,
the token will be evaluated as expired.

The default TTL is set as 14400 seconds (10 hours). This value can be modified by setting the following
environment variable.

```
AUTHENTICATOR_JWT_TTL_IN_SECONDS=14401
```

#### Changing the default JWT signing algorithm

The API uses [HMAC-SHA512](https://en.wikipedia.org/wiki/HMAC) to sign the JWTs by default. However a number of
[other signature algorithms](https://tools.ietf.org/html/rfc7518#section-3) are supported which could be used
instead. Support for some of these other algorithms is in the development pipeline, but can be added manually by
creating an implementation class of the JwtHelper interface and creating an instance of that class in the
JwtHelper bean configuration.

## Running the server

Prerequisites:
- [JDK 11](https://adoptopenjdk.net/)
- [Gradle](https://gradle.org/releases/)
- [Docker](https://www.docker.com/products/docker-desktop)

### Running from source

Start a local PostgreSQL database with Docker. If using an external PostgreSQL database that is already running,
this step is not required.

```shell script
$ docker-compose up authenticator-db -d
```

Build the application with Gradle.

```shell script
$ gradle assemble
```

Run the application.

```shell script
$ gradle bootRun
```

### Running using local Docker image

Build the application using Gradle.

```shell script
$ gradle assemble
```

Start both the application and the local PostgreSQL database inside local Docker containers.

```shell script
$ docker-compose up -d --build
```

### Running using image from Docker Hub

A published Docker image of the application can be found on 
[Docker Hub](https://hub.docker.com/r/bncrypted/authenticator-api). This image can be run using the Docker pull
and Docker run commands on the command line, or it can be pulled from a Dockerfile or a docker-compose.yml file.
Note that all environment variables in this Docker image will use the default values unless they are explicitly 
set.

## Using the server

The server supports the following user management operations:
- adding user credentials (username, password and auto-generated multi-factor authentication key)
- updating user password
- regeneration of user multi-factor authentication key
- deleting user credentials

And the following session management operations:
- leasing tokens based on user credentials (username, password and time-based one-time password generated by
[Google Authenticator](https://en.wikipedia.org/wiki/Google_Authenticator) using the user multi-factor
authentication key) and JWT signing key
- verifying tokens using JWT signing key

The full API documentation can be found at the following endpoint.

```
/swagger-ui.html
```
