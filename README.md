# FSM Mapper

Created clojure projects from FSM (finite state machine) descriptions in `edn`
format.

## Installation

Run `lein install` before use.
## Usage

Run : explanation

```sh
    $ lein run --file specs/count_fsm.edn 
```

Will generate a project in `out` directory with the code generated in clojure.



## Options

In order to visualize the FSM from specs/ direcory

```sh
    $ lein run --file specs/count_fsm.edn --show count_fsm
```
The show flag should have the same name as the `edn` file without extension
since the current implementation names generated namespaces based on file names.


## Running generated project

Once the project has been generated, change the directory to `out/[project-name]`
and run :
```sh
    $ lein run
```

this should run the project.


## Bugs
A very limited proof of concept.



Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
