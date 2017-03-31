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
		Pet pet = new Pet(true, false);
		WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>().setInputObject(pet);
		workflow.execute(context);
		assertEquals("Cat", context.getOutputObject());
	}
	
	@Test
	public void testDog() {		
		Pet pet = new Pet(true, true);
		WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>().setInputObject(pet);
		workflow.execute(context);
		assertEquals("Dog", context.getOutputObject());
	}
	
	@Test
	public void testGoldfish() {		
		Pet pet = new Pet(false, false);
		WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>().setInputObject(pet);
		workflow.execute(context);
		assertEquals("Goldfish", context.getOutputObject());
	}
	
	public class Pet {
		public boolean hasFourLegs;
		public boolean mansBestFriend;
		public Pet(boolean hasFourLegs, boolean mansBestFriend){
			this.hasFourLegs = hasFourLegs;
			this.mansBestFriend = mansBestFriend;
		}
	}
}
