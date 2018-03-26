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
- Transfer should be rejected if there's not enough money.
- Also don't use floats, doubles and other non-exact stuff, even in 
JSON.

## Building

This project uses gradle, but excludes wrapper from source control,
since i totally agree with [this](https://stackoverflow.com/a/42044733/2908793)
opinion. To build the project, you will need to execute following 
commands (assuming Gradle is already installed on machine):

```bash
gradle wrapper --gradle-version 4.6
gradle shadowJar
```

After that you will have shadowed jar (aka fat jar, uber jar) inside 
`build` directory.

## Launching

To launch the server, simply run `bin/console serve`. `bin/console`
is a simple bash script that ensures that jar is compiled and then
passes arguments to it.

## API

Available to inspect via `/_openapi/ui` endpoint.

In a nutshell, you can create new accounts with specific currencies,
define currency exchange rates, and then execute transfers using various
currencies.

## Testing

Tests don't bring application up by themselves, so it has to be done 
manually:

```bash
bin/console serve &
bin/console await
./gradlew :clean :integrationTest :allureReport :allureServe
```

To customize host and port one can use `mts.host` and `mts.port` system
properties:

```bash
./gradlew -Dmts.host=192.168.0.3 -Dmts.port=80 :clean :integrationTest :allureReport :allureServe
```

## Known issues

- The domain logic shouldn't throw exceptions but rather return result
objects. Sadly i didn't have enough time for that.
- Lots of constraint violation exceptions are generated manually while
they should be created from validator output. Sadly, never enough time 
again.
- Some parts of code are just little bit messy.
- Allure really kicks off with request/response attachments but oh god 
where do i get time for this.

## FAQ

### Safety guarantees

I've set isolation level to `REPEATABLE READ`, so database should block
concurrent access to entities that were touched in this transaction,
thus preventing withdrawal race for same account. Of course i don't have
time to write jepsen tests, but this should work.  

### Why H2?

- It has transactions as any other SQL database, so i could cheat out
- Task description told me to imitate persistence using in-memory 
storage, so hey here it is

### Why Vert.X / Guice / etc.?

I've never used them before and took the chance to explore.

### Everything could be done much faster if you wouldn't be polishing some corners

That's true, but hey

- I have an inner perfectionist
- I need to have a disclosure agreeable project that i can present on 
interviews

### How could that be done without using external store?

Most probably i would go with hashes surrounded by striped locks. In any
case i would have to deal with three entity updates (source account, 
target account, transfer entity), so there has to be either single 
synchronization point (which doesn't scale well, but can easily 
guarantee operation atomicity in a single-node system) or some mechanism
to synchronize around specific entities (hence striped locks). So i 
would have a striped lock pool somewhere in application, and whenever i
would need to do a commit, i would pull out necessary amount of locks
(one for each entity), sort them by their hashcode* to prevent deadlocks
and then try lock every of them in that order in a loop.

*This, actually, is kinda tricky part. Nobody guarantees that hashcodes
wouldn't collide, so *real* safety would be guaranteed either by sorting 
by lock memory address (whoa whoa) or by somehow sequentially numbering 
every lock.

All of the considerations specified above won't work for distributed 
system, of course.

### So how would you do this in distributed system?

I would go with event sourcing pattern. First of all, we're already in
a financial operations context which cries loudly for ES, and secondly 
it gives us entity update atomicity guarantees out of the box. The 
algorithm would become much more complex and time-consuming, but it 
should work against nearly any existing data store, and migration from 
(e.g.) MySQL to Cassandra wouldn't be as painful as it gets in regular
applications.

So, the algorithm would look like that:

- Attach a pending transfer to account A
- Return transfer id to client
- Create transfer entity
- Move account A transfer amount from 'balance' to 'hold' with a 
reference to specific transfer. If there's not enough funds, mark
pending transfer as cancelled, mark transfer entity as cancelled, then 
remove pending transfer from account A.
- Put funds into account B 'hold' section with reference to specific
transfer
- Remove funds from account A 'hold' section and remove pending
transfer
- Move funds from account B 'hold' section to 'balance section'
- Mark transfer entity as completed

If application has some kind of background scanners, it can pick this 
algorithm from any step and continue even if originating node has 
crashed.

I don't promise that this algorithm is flawless (i could miss some 
points that would allow to progress differently starting from different
steps), but in a nutshell it would look like this.

## Dev branch cellar

[![CircleCI/dev](https://img.shields.io/circleci/project/github/etki/revolving-skill-check/dev.svg?style=flat-square)](https://circleci.com/gh/etki/java-event-sourcing/tree/dev)
[![Scrutinizer/dev](https://img.shields.io/scrutinizer/g/etki/revolving-skill-check/dev.svg?style=flat-square)](https://scrutinizer-ci.com/g/etki/revolving-skill-check/?branch=dev)

## Licensing

I'm not sure anyone will ever need this, but:

MIT / Etki / 2018
