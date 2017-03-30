package com.capgemini.rules.workflow.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.capgemini.rules.node.AbstractDecisionNode;
import com.capgemini.rules.node.AbstractProcessingNode;
import com.capgemini.rules.node.FinalNode;
import com.capgemini.rules.workflow.Workflow;
import com.capgemini.rules.workflow.WorkflowContext;
import com.capgemini.rules.workflow.spring.WorkflowSpringIntegrationTest.Pet;

@Configuration
public class TestConfig {
	
	@Bean
	public AbstractDecisionNode<Pet, String> doesItHaveFourLegsDecisionNode() {
		AbstractDecisionNode<Pet, String> node = new AbstractDecisionNode<Pet, String>("doesItHaveFourLegsDecisionNode") {
			@Override
			protected boolean makeDecision(WorkflowContext<Pet, String> context) {
				return context.getInputObject().hasFourLegs;
			}
		};
		node.setSuccessNode(isThisMansBestFriendDecisionNode());
		node.setFailureNode(goldfishProcessNode());
		return node;
	}
	
	@Bean
	public AbstractDecisionNode<Pet, String> isThisMansBestFriendDecisionNode() {
		AbstractDecisionNode<Pet, String> node = new AbstractDecisionNode<Pet, String>("isThisMansBestFriendDecisionNode") {
		@Override
			protected boolean makeDecision(WorkflowContext<Pet, String> context) {
				return context.getInputObject().mansBestFriend;
			}
		};
		node.setSuccessNode(dogProcessNode());
		node.setFailureNode(catProcessNode());
		return node;
	}
	
	@Bean
	public AbstractProcessingNode<Pet, String> catProcessNode() {
		AbstractProcessingNode<Pet, String> node = new AbstractProcessingNode<Pet, String>("catProcessNode") {
			@Override
			protected void performAction(WorkflowContext<Pet, String> context) {
				context.setOutputObject("Cat");
			}		
		};
		node.setNextNode(finalNode());
		return node;
	}

	@Bean
	public AbstractProcessingNode<Pet, String> dogProcessNode() {
		AbstractProcessingNode<Pet, String> node = new AbstractProcessingNode<Pet, String>("dogProcessNode") {
			@Override
			protected void performAction(WorkflowContext<Pet, String> context) {
				context.setOutputObject("Dog");
			}
		};
		node.setNextNode(finalNode());
		return node;
	}

	@Bean
	public AbstractProcessingNode<Pet, String> goldfishProcessNode() {
		AbstractProcessingNode<Pet, String> node = new AbstractProcessingNode<Pet, String>("goldfishProcessNode") {
			@Override
			protected void performAction(WorkflowContext<Pet, String> context) {
				context.setOutputObject("Goldfish");
			}
		};
		node.setNextNode(finalNode());
		return node;
	}

	@Bean
	public FinalNode<Pet, String> finalNode() {
		return new FinalNode<Pet, String>();
	}
	
	@Bean
	public Workflow<Pet, String> workflow() {			
		return new Workflow<Pet, String>("PetIdentifierWorkflow", doesItHaveFourLegsDecisionNode());
	}
	
}
