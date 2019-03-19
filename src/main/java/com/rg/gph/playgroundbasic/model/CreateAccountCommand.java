package com.rg.gph.playgroundbasic.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAccountCommand implements Serializable {

	@NotNull(message = "fullName may not null")
	@NotEmpty(message = "fullName may not empty")
	private String fullName;

	@Email
	@NotNull(message = "emailAddress may not null")
	@NotEmpty(message = "emailAddress may not empty")
	private String emailAddress;

	@Pattern(
			regexp = "^\\d{4,15}",
			flags = Pattern.Flag.CASE_INSENSITIVE,
			message = "Invalid phoneNumber format")
	@Size(min = 5, max = 15)
	@NotNull(message = "phoneNumber may not null")
	@NotEmpty(message = "phoneNumber may not empty")
	private String phoneNumber;

	@NotNull(message = "placeOfBirth may not null")
	@NotEmpty(message = "placeOfBirth may not empty")
	private String placeOfBirth;

	@NotNull(message = "birthDate may not be null")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate birthDate;

}
