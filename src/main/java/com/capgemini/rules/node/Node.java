package com.capgemini.rules.node;

import com.capgemini.rules.workflow.WorkflowContext;

/**
 * This represents a single point of processing within a {@link Workflow}. Each Node will decide
 * the next Node to be executed.
 * 
 * The Node will operate on a {@link WorkflowContext} that has input and output objects with
 * generic types I and O respectively.
 */
public interface Node<I, O>
{
	
  /** Perform the business logic of this node on, or using, the context data it is supplied. */
  public Node<I, O> execute(WorkflowContext<I, O> contextData);
  
  /** A logical name for the Node */
  public String getLogicalName();
  
}
