package com.capgemini.rules.workflow.spring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.capgemini.rules.workflow.Workflow;
import com.capgemini.rules.workflow.WorkflowContext;
import com.capgemini.rules.workflow.WorkflowIntegrationTest.Pet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class WorkflowSpringIntegrationTest {

	@Autowired
	private Workflow<Pet, String> workflow;	
	
	@Test
	public void testCat() {		
		Pet pet = new Pet(true, false);
		WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>().setInputObject(pet).setOutputObject(new String()); // Often you might eagerly initialise this rather than rely on the workflow to do it for you
		context.setLoggingPrefix("CorrelationId: testCat"); //You might also want to set some sort of prefix to your logging e.g. to stitch together log entries for a single execution
		workflow.execute(context);
		assertEquals("Cat", context.getOutputObject());
	}
	
	@Test
	public void testDog() {		
		Pet pet = new Pet(true, true);
		WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>().setInputObject(pet).setOutputObject(new String()); // Often you might eagerly initialise this rather than rely on the workflow to do it for you
		context.setLoggingPrefix("CorrelationId: testCat"); //You might also want to set some sort of prefix to your logging e.g. to stitch together log entries for a single execution
		workflow.execute(context);
		assertEquals("Dog", context.getOutputObject());
	}
	
	@Test
	public void testGoldfish() {		
		Pet pet = new Pet(false, false);
		WorkflowContext<Pet, String> context = new WorkflowContext<Pet, String>().setInputObject(pet).setOutputObject(new String()); // Often you might eagerly initialise this rather than rely on the workflow to do it for you
		context.setLoggingPrefix("CorrelationId: testCat"); //You might also want to set some sort of prefix to your logging e.g. to stitch together log entries for a single execution
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
