package com.rg.gph.playgroundbasic.exception.handler;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rg.gph.playgroundbasic.exception.ResourceNotFoundException;
import com.rg.gph.playgroundbasic.model.ApiResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class PlaygroundBasicExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	@NonNull
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			@NonNull final MethodArgumentNotValidException ex,
			@NonNull final HttpHeaders headers,
			@NonNull final HttpStatus status,
			@NonNull final WebRequest request) {

		Map<String, Map<String, String>> totalError = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			if (CollectionUtils.isEmpty(totalError)) {
				totalError.put(error.getCode(), Stream.of(
						new AbstractMap.SimpleEntry<>(error.getField(), error.getDefaultMessage()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
			}
			else if (totalError.containsKey(error.getCode())) {
				Map<String, String> stringMap = totalError.get(error.getCode());
				stringMap.put(error.getField(), error.getDefaultMessage());
				totalError.put(error.getCode(), stringMap);
			}
			else {
				totalError.put(error.getCode(), Stream.of(
						new AbstractMap.SimpleEntry<>(error.getField(), error.getDefaultMessage()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
			}
		}

		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			if (CollectionUtils.isEmpty(totalError)) {
				totalError.put(error.getCode(), Stream.of(
						new AbstractMap.SimpleEntry<>(error.getObjectName(), error.getDefaultMessage()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
			}
			else if (totalError.containsKey(error.getCode())) {
				Map<String, String> stringMap = totalError.get(error.getCode());
				stringMap.put(error.getObjectName(), error.getDefaultMessage());
				totalError.put(error.getCode(), stringMap);
			}
			else {
				totalError.put(error.getCode(), Stream.of(
						new AbstractMap.SimpleEntry<>(error.getObjectName(), error.getDefaultMessage()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
			}
		}

		ApiResponse apiResponse = ApiResponse
				.badRequest()
				.message(String.format("Validation error for (%s) Error count (%s)", ex.getParameter().getExecutable().getName(), ex.getBindingResult().getErrorCount()))
				.withErrors(totalError)
				.build();

		return handleExceptionInternal(ex, apiResponse, headers, apiResponse.getStatus(), request);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	private ResponseEntity<ApiResponse> handleResourceNotFoundException(final ResourceNotFoundException ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ApiResponse
						.notFound()
						.message(ex.getMessage())
						.build()
				);
	}

}
