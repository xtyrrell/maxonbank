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
- [ ] Maintain a `totalBalance` value instead of calculating one on the fly 
- [ ] Add timestamps to LedgerEntries
- [ ] See if there's a way to generate account IDs inside the Command instead of requiring the command sender to generate an ID and then pass it in the command (this is poor encapsulation)
