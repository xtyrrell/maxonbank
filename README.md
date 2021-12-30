 # MaxonBank
MaxonBank is a Kotlin + Spring Boot + Axon Framework application that supports opening, depositing to, and withdrawing from accounts.

We use the terminology as described in the [Ubiquitous Language Notion document](https://www.notion.so/Ubiquitous-Language-51b1115c4f42410fb2bd10e52548b1ad).

The application is structured around the CQRS pattern. It has the following packages:
- `coreapi` defines the basic shared interfaces: the commands, events, exceptions, and queries that are then used by the different parts of the application
- `command` defines each Aggregate model with all of its command handlers, each applying its relevant business logic before publishing the relevant event
- `query` defines a view and repository, which wraps the data and persistence of the model, as well as a projector, which maps the model's events to state changes in the repository in its event handlers, and also handles queries by serving relevant data from the repository. 
- `web` is responsible for the web interface to the application's functionality. It exposes endpoints to query and operate on the application's data through a RESTful interface. These endpoints hide the mechanics of the application's architecture, so consumers aren't required to know about CQRS, DDD, Axon Framework, etc.

## Getting started

To run this application, you'll need
- java 17
- a running Axon Server -- you can get it using docker or as a JAR


## Todos
- [x] Maintain a `totalBalance` value instead of calculating one on the fly 
- [x] Add timestamps to LedgerEntries
- [ ] Add tests (naughty me, I should have done this earlier :| )
  - [x] test Account commands (src/test/kotlin/com/xtyrrell/maxonbank/command/AccountTests.kt)
  - [ ] test Account queries
    - This might a fair bit of tinkering work to get Axon integrations mocked or somehow test-ready. Obviously this would be invaluable for a production app, but I'm going to leave it for now.
  - [ ] test AccountsController
    - This also needs a bit of work to get set up. We'd want fixture data (sample Accounts and linked LedgerEntries) to work with in tests. We'd want this fixture data to be reset for each test. I'm going to leave this for now.

## Notes
- Traditional architecture has a service layer in between the interface layer (AccountsController for the web interface) and the data layer. Maxonbank does not have this, as Axon's queryGateway and commandGateway feel sufficiently abstracted, and I don't see any clear benefit to doing so. However, we could still consider this.
- I think I should try to take the timestamps out of LedgerEntryView and put them instead inside the messages that prompt LedgerEntry creation, which would be a more accurate record of ledger entry timestamps.
- Is there something we can do about having to use `var` so much instead of `val` (such as in Spring Data JPA entities)?
- A lot of state is stored in the database instead of being event sourced. What are the tradeoffs of this? Would it be better to source all or more state from events? For example, the Account projection (query/AccountProjection.kt) stores all state in an AccountView Hibernate / Spring Data JPA @Entity.
  - It makes sense to me that storing unbounded data (like an account's ledger entries) in a DB is better than having it event-sourced for memory reasons. However, other data like balances I believe would work just fine with an event-sourced approach.
