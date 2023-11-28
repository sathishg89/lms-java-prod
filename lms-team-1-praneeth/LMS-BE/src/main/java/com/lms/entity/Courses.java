package com.lms.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Courses {

	@Id
	@JsonProperty(access = Access.WRITE_ONLY)
	private int cid;

	private String coursename;

	private String coursetrainer;

	private LocalDateTime courseinsertdate;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cmodule", referencedColumnName = "cid")
	private List<CourseModules> cmodule;

	@ManyToMany(mappedBy = "listcourses", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("courses")
	private List<UserCourse> uc;
}
