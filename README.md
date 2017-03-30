# Simple Rules Engine
A simple implementation of a rules engine that allows e.g. UML activity diagrams to be modelled in Java.

## When to use
It is designed to work best with complex business rules that can be decomposed and expressed as an activity diagram.

The additional complexity and programming overhead the module attracts means that it isn't suited to simple business rules (despite it being a simple rules engine!). Simple business rules may be best implemented in a standard imperitive programming fashion instead.


## Concepts
### Workflow
A Workflow can be used to represent the business rules represented by a UML activity diagram.

It is a graph of processes and decisions that are wired together to create the whole workflow through which some data may be pushed.

### Node
Each process and decision is modelled independently as a Node and the workflow is created by wiring together these nodes.

In fact the workflow itself is also a node - this allows us to nest workflows within other workflows and to integration test at any level we choose.

### WorkflowContext
Some sort of data must be pushed through the workflow and the rules within it may apply updates to external resources or, more commonly, return some data to the calling process.

The input and output data is modelled as a WorkflowContext. This is a thin type-safe wrapper around an input and output object.

## Wiring
As the processes and decisions are decomposed into individual nodes they must be wired together to produce the overall workflow.

This is best done using some form of dependency injection. Simple Java DI using the `new` operator may suffice.

Alternatively a specialist DI library such as Spring or Guice may be preferred.

## Examples
The best examples should be those in the integration tests.

The tests include examples of:
* A standard workflow that includes decision and processing nodes.
* Nesting workflows within a larger worklow to achieve re-use.
* Looping over a node.
* Looping over a sub-workflow.
* Using simple Java DI.
* Using Spring DI.