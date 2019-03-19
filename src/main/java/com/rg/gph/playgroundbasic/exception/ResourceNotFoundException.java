package com.rg.gph.playgroundbasic.exception;

public class ResourceNotFoundException extends RuntimeException {

	private final String resourceId;

	private final String resourceName;

	public String getResourceId() {
		return resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public ResourceNotFoundException(String resourceId, String resourceName) {
		super(String.format("%s with id (%s) not found", resourceName, resourceId));
		this.resourceId = resourceId;
		this.resourceName = resourceName;
	}

	public ResourceNotFoundException(String message, String resourceId, String resourceName) {
		super(message);
		this.resourceId = resourceId;
		this.resourceName = resourceName;
	}

	public ResourceNotFoundException(String message, Throwable cause, String resourceId, String resourceName) {
		super(message, cause);
		this.resourceId = resourceId;
		this.resourceName = resourceName;
	}

	public ResourceNotFoundException(Throwable cause, String resourceId, String resourceName) {
		super(cause);
		this.resourceId = resourceId;
		this.resourceName = resourceName;
	}

	public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String resourceId, String resourceName) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.resourceId = resourceId;
		this.resourceName = resourceName;
	}
}
