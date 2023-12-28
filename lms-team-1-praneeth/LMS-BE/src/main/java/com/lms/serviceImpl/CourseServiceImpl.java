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
import org.springframework.web.multipart.MultipartFile;

import com.lms.constants.CustomErrorCodes;
import com.lms.dto.CourseInfoDto;
import com.lms.dto.CourseUserDto;
import com.lms.dto.CourseUsersInfoDto;
import com.lms.dto.CoursesListDto;
import com.lms.dto.ModuleUpdateDto;
import com.lms.dto.VideoUploadDto;
import com.lms.entity.CourseLink;
import com.lms.entity.CourseModules;
import com.lms.entity.CourseUsers;
import com.lms.entity.Courses;
import com.lms.entity.Resume;
import com.lms.exception.details.CustomException;
import com.lms.repository.CourseUsersRepo;
import com.lms.repository.CoursesRepo;
import com.lms.repository.ResumeRepo;
import com.lms.service.CourseService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseUsersRepo ucr;

	@Autowired
	private CoursesRepo cr;

	@Autowired
	private ResumeRepo rr;

	@Override
	public boolean saveCourseUser(CourseUsers courseUsers) {

		if (!ucr.existsByuserEmail(courseUsers.getUserEmail())) {
			CourseUsers save = ucr.save(courseUsers);
			if (save == null) {
				return false;
			} else {
				return true;
			}
		} else {

			throw new CustomException(CustomErrorCodes.USER_ALREADY_EXIST.getErrorMsg(),
					CustomErrorCodes.USER_ALREADY_EXIST.getErrorCode());
		}

	}

	@Override
	public boolean updateCourseUser(CourseUsers courseUsers, String userEmail) {

		if (ucr.existsByuserEmail(userEmail)) {
			CourseUsers findByuserEmail = ucr.findByuserEmail(userEmail);

			if (courseUsers != null && courseUsers.getUserEmail() != null) {
				findByuserEmail.setUserEmail(courseUsers.getUserEmail());
			}
			if (courseUsers != null && courseUsers.getUserName() != null) {
				findByuserEmail.setUserName(courseUsers.getUserName());
			}
			CourseUsers save = ucr.save(findByuserEmail);
			if (save == null) {
				return false;
			} else {
				return true;
			}
		} else {

			throw new CustomException(CustomErrorCodes.USER_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.USER_NOT_FOUND.getErrorCode());
		}

	}

	@Override
	public boolean saveCourses(Courses course) {

		course.setCourseCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));

		if (!cr.existsBycourseName(course.getCourseName())) {
			Courses save = cr.save(course);

			if (save == null) {
				return false;
			} else {
				return true;
			}
		} else {
			throw new CustomException(CustomErrorCodes.COURSE_ALREADY_EXIST.getErrorMsg(),
					CustomErrorCodes.COURSE_ALREADY_EXIST.getErrorCode());
		}

	}

	@Override
	public boolean updateCourses(Courses course, String coursename, String trainerName) {

		if (cr.existsBycourseName(coursename)) {

			List<Courses> lcourses = cr.findBycourseNameAndcourseTrainer(coursename, trainerName);

			if (!lcourses.isEmpty()) {
				Courses courses = lcourses.get(0);
				if (course.getCourseName() != null && !course.getCourseName().isEmpty()) {
					courses.setCourseName(course.getCourseName());

				}
				if (course.getCourseTrainer() != null && !course.getCourseTrainer().isEmpty()) {
					courses.setCourseTrainer(course.getCourseTrainer());

				}
				if (course.getCourseDescription() != null && !course.getCourseDescription().isEmpty()) {
					courses.setCourseDescription(course.getCourseDescription());

				}
				if (course.getCourseImage() != null) {
					courses.setCourseImage(course.getCourseImage());
				}
				if (course.isArchived() != false) {
					courses.setArchived(course.isArchived());
				}

				log.info("c1 " + courses.getCourseName() + courses.getCourseTrainer() + courses.isArchived());

				Courses save = cr.save(courses);

				if (save == null) {
					return false;
				} else {
					return true;
				}
			} else {
				throw new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
						CustomErrorCodes.INVALID_DETAILS.getErrorMsg());
			}

		} else {
			throw new CustomException(CustomErrorCodes.COURSE_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.COURSE_NOT_FOUND.getErrorCode());
		}

	}

	@Override
	public boolean accessCouresToUser(String courseUserEmail, String courseName, String trainerName) {

		boolean userExists = ucr.existsByuserEmail(courseUserEmail);
		boolean courseExists = cr.existsBycourseName(courseName);

		if (userExists && courseExists) {

			CourseUsers fun = ucr.findByuserEmail(courseUserEmail);
			List<Courses> fcn = cr.findBycourseName(courseName);

			Optional<Courses> courseOptional = fcn.stream()
					.filter(course -> course.getCourseTrainer().equals(trainerName)).findFirst();

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

		LinkedHashSet<String> videolink = videoDto.getVideoLink();

		List<String> videoname = videoDto.getVideoName();
		List<String> linklist = new ArrayList<>(videolink);

		if (videoname.size() < videolink.size() || videoname.size() > videolink.size()) {
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
			List<Courses> fcn = cr.findBycourseNameAndcourseTrainer(videoDto.getCourseName(),
					videoDto.getCourseTrainer());

			CourseLink cl = CourseLink.builder().videoLink(linklist).videoName(videoDto.getVideoName()).build();

			List<CourseLink> cl1 = new ArrayList<>();
			cl1.add(cl);

			// converting the details into cm object
			CourseModules cm = CourseModules.builder().moduleNumber(videoDto.getModuleNumber())

					.moduleName(videoDto.getModuleName())
					.videoInsertTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy ")))
					.courseLinks(cl1).build();

			// if fcn contains
			if (fcn.size() > 0) {

				// by using tname gettiing the course object
				Courses courses = fcn.stream()
						.filter(course -> course.getCourseTrainer().equals(videoDto.getCourseTrainer())).findFirst()
						.get();

				// getting the coursemodules from courses
				List<CourseModules> existingModules = courses.getCourseModule();

				// if courses are already exist the it goes inside else outside
				if (existingModules.size() > 0) {

					// check the modulenum from db and from client if both are same then return the
					// coursemodule list
					Optional<CourseModules> em = existingModules.stream()
							.filter(module -> module.getModuleNumber() == videoDto.getModuleNumber()).findFirst();

					// add the videolink to set of link if the module if present or else add the
					// builder to existingmocules list

					if (em.isPresent()) {

						CourseModules cm1 = em.get();

						List<CourseLink> clinks = cm1.getCourseLinks();

						log.info("" + clinks);

						if (clinks.size() > 0) {
							for (CourseLink existingCl : clinks) {
								log.info("" + existingCl);
								existingCl.getVideoLink().addAll(cl.getVideoLink());
								existingCl.getVideoName().addAll(cl.getVideoName());
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
				courses.setCourseModule(existingModules);
				cr.save(courses);

				return true;
			} else {
				return false;
			}
		}

	}

	@Override
	public CourseUserDto getCourseUsers(String courseUserEmail) {

		try {
			CourseUsers fun = ucr.findByuserEmail(courseUserEmail);

			CourseUserDto ucd = CourseUserDto.builder().userName(fun.getUserName()).userEmail(fun.getUserEmail())
					.coursesList(fun.getCoursesList()).build();

			return ucd;
		} catch (Exception e) {
			throw new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());
		}

	}

	@Override
	public List<CourseUsersInfoDto> getCourses(String courseName, String trainerName) {

		try {
			List<Courses> findByusername = cr.findBycourseName(courseName);

			List<CourseUsersInfoDto> collect = findByusername.stream()
					.filter(fil -> fil.getCourseTrainer().equals(trainerName))
					.map(c -> new CourseUsersInfoDto(c.getCourseId(), c.getCourseName(), c.getCourseTrainer(),
							c.getCourseCreateDate(), c.getCourseUsers()))
					.collect(Collectors.toList());
			return collect;
		} catch (Exception e) {

			throw new CustomException(CustomErrorCodes.USER_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.USER_NOT_FOUND.getErrorCode());

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

		List<Courses> findBycoursenameAndcoursetrainer = cr.findBycourseNameAndcourseTrainer(courseName, trainerName);

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

			coursesList.removeIf(course -> course.getCourseName().equals(courseName)
					&& course.getCourseTrainer().equals(trainerName));

			findByuserEmail.setCoursesList(coursesList);

			ucr.save(findByuserEmail);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public List<CoursesListDto> getAllCourses() {
		List<CoursesListDto> findAll = cr.getOnlyCourses();
		return findAll;

	}

	@Override
	public List<CourseModules> getCourseModules(String courseName, String trainerName) {

		boolean existsBycoursename = cr.existsBycourseName(courseName);

		if (existsBycoursename) {
			List<CourseModules> collect = cr.findCourseModulesByCourseName(courseName, trainerName);

			if (!collect.isEmpty()) {
				return collect;
			} else {
				throw new CustomException(CustomErrorCodes.MISSING_MODULE.getErrorMsg(),
						CustomErrorCodes.MISSING_MODULE.getErrorCode());
			}
		} else {
			throw new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
					CustomErrorCodes.INVALID_DETAILS.getErrorCode());
		}

	}

	@Override
	public CourseInfoDto getCourseInfo(String courseName) {

		List<Object[]> courseDetails = cr.getCourseDetails(courseName);

		List<Integer> modulenumList = courseDetails.stream().map(result -> (Integer) result[4])
				.collect(Collectors.toList());

		CourseInfoDto courseInfoDto = courseDetails.stream().findFirst()
				.map(result -> CourseInfoDto.builder().courseName((String) result[0]).courseTrainer((String) result[1])
						.courseImage((byte[]) result[2]).courseDescription((String) result[3])
						.moduleNumber(modulenumList).build())
				.get();

		return courseInfoDto;
	}

	@Override
	public boolean saveResume(String userEmail, MultipartFile multipart) throws Exception {

		byte[] file = multipart.getBytes();

		Resume r = Resume.builder().userEmail(userEmail).resume(file).build();

		Resume resume = rr.findByUserEmail(userEmail)
				.orElseThrow(() -> new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
						CustomErrorCodes.INVALID_EMAIL.getErrorCode()));
		if (resume == null) {
			rr.save(r);

		} else {
			resume.setResume(file);
			rr.save(resume);
		}
		return true;
	}

	@Override
	public byte[] getResume(String userEmail) {

		Resume resume = rr.findByUserEmail(userEmail)
				.orElseThrow(() -> new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
						CustomErrorCodes.INVALID_EMAIL.getErrorCode()));

		return resume.getResume();

	}

	@Override
	public boolean deleteResume(String userEmail) {

		Resume resume = rr.findByUserEmail(userEmail)
				.orElseThrow(() -> new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
						CustomErrorCodes.INVALID_EMAIL.getErrorCode()));

		resume.setResume(null);
		rr.save(resume);

		return true;
	}

	@Override
	public List<CourseModules> updateModule(String courseName, int modulenum, ModuleUpdateDto mud) {

		LocalDateTime now = LocalDateTime.now();

		Courses courses = cr.findBycourseName(courseName).get(0);

		if (courses == null) {

			throw new CustomException(CustomErrorCodes.COURSE_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.COURSE_NOT_FOUND.getErrorCode());

		}

		List<CourseModules> ml = courses.getCourseModule();

		Optional<CourseModules> optionalCourseModules = ml.stream().filter(x -> x.getModuleNumber() == modulenum)
				.findFirst();

		if (optionalCourseModules.isPresent()) {
			CourseModules courseModules = optionalCourseModules.get();

			courseModules.setModuleName(mud.getModuleName());
			courseModules.setModuleNumber(modulenum);
			courseModules.setVideoInsertTime(now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

			List<CourseLink> clinks = courseModules.getCourseLinks();

			if (!clinks.isEmpty()) {
				CourseLink courseLink = clinks.get(0);

				if (mud.getVideoLink() != null) {
					courseLink.setVideoLink(mud.getVideoLink());

				}
				if (mud.getVideoName() != null) {
					courseLink.setVideoName(mud.getVideoName());
				}

			}

			Courses save = cr.save(courses);
			return save.getCourseModule();
		} else {

			throw new CustomException(CustomErrorCodes.MISSING_MODULE.getErrorMsg(),
					CustomErrorCodes.MISSING_MODULE.getErrorCode());
		}
	}

	@Override
	public boolean deleteModule(String courseName, int modulenum) {
		Courses courses = cr.findBycourseName(courseName).get(0);

		List<CourseModules> ml = courses.getCourseModule();

		CourseModules courseModules = ml.stream().filter(x -> x.getModuleNumber() == modulenum).findFirst()
				.orElse(null);

		if (courseModules != null) {
			List<CourseLink> clinks = courseModules.getCourseLinks();

			if (!clinks.isEmpty()) {
				CourseLink courseLink = clinks.get(0);

				courseLink.getVideoLink().clear();
				courseLink.getVideoName().clear();

				if (courseLink.getVideoLink().isEmpty() && courseLink.getVideoName().isEmpty()) {
					clinks.remove(courseLink);
				}
			}

			ml.remove(courseModules);
			courses.setCourseModule(ml);
			cr.save(courses);

			return true;
		}

		return false;
	}

}
