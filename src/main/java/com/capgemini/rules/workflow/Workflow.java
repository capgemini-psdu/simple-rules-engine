package com.capgemini.rules.workflow;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capgemini.rules.node.FinalNode;
import com.capgemini.rules.node.Node;

/**
 * A Workflow wraps a graph of one or more {@link Node} objects. It is responsible
 * for executing each {@link Node} in sequence until an execution returns
 * {@link FinalNode}. The {@link Node} objects themselves will decide which is the
 * next node to be executed in the Workflow. 
 * 
 * The Workflow, and each {@link Node} within it, will operate on a {@link WorkflowContext}
 * that has input and output objects with generic types I and O respectively.
 * 
 * This constraint that all Nodes in a Workflow, including nested Workflow Nodes, must share
 * the same type of WorkflowContext may appear at first to limit re-use opportunities.
 * However in reality I expect that Nodes will be tightly coupled to their specific
 * WorkflowContext anyway and the input/output objects in the WorkflowContext can also be made
 * to be generic enough to allow of re-use of Workflows. 
 */
public class Workflow<I, O> implements Node<I, O> {

	private static Logger LOGGER = LoggerFactory.getLogger(Workflow.class);

	private String logicalName;
	
	protected Node<I, O> rootNode;

	/**
	 * Single-arg constructor to be used with the setter for rootNode.
	 * @param logicalName The logical name for the workflow (which is itself a Node).
	 */
	public Workflow(String logicalName) {
		assert(StringUtils.isNotEmpty(logicalName));
		this.logicalName = logicalName;
	}
	
	/**
	 * @param logicalName A logical name for the workflow.
	 * @param rootNode The first node in the workflow.
	 */
	public Workflow(String logicalName, Node<I, O> rootNode) {
		assert(StringUtils.isNotEmpty(logicalName));
		this.logicalName = logicalName;
		this.rootNode = rootNode;
	}

	public Node<I, O> execute(WorkflowContext<I, O> context) {
		assert(rootNode != null);
		Node<I, O> nextNode = rootNode;
		LOGGER.info((context.getLoggingPrefix() + " Starting workflow: [{}]").trim(), logicalName);
		while (!(nextNode instanceof FinalNode)) {
			LOGGER.info((context.getLoggingPrefix() + " Execute workflow: [{}] node: [{}] class: [{}]").trim(), logicalName, nextNode.getLogicalName(), StringUtils.defaultIfEmpty(nextNode.getClass().getSimpleName(), "<anonymous>"));
			nextNode = nextNode.execute(context);
		}
		LOGGER.info((context.getLoggingPrefix() + " Terminating workflow: [{}]").trim(), logicalName);
		LOGGER.debug((context.getLoggingPrefix() + " Terminating workflow: [{}] with context: [{}]").trim(), logicalName, context.toString());
		return nextNode;
	}

	@Override
	public String getLogicalName() {
		return logicalName;
	}

	/** Optional setter to be used if you prefer the single-arg constructor. */
	public void setRootNode(Node<I, O> rootNode) {
		this.rootNode = rootNode;
	}
	
}
