package com.lms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@Hidden
	private long userId;

	@Column(name = "username")
	@NotEmpty(message = "name cannot be empty")
	private String userName;

	@NotEmpty(message = "Email cannot be empty")
	@Column(name = "email")
	private String userEmail;

	@NotEmpty(message = "password cannot be empty")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@NotEmpty(message = "role cannot be empty")
	private String role;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	@Hidden
	private byte[] img;

	@Column(nullable = false, columnDefinition = "TINYINT", length = 1)
	private boolean isActive;

}
