package com.lms.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "courses")
public class Courses {

	@Id
	@GeneratedValue(generator = "cseqgen")
	@SequenceGenerator(name = "cseqgen", sequenceName = "csg", initialValue = 114, allocationSize = 1)
	@Column(name = "courseid")
	private int courseId;

	@Column(name = "coursename")
	private String courseName;

	@Column(name = "coursetrainer")
	private String courseTrainer;

	@Column(name = "coursecreatedate")
	private String courseCreateDate;

	@Lob
	@Column(columnDefinition = "LONGBLOB",name = "courseimage")
	private byte[] courseImage;

	@Column(name = "coursedescription")
	private String courseDescription;

	@Column(nullable = false, columnDefinition = "TINYINT", length = 1,name = "archived")
	private boolean archived;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "fk_courseid", referencedColumnName = "courseId", nullable = false)
	private List<CourseModules> courseModule;

	@ManyToMany(mappedBy = "coursesList", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("coursesList")
	private List<CourseUsers> courseUsers;
}
