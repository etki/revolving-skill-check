# Test task for R company

[![CircleCI/master](https://img.shields.io/circleci/project/github/etki/revolving-skill-check/master.svg?style=flat-square)](https://circleci.com/gh/etki/java-event-sourcing/tree/master)
[![Scrutinizer/master](https://img.shields.io/scrutinizer/g/etki/revolving-skill-check/master.svg?style=flat-square)](https://scrutinizer-ci.com/g/etki/revolving-skill-check/?branch=master)

This repository contains test task implementation for secret R company.

The task itself mandated following requirements:

- Single jar packed RESTful API service for money transfers
- Simple and to the point
- Anything but Spring
- With e2e tests

So implicit requirements are

- Money transfer is an atomic operation, so mind your isolation levels
if using SQL-alike database and choose your strategy wisely if using any 
other store where operation atomicity doesn't span across entities.
- Also don't use floats, doubles and other non-exact stuff.

## Building

This project uses gradle, but excludes wrapper from source control,
since i totally agree with [this](https://stackoverflow.com/a/42044733/2908793)
opinion. To build the project, you will need to execute following 
commands (assuming Gradle is already installed on machine):

```bash
gradle wrapper --gradle-version 3.3
gradle shadowJar
```

After that you will have shadowed jar (aka fat jar, uber jar) inside 
`build` directory.

## Launching

Built jar has three commands: `serve`, `await` and `test`. `serve`
brings up internal server on specified port (8080 by default), `await`
waits until it is up (so tests could be run), `test` runs built-in 
tests against provided address.

## API

Available to inspect via `/_swagger/ui` endpoint.

## FAQ

### Why H2?

- It has transactions as any other SQL database, so i could cheat out
- Task description told me to imitate persistence using in-memory 
storage, so hey here it is

### Why Vert.X?

I've never used it before and took the chance to explore.

### What could be done better?

Given the context (financial operations), everything should be done
using event sourcing pattern. This, however, would rise the cost of 
whole thing, also a sophisticated algorithm would have to be 
implemented - a transfer should first be registered on one entity,
then applied on other one, then cleared from first entity, then cleared
from second entity, everything with idempotency and inevitable progress 
guarantees, and all this stuff would also require some background 
scanner which would help application to recover from crashes - i would 
love to implement all of this, but seriously i just don't have that much
time.

## Dev branch cellar

[![CircleCI/dev](https://img.shields.io/circleci/project/github/etki/revolving-skill-check/dev.svg?style=flat-square)](https://circleci.com/gh/etki/java-event-sourcing/tree/dev)
[![Scrutinizer/dev](https://img.shields.io/scrutinizer/g/etki/revolving-skill-check/dev.svg?style=flat-square)](https://scrutinizer-ci.com/g/etki/revolving-skill-check/?branch=dev)

## Licensing

I'm not sure everyone will ever need this, but:

MIT / Etki / 2018
