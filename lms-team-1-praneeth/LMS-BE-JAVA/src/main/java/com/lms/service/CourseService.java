package com.lms.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lms.dto.CourseUsersInfoDto;
import com.lms.dto.CoursesListDto;
import com.lms.dto.ModuleUpdateDto;
import com.lms.dto.CourseInfoDto;
import com.lms.dto.CourseUserDto;
import com.lms.dto.VideoUploadDto;
import com.lms.entity.CourseModules;
import com.lms.entity.CourseUsers;
import com.lms.entity.Courses;

public interface CourseService {

	boolean saveCourseUser(CourseUsers courseUsers);

	CourseUserDto getCourseUsers(String courseUserName);

	boolean updateCourseUser(CourseUsers courseUsers, String userEmail);

	boolean deleterCourseUser(String email);

	boolean saveCourses(Courses course);

	boolean updateCourses(Courses course, String coursename, String trainerName);

	List<CoursesListDto> getAllCourses();

	CourseInfoDto getCourseInfo(String courseName);

	List<CourseUsersInfoDto> getCourses(String courseName, String trainerName);

	boolean deleteCourse(String courseName, String trainerName);

	boolean accessCouresToUser(String courseUserEmail, String courseName, String trainerName);

	boolean removeCourseAccess(String userEmail, String courseName, String trainerName);

	boolean addVideoLink(VideoUploadDto videoDto);

	List<CourseModules> getCourseModules(String courseName, String trainerName);

	List<CourseModules> updateModule(String courseName, int modulenum, ModuleUpdateDto mud);

	boolean deleteModule(String courseName, int modulenum);

	boolean saveResume(String userEmail, MultipartFile multipart) throws Exception;

	byte[] getResume(String userEmail);

	boolean deleteResume(String userEmail);

}
