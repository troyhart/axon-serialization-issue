# Axon Serialization Issue

NOTE: even though it turns out the axon issue was really mine, this still may be a good
reference for getting started in axon. I have heavily used kotlin for data classes (value objects)
and also for some of the services.

This project was created to demonstrate an issue with axon serialization/deserialization. 
When I started creating milestone events I found the need for more complex objects to
show up in the event payload. Naturally I used kotlin extensively on my models and I
had figured this was a culprit.  

I have created a test ([TestSerialization.java](src/test/java/com/example/axonserializationissue/query/TestSerialization.java))
that demonstrates an issue with Serialization and this is the source of my suspicions.
However, as I was working through this demo I found a bug in my code related to the
issue. When I fixed this bug, the issue that I was blaming on axon cleared up. I think
that the framework needs to do a better job about logging. When I had some bad annotations
axon was silently failing and redelivering my milestone event endlessly.

## Using this project

This project is 100% containerized. It includes a [bash build script](build.sh) that triggers
a maven build script and then builds a docker image from the source code. All the required
infrastructure is defined in [docker-compose.yml](docker-compose.yml) and the environment
is defined in [.env](.env).

You do not need java on your system to build and test this project. The only requirements 
are docker and docker compose. After executing the build all you need to do is launch the
project with a simple `docker-compose` command:

```bash
$ docker-compose up -d
```

Of source the above command must be executed from the root of the project (where the 
`build.sh` script is located).

### Swagger

This project uses swagger to document the rest API. When you have the project deployed, 
you can access the live documentation at:

```
http://localhost:8081/swagger-ui.html
```
