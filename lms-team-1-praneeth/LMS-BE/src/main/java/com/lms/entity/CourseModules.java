package com.lms.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "coursemodules")
@NoArgsConstructor
@AllArgsConstructor
public class CourseModules {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = Access.WRITE_ONLY)
	private int cmid;

	private int modulenum;

	
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "courselinks", joinColumns = @JoinColumn(name = "fk_cmid"))
	private Set<String> clinks;

}
