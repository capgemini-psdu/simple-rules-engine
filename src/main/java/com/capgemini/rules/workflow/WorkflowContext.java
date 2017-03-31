package com.capgemini.rules.workflow;

import com.google.gson.Gson;

/**
 * Context shared with all nodes in a {@link Workflow}.
 * 
 * The context accepts input and output objects with generic types I and O respectively.
 * 
 * Generally the {@link Workflow} will operate based on the data supplied in the input
 * object whilst maintaining its state in the output object. The output object is also
 * used to return any final state or result to the calling process.
 */
public class WorkflowContext<I, O> {

	/** Input object for workflow */
	private I inputObject;

	/** Output object for workflow */
	private O outputObject;
	
	/** A prefix for any logging. For example may be some sort of business or thread identifier. */
	private String loggingPrefix = ""; 	

	public WorkflowContext(I inputObject, O outputObject, String loggingPrefix) {
		this.inputObject = inputObject;
		this.outputObject = outputObject;
		this.loggingPrefix = loggingPrefix;
	}

	public WorkflowContext(I inputObject, String loggingPrefix) {
		this.inputObject = inputObject;
		this.loggingPrefix = loggingPrefix;
	}

	public WorkflowContext(I inputObject, O outputObject) {
		this.inputObject = inputObject;
		this.outputObject = outputObject;
	}

	public WorkflowContext() {
	}

	public I getInputObject() {
		return inputObject;
	}

	public WorkflowContext<I, O> setInputObject(I inputObject) {
		this.inputObject = inputObject;
		return this;
	}

	public O getOutputObject() {
		return outputObject;
	}

	public WorkflowContext<I, O> setOutputObject(O outputObject) {
		this.outputObject = outputObject;
		return this;
	}

	public String getLoggingPrefix() {
		return loggingPrefix;
	}

	public void setLoggingPrefix(String loggingPrefix) {
		this.loggingPrefix = loggingPrefix;
	}

	@Override
	public String toString() {		
		Gson gson = new Gson();
		return gson.toJson(this);  
	}
}
