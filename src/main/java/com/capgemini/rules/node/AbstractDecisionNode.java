package com.capgemini.rules.node;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capgemini.rules.workflow.WorkflowContext;

/**
 * An abstract Node representing a decision diamond in an activity diagram i.e.
 * it implements a boolean decision within a flow where the result of the
 * decision will determine which node is processed next.
 * 
 * Child classes should implement the {@link #makeDecision(WorkflowContext)}
 * method.
 * 
 * The next node for both <code>true</code> and <code>false</code> results must
 * be dependency injected.
 */
public abstract class AbstractDecisionNode<I, O> implements Node<I, O> {

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractDecisionNode.class);
	
	protected Node<I, O> successNode;

	protected Node<I, O> failureNode;

	protected String logicalName;
	
	/**
	 * Single-arg constructor to be used with the setters for successNode and failureNode.
	 * @param logicalName The logical name for the node.
	 */
	public AbstractDecisionNode(String logicalName) {
		assert(StringUtils.isNotEmpty(logicalName));
		this.logicalName = logicalName;
	}

	/**
	 * @param success The next node when the semantic result of the execution of this node is <code>true</code>.
	 * @param failure The next node when the semantic result of the execution of this node is <code>false</code>.
	 */
	public AbstractDecisionNode(String logicalName, Node<I, O> successNode, Node<I, O> failureNode) {
		this.logicalName = logicalName;
		this.successNode = successNode;
		this.failureNode = failureNode;
	}

	/** {@inheritDoc} */
	@Override
	public final Node<I, O> execute(WorkflowContext<I, O> context) {
		assert(successNode != null);
		assert(failureNode != null);
		LOGGER.trace((context.getLoggingPrefix() + " Execute decision node: [{}] with context: [{}]").trim(), logicalName, context.toString());
		boolean result = makeDecision(context);
		LOGGER.trace((context.getLoggingPrefix() + " Decision node: [{}] result: [{}] with context: [{}]").trim(), logicalName, result, context.toString());
		if (result) {
			return successNode;
		} else {
			return failureNode;
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getLogicalName() {
		return logicalName;
	}

	/** Optional fluent setter to be used if you prefer the single-arg constructor. */
	public Node<I, O> setSuccessNode(Node<I, O> successNode) {
		this.successNode = successNode;
		return this;
	}

	/** Optional setter to be used if you prefer the single-arg constructor. */
	public void setFailureNode(Node<I, O> failureNode) {
		this.failureNode = failureNode;
	}

	/**
	 * This is the extension point to be implemented by subclasses. It should
	 * return <code>true</code> or <code>false</code> depending upon the result
	 * of the business logic applied. 
	 */
	protected abstract boolean makeDecision(WorkflowContext<I, O> context);

}
