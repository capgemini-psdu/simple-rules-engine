package com.capgemini.rules.workflow.spring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.capgemini.rules.workflow.Workflow;
import com.capgemini.rules.workflow.WorkflowContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class WorkflowSpringIntegrationTest {

	@Autowired
	private Workflow<Pet, String> workflow;	
	
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
