package com.rg.gph.playgroundbasic.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailQuery implements Serializable {

	private String id;

	private String fullName;

	private String emailAddress;

	private String phoneNumber;

	private Integer age;

	private String placeOfBirth;

	private LocalDate birthDate;
}
