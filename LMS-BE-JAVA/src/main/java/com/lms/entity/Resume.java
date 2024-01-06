package com.lms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "resume")
public class Resume {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "resumeid")
	private int resumeId;

	@NotEmpty(message = "email cannot be empty")
	@Column(name = "useremail")
	private String userEmail;

	@NotEmpty(message = "file cannot be empty")
	@Lob
	@Column(columnDefinition = "LONGBLOB", name = "resume")
	private byte[] resume;

}
