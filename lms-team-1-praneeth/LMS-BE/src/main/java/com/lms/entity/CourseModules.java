package com.lms.entity;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "coursemodules")
public class CourseModules {

	@Id
	private int cmid;

	private int module;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "courselinks", joinColumns = @JoinColumn(name = "cmid"))
	private Set<String> clinks;

}
