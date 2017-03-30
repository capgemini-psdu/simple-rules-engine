package com.capgemini.rules.workflow;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.rules.node.AbstractDecisionNode;
import com.capgemini.rules.node.AbstractProcessingNode;
import com.capgemini.rules.node.FinalNode;
import com.capgemini.rules.workflow.Workflow;
import com.capgemini.rules.workflow.WorkflowContext;

public class WorkflowIntegrationTest {

	private Workflow<Pet, String> workflow;
	
	@Before
	public void setup() {

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
	}
	
	@Test
	public void testCat() {		
		WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>();
		Pet pet = new Pet();
		pet.hasFourLegs = true;
		pet.mansBestFriend = false;
		context.setInputObject(pet);
		context.setOutputObject(new String()); // Often you might eagerly initialise this rather than rely on the workflow to do it for you
		context.setLoggingPrefix("CorrelationId: testCat");
		workflow.execute(context);
		assertEquals("Cat", context.getOutputObject());
	}
	
	@Test
	public void testDog() {		
		WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>();
		Pet pet = new Pet();
		pet.hasFourLegs = true;
		pet.mansBestFriend = true;
		context.setInputObject(pet);
		context.setOutputObject(null); // Often you might eagerly initialise this rather than rely on the workflow to do it for you
		context.setLoggingPrefix("CorrelationId: testDog");
		workflow.execute(context);
		assertEquals("Dog", context.getOutputObject());
	}
	
	@Test
	public void testGoldfish() {		
		WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>();
		Pet pet = new Pet();
		pet.hasFourLegs = false;
		pet.mansBestFriend = false;
		context.setInputObject(pet);
		context.setOutputObject(null); // Often you might eagerly initialise this rather than rely on the workflow to do it for you
		context.setLoggingPrefix("CorrelationId: testGoldfish");
		workflow.execute(context);
		assertEquals("Goldfish", context.getOutputObject());
	}
	
	public class Pet {
		public boolean hasFourLegs;
		public boolean mansBestFriend;
	}
}
