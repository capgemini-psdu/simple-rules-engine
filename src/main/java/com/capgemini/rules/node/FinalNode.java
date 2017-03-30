package com.capgemini.rules.node;

import com.capgemini.rules.workflow.WorkflowContext;

/**
 * A no-op {@link Node} that signifies the end of the flow. 
 */
public class FinalNode<I, O> implements Node<I, O> {
	
	/** {@inheritDoc} */
	@Override
	public final Node<I, O> execute(WorkflowContext<I, O> context) {
		return null;		
	}

	/** {@inheritDoc} */
	@Override
	public String getLogicalName() {
		return "FinalState";
	}
}
