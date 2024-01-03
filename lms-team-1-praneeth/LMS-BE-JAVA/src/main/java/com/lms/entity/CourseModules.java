package com.lms.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coursemodules")
public class CourseModules {

	@Id
	@GeneratedValue(generator = "cmseqgen")
	@SequenceGenerator(name = "cmseqgen", sequenceName = "cmsg", initialValue = 5, allocationSize = 1)
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "cmid")
	private int moduleId;

	@Column(name="modulenumber")
	private int moduleNumber;

	@NotEmpty(message = "Modulename cannot be empty")
	@Column(name="modulename")
	private String moduleName;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name="videoinserttime")
	private String videoInsertTime;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fk_cmid")
	private List<CourseLink> courseLinks;

}
