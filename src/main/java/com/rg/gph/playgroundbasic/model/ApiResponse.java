package com.rg.gph.playgroundbasic.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.springframework.http.HttpStatus;

@JsonPropertyOrder({"code", "status", "message", "data", "errors"})
public class ApiResponse<T> {

	@JsonProperty("code")
	private final Integer code;

	@JsonProperty("status")
	private final HttpStatus status;

	@JsonProperty("message")
	private final String message;

	@JsonProperty("data")
	private final T data;

	@JsonProperty("errors")
	private final Map<String, ?> errors;

	private ApiResponse(Integer code, HttpStatus status, String message, T data, Map<String, ?> errors) {
		this.code = code;
		this.status = status;
		this.message = message;
		this.data = data;
		this.errors = errors;
	}

	public Integer getCode() {
		return code;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public Map<String, ?> getErrors() {
		return errors;
	}

	public static ApiResponse.ApiResponseBuilder status(final HttpStatus status) {
		return new ApiResponse.ApiResponseDefaultBuilder(status);
	}

	public static ApiResponse.ApiResponseBuilder ok() {
		return new ApiResponse.ApiResponseDefaultBuilder(HttpStatus.OK);
	}

	public static ApiResponse.ApiResponseBuilder badRequest() {
		return new ApiResponse.ApiResponseDefaultBuilder(HttpStatus.BAD_REQUEST);
	}

	public static ApiResponse.ApiResponseBuilder notFound() {
		return new ApiResponse.ApiResponseDefaultBuilder(HttpStatus.NOT_FOUND);
	}

	public interface ApiResponseBuilder {

		ApiResponseBuilder message(String message);

		ApiResponseBuilder withErrors(Map<String, ?> errors);

		<T> ApiResponse<T> build();

		<T> ApiResponse<T> data(T data);
	}

	private static class ApiResponseDefaultBuilder implements ApiResponseBuilder {

		private HttpStatus status;

		private String message;

		private Map<String, ?> errors;

		private ApiResponseDefaultBuilder(final HttpStatus status) {
			this.status = status;
		}

		public ApiResponseBuilder message(final String message) {
			this.message = message;
			return this;
		}

		public ApiResponseBuilder withErrors(Map<String, ?> errors) {
			this.errors = errors;
			return this;
		}

		public <T> ApiResponse<T> build() {
			return data(null);
		}

		public <T> ApiResponse<T> data(final T data) {
			return new ApiResponse<>(this.status.value(), this.status, this.message, data, this.errors);
		}

	}

}
