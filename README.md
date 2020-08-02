# web-ps

A web service that returns information on the processes running on the web server as a JSON blob.

The web service will start a new listener on port 8080 which will respond `GET` requests on `/ps`.

_NOTE: This service assumes that the server it is being run on implements [procfs](https://en.wikipedia.org/wiki/Procfs). It will not run on an OS that does not (i.e. macOS)_

## Requirements

This web server has been written as a Clojure app, and compiled to a Java binary. It requires Java 1.8 to run.

## Running the web server

The web server can be run one of three ways:

### From the binary

```bash
$ java -jar <path>/<to>/<binary>.jar
```

### From source

With leiningen [installed](https://leiningen.org/#install), the web server can be started from the root of the project.

```bash
$ lein run
```

### Via Docker

The project includes a Docker container, based on centos7, which can be used to run the web server. A `make` task has been created to simplify this.

```bash
$ make docker_build
$ make docker_run
```

_NOTE: Due to the fact that the Docker container only has a single process running, the web service will *always* return an empty list_

## Testing

### Unit/Integration Tests

For running unit and integration tests, Leiningen is required. For convenience, a Vagrantfile and Puppet module has been included, which will provision a centos7 based VM, with Java and Leiningen installed.

Start the Vagrant VM and SSH in:

```bash
$ vagrant up --provider=virtualbox
...
$ vagrant ssh
```

From the folder that you land in, there are 5 separate test commands that can be run

`lein test` - Run all unit and integration tests
`lein unit` - Run just the unit tests
`lein integration` - Run just the integration tests
`lein coverage` - Generate a test coverage report via [cloverage](https://github.com/cloverage/cloverage)
`lein lint` - Static code analysis via [kibit](https://github.com/jonase/kibit)

### Manual Testing

The web server is listening on port 8080. The endpoint can be retrieved by pointing a browser at `http://localhost:8080/ps` or by running the command `curl localhost:8080/ps`

_NOTE: Both the Vagrant VM and Docker containers will expose port 8080 to localhost, allowing the manual tests to be run from the host machine_
