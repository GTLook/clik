# CLIK (Command Line Interfacing Kanban)

Clik is a command line tool for organizing your tasks list on a local machine. It is structured as a RESTfull API and outputs JSON.  

```shell
Paths:

Get :8080/help
Get :8080/tasks/
Get :8080/tasks/:id
Post  :8080/tasks  (title) (note) (team) (*optional* lane)
Patch :8080/tasks/:id  (title) (note) (team) (lane)
Delete :8080/tasks/:id
```

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```shell
* fork and clone this repository
* lein repl
```

### Lein WebServer commands

Run commands for starting and stopping the local webserver

```shell
* (go)
* (reset)
* (stop)
```

## Built With

* [Clojure](https://clojure.org/) - The language
* [Cheshire](https://github.com/dakrone/cheshire) - JSON encoder
* [Ring](https://github.com/ring-clojure) - HTTP Server 


## Contributing

Please send me a message for details on our code of conduct, and the process for submitting pull requests.

## Authors

* **Gavin Look** - *Initial work* - [GTLook](https://github.com/GTLook/)

## License

This project is licensed under the MIT License

## Acknowledgments

* Thanks Greg!
