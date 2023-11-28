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
@Table(name = "usercourses")
public class UserCourse {

	@Id
	@JsonProperty(access = Access.WRITE_ONLY)
	private int uid;
	@NotEmpty
	private String username;
	@NotEmpty
	private String useremail;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "usercourses_courses", joinColumns = @JoinColumn(name = "fk_ucid"), inverseJoinColumns = @JoinColumn(name = "fk_cid"))
	@JsonIgnoreProperties("uc")
	private List<Courses> listcourses;

}
