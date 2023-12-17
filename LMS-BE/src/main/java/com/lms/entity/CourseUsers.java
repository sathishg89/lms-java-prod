package com.lms.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "corseusers")
public class CourseUsers {

	@Id
	@GeneratedValue(generator = "cuseqgen")
	@SequenceGenerator(name = "cuseqgen", sequenceName = "LMS.cusg", initialValue = 1001, allocationSize = 1, schema = "LMS")
	@JsonProperty(access = Access.WRITE_ONLY)
	private int userid;

	@NotEmpty(message = "username cannot be empty")
	@Column(name = "username")
	private String userName;

	@NotEmpty(message = "useremail cannot be empty")
	@Column(name = "useremail")
	private String userEmail;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "courses_users", joinColumns = @JoinColumn(name = "fk_userid"), inverseJoinColumns = @JoinColumn(name = "fk_courseid"))
	@JsonIgnoreProperties({ "courseusers", "courseinsertdate" })
	private List<Courses> coursesList;

}
