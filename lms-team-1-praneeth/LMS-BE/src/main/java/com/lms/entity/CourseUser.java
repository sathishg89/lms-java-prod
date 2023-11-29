package com.lms.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class CourseUser {

	@Id
	@JsonProperty(access = Access.WRITE_ONLY)
	private int uid;
	@NotEmpty
	private String username;
	@NotEmpty
	private String useremail;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "courses_users", joinColumns = @JoinColumn(name = "fk_userid"), inverseJoinColumns = @JoinColumn(name = "fk_courseid"))
	@JsonIgnoreProperties("uc")
	private List<Courses> listcourses;

}
