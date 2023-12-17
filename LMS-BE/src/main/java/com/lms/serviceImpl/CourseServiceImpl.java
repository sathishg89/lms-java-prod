package com.lms.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.constants.CustomErrorCodes;
import com.lms.dto.AllCourseUsersDto;
import com.lms.dto.AllCoursesDto;
import com.lms.dto.UserCoursesDto;
import com.lms.dto.VideoUploadDto;
import com.lms.entity.CourseLink;
import com.lms.entity.CourseModules;
import com.lms.entity.CourseUsers;
import com.lms.entity.Courses;
import com.lms.exception.details.CustomException;
import com.lms.repository.CourseUsersRepo;
import com.lms.repository.CoursesRepo;
import com.lms.service.CourseService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseUsersRepo ucr;

	@Autowired
	private CoursesRepo cr;

	@Override
	public boolean saveCourseUser(CourseUsers courseUsers) {

		CourseUsers save = ucr.save(courseUsers);
		if (save == null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean saveCourses(Courses course) {

		course.setCoursecreatedate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));

		Courses save = cr.save(course);

		if (save == null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean accessCouresToUser(String courseUserEmail, String courseName, String trainerName) {

		boolean userExists = ucr.existsByuserEmail(courseUserEmail);
		boolean courseExists = cr.existsBycoursename(courseName);

		if (userExists && courseExists) {

			CourseUsers fun = ucr.findByuserEmail(courseUserEmail);
			List<Courses> fcn = cr.findBycoursename(courseName);

			Optional<Courses> courseOptional = fcn.stream()
					.filter(course -> course.getCoursetrainer().equals(trainerName)).findFirst();

			if (!fun.getCoursesList().containsAll(fcn)) {
				fun.getCoursesList().add(courseOptional.get());
				ucr.save(fun);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean addVideoLink(VideoUploadDto videoDto) {

		LinkedHashSet<String> videolink = videoDto.getVideolink();

		List<String> videoname = videoDto.getVideoname();
		List<String> linklist = new ArrayList<>(videolink);

		if (videoname.size() < videolink.size() || videolink.size() > videoname.size()) {
			throw new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
					CustomErrorCodes.INVALID_DETAILS.getErrorCode());
		} else {
			LinkedHashMap<String, String> linkedmap = new LinkedHashMap<>();

			Iterator<String> nameIterator = videoname.iterator();
			Iterator<String> linkIterator = linklist.iterator();

			while (nameIterator.hasNext() && linkIterator.hasNext()) {
				String name = nameIterator.next();
				String link = linkIterator.next();
				linkedmap.put(name, link);
			}
			// find the details from db using cname, trainername
			List<Courses> fcn = cr.findBycoursenameAndcoursetrainer(videoDto.getCourseName(),
					videoDto.getTrainerName());

			CourseLink cl = CourseLink.builder().links(linklist).videoname(videoDto.getVideoname()).build();

			List<CourseLink> cl1 = new ArrayList<>();
			cl1.add(cl);

			// converting the details into cm object
			CourseModules cm = CourseModules.builder().modulenum(videoDto.getModulenumber())
					.videoinserttime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy "))).clinks(cl1)
					.build();

			// if fcn contains
			if (fcn.size() > 0) {

				// by using tname gettiing the course object
				Courses courses = fcn.stream()
						.filter(course -> course.getCoursetrainer().equals(videoDto.getTrainerName())).findFirst()
						.get();

				// getting the coursemodules from courses
				List<CourseModules> existingModules = courses.getCoursemodule();

				// if courses are already exist the it goes inside else outside
				if (existingModules.size() > 0) {

					// check the modulenum from db and from client if both are same then return the
					// coursemodule list
					Optional<CourseModules> em = existingModules.stream()
							.filter(module -> module.getModulenum() == videoDto.getModulenumber()).findFirst();

					// add the videolink to set of link if the module if present or else add the
					// builder to existingmocules list

					if (em.isPresent()) {

						CourseModules cm1 = em.get();

						List<CourseLink> clinks = cm1.getClinks();

						log.info("" + clinks);

						if (clinks.size() > 0) {
							for (CourseLink existingCl : clinks) {
								log.info("" + existingCl);
								existingCl.getLinks().addAll(cl.getLinks());
								existingCl.getVideoname().addAll(cl.getVideoname());
							}
						} else {

							clinks.addAll(cl1);

						}

					} else {

						existingModules.add(cm);
						// log.info("" +existingModules);
					}

				} else {
					existingModules.add(cm);
				}
				// set the course object with new setcoursemodule
				courses.setCoursemodule(existingModules);
				cr.save(courses);

				return true;
			} else {
				return false;
			}
		}

	}

	@Override
	public UserCoursesDto getCourseUsers(String courseUserEmail) {

		try {
			CourseUsers fun = ucr.findByuserEmail(courseUserEmail);

			UserCoursesDto ucd = UserCoursesDto.builder().username(fun.getUserName()).useremail(fun.getUserEmail())
					.courseslist(fun.getCoursesList()).build();

			return ucd;
		} catch (Exception e) {
			throw new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());
		}

	}

	@Override
	public List<AllCourseUsersDto> getCourses(String courseName, String trainerName) {

		try {
			List<Courses> findByusername = cr.findBycoursename(courseName);

			List<AllCourseUsersDto> collect = findByusername.stream()
					.filter(fil -> fil.getCoursetrainer().equals(trainerName))
					.map(c -> new AllCourseUsersDto(c.getCoursename(), c.getCoursetrainer(), c.getCourseusers()))
					.collect(Collectors.toList());
			return collect;
		} catch (Exception e) {

			throw new CustomException(CustomErrorCodes.USER_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.USER_NOT_FOUND.getErrorCode());

		}

	}

	@Override
	public List<CourseModules> getVideoLink(String userEmail, String courseName, String trainerName) {

		try {
			CourseUsers courseUsers = ucr.findByuserEmail(userEmail);

			List<CourseModules> collect = courseUsers.getCoursesList().stream()
					.filter(fil -> fil.getCoursename().equals(courseName) && fil.getCoursetrainer().equals(trainerName))
					.flatMap(courses -> courses.getCoursemodule().stream()).collect(Collectors.toList());

			if (collect.size() > 0) {
				return collect;
			} else {
				throw new CustomException(CustomErrorCodes.COURSE_NOT_FOUND.getErrorMsg(),
						CustomErrorCodes.COURSE_NOT_FOUND.getErrorCode());
			}
		} catch (Exception e) {
			throw new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
					CustomErrorCodes.INVALID_DETAILS.getErrorCode());
		}

	}

	@Override
	public boolean deleterCourseUser(String email) {

		CourseUsers user = ucr.findByuserEmail(email);

		if (user != null) {
			user.getCoursesList().clear();

			ucr.save(user);

			ucr.delete(user);

			return true;
		}

		else {
			return false;
		}

	}

	@Override
	public boolean deleteCourse(String courseName, String trainerName) {

		List<Courses> findBycoursenameAndcoursetrainer = cr.findBycoursenameAndcoursetrainer(courseName, trainerName);

		if (!findBycoursenameAndcoursetrainer.isEmpty()) {
			Courses courses = findBycoursenameAndcoursetrainer.get(0);

			cr.delete(courses);
			return true;

		} else {
			return false;
		}

	}

	@Override
	public boolean removeCourseAccess(String userEmail, String courseName, String trainerName) {

		CourseUsers findByuserEmail = ucr.findByuserEmail(userEmail);

		if (findByuserEmail != null) {
			List<Courses> coursesList = findByuserEmail.getCoursesList();

			List<Courses> collect = coursesList.stream()
					.filter(fil -> fil.getCoursename().equals(courseName) & fil.getCoursetrainer().equals(trainerName))
					.collect(Collectors.toList());

			collect.clear();

			findByuserEmail.setCoursesList(collect);

			ucr.save(findByuserEmail);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public List<AllCoursesDto> getAllCourses() {
		List<AllCoursesDto> findAll = cr.getOnlyCourses();

		return findAll;
	}

}
