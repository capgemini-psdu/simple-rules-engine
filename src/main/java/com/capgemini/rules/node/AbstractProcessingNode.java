package com.capgemini.rules.node;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capgemini.rules.workflow.Workflow;
import com.capgemini.rules.workflow.WorkflowContext;

/**
 * An abstract Node representing a processing rectangle node in an activity
 * diagram i.e. will generally update the flow context in some way.
 * 
 * The next Node in the flow (if there is one) is fixed (and mandatory).
 * 
 * Child classes should implement the {@link #performAction(WorkflowContext)}
 * method.
 */
public abstract class AbstractProcessingNode<I, O> implements Node<I, O> {

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractProcessingNode.class);
	
	protected Node<I, O> nextNode;

	protected String logicalName;

	/**
	 * Single-arg constructor to be used with the setter for nextNode.
	 * @param logicalName The logical name for the node.
	 */
	public AbstractProcessingNode(String logicalName) {
		assert(StringUtils.isNotEmpty(logicalName));
		this.logicalName = logicalName;
	}

	/**
	 * @param nextNode The next node in the {@link Workflow}. When the next node should terminate the {@link Workflow} use a {@link FinalNode}.
	 */
	public AbstractProcessingNode(String logicalName, Node<I, O> nextNode) {
		this.logicalName = logicalName;
		this.nextNode = nextNode;
	}

	/** {@inheritDoc} */
	@Override
	public final Node<I, O> execute(WorkflowContext<I, O> context) {
		assert(nextNode != null);
		LOGGER.trace((context.getLoggingPrefix() + " Execute processing node: [{}] with context: [{}]").trim(), logicalName, context.toString());
		performAction(context);
		LOGGER.trace((context.getLoggingPrefix() + " Terminating processing node: [{}] with context: [{}]").trim(), logicalName, context.toString());
		return nextNode;
	}

	/** {@inheritDoc} */
	@Override
	public String getLogicalName() {
		return logicalName;
	}

	/** Optional setter to be used if you prefer the single-arg constructor. */
	public void setNextNode(Node<I, O> nextNode) {
		this.nextNode = nextNode;
	}
	
	/**
	 * This is the extension point to be implemented by subclasses. It should
	 * apply the business logic to the {@link WorkflowContext}. 
	 */	
	protected abstract void performAction(WorkflowContext<I, O> context);

}
