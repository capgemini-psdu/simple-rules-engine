# Simple Rules Engine
A simple implementation of a rules engine that allows workflows such as UML activity diagrams to be modelled in Java.

## When to use
It is designed to work best with complex and/or hierarchical business rules that can be decomposed and expressed as a workflow e.g. as an activity diagram.

The additional complexity and programming overhead the module attracts means that it isn't suited to simple business rules (despite it being a simple rules engine!). Simple business rules may be best implemented in a standard OO or functional programming fashion instead.


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
The following is a simple example to identify a pet by its characteristics. This also demonstrates the programming overhead that this rules engine introduces over and above standard OO or functional models for very simple business rules such as this.

```
	public class Pet {
		public boolean hasFourLegs;
		public boolean mansBestFriend;
		public Pet(boolean hasFourLegs, boolean mansBestFriend){
			this.hasFourLegs = hasFourLegs;
			this.mansBestFriend = mansBestFriend;
		}
	}
	
	private Workflow<Pet, String> workflow;

	AbstractDecisionNode<Pet, String> doesItHaveFourLegsDecisionNode = new AbstractDecisionNode<Pet, String>("doesItHaveFourLegsDecisionNode") {
		@Override
		protected boolean makeDecision(WorkflowContext<Pet, String> context) {
			return context.getInputObject().hasFourLegs;
		}
	};
	
	AbstractDecisionNode<Pet, String> isThisMansBestFriendDecisionNode = new AbstractDecisionNode<Pet, String>("isThisMansBestFriendDecisionNode") {
		@Override
		protected boolean makeDecision(WorkflowContext<Pet, String> context) {
			return context.getInputObject().mansBestFriend;
		}
	}; 
	
	AbstractProcessingNode<Pet, String> catProcessNode = new AbstractProcessingNode<Pet, String>("catProcessNode") {
		@Override
		protected void performAction(WorkflowContext<Pet, String> context) {
			context.setOutputObject("Cat");
		}			
	};
	
	AbstractProcessingNode<Pet, String> dogProcessNode = new AbstractProcessingNode<Pet, String>("dogProcessNode") {
		@Override
		protected void performAction(WorkflowContext<Pet, String> context) {	
			context.setOutputObject("Dog");		
		}			
	};
	
	AbstractProcessingNode<Pet, String> goldfishProcessNode = new AbstractProcessingNode<Pet, String>("goldfishProcessNode") {
		@Override
		protected void performAction(WorkflowContext<Pet, String> context) {	
			context.setOutputObject("Goldfish");		
		}			
	};
	
	FinalNode<Pet, String> finalNode = new FinalNode<Pet, String>();
	
	doesItHaveFourLegsDecisionNode.setSuccessNode(isThisMansBestFriendDecisionNode);
	doesItHaveFourLegsDecisionNode.setFailureNode(goldfishProcessNode);
	
	isThisMansBestFriendDecisionNode.setSuccessNode(dogProcessNode);
	isThisMansBestFriendDecisionNode.setFailureNode(catProcessNode);
	
	
	goldfishProcessNode.setNextNode(finalNode);
	dogProcessNode.setNextNode(finalNode);
	catProcessNode.setNextNode(finalNode);
	
	workflow = new Workflow<Pet, String>("PetIdentifierWorkflow", doesItHaveFourLegsDecisionNode);
	WorkflowContext<Pet, String> context;

	// Test Cat		
	Pet pet = new Pet(true, false); // Has four legs but is not man's best friend
	WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>().setInputObject(pet);
	workflow.execute(context);
	assertEquals("Cat", context.getOutputObject());
```

### More examples
The tests include examples of:
* A standard workflow that includes decision and processing nodes.
* Nesting workflows within a larger worklow to achieve re-use.
* Looping over a node.
* Looping over a sub-workflow.
* Using simple Java DI.
* Using Spring DI.