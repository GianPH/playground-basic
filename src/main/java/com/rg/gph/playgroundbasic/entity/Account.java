package com.rg.gph.playgroundbasic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "email_address", unique = true)
	private String emailAddress;

	@Column(name = "phone_number", unique = true, length = 18)
	private String phoneNumber;

	@Column(name = "age")
	private Integer age;

	@Column(name = "place_of_birth")
	private String placeOfBirth;

	@Column(name = "birth_date")
	private LocalDate birthDate;
}
