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
	public boolean saveCourses(Courses course) {

		course.setCoursecreatedate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));

		if (!cr.existsBycoursename(course.getCoursename())) {
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

					.modulename(videoDto.getModulename())
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
	public CourseUserDto getCourseUsers(String courseUserEmail) {

		try {
			CourseUsers fun = ucr.findByuserEmail(courseUserEmail);

			CourseUserDto ucd = CourseUserDto.builder().username(fun.getUserName()).useremail(fun.getUserEmail())
					.courseslist(fun.getCoursesList()).build();

			return ucd;
		} catch (Exception e) {
			throw new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());
		}

	}

	@Override
	public List<CourseUsersInfoDto> getCourses(String courseName, String trainerName) {

		try {
			List<Courses> findByusername = cr.findBycoursename(courseName);

			List<CourseUsersInfoDto> collect = findByusername.stream()
					.filter(fil -> fil.getCoursetrainer().equals(trainerName))
					.map(c -> new CourseUsersInfoDto(c.getCourseid(), c.getCoursename(), c.getCoursetrainer(),
							c.getCoursecreatedate(), c.getCourseusers()))
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

			coursesList.removeIf(course -> course.getCoursename().equals(courseName)
					&& course.getCoursetrainer().equals(trainerName));

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

		List<CourseModules> collect;
		try {
			collect = cr.findCourseModulesByCourseName(courseName, trainerName);

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
	public CourseInfoDto getCourseInfo(String courseName) {

		List<Object[]> courseDetails = cr.getCourseDetails(courseName);

		List<Integer> modulenumList = courseDetails.stream().map(result -> (Integer) result[4])
				.collect(Collectors.toList());

		CourseInfoDto courseInfoDto = courseDetails.stream().findFirst()
				.map(result -> CourseInfoDto.builder().coursename((String) result[0]).coursetrainer((String) result[1])
						.courseimage((byte[]) result[2]).description((String) result[3]).modulenum(modulenumList)
						.build())
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

		Courses courses = cr.findBycoursename(courseName).get(0);

		List<CourseModules> ml = courses.getCoursemodule();

		CourseModules courseModules = ml.stream().filter(x -> x.getModulenum() == modulenum).findFirst().get();
		courseModules.setModulename(mud.getModulename());
		courseModules.setModulenum(modulenum);
		courseModules.setVideoinserttime(now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

		List<CourseLink> clinks = courseModules.getClinks();

		if (!clinks.isEmpty()) {

			List<String> links = clinks.get(0).getLinks();
			List<String> videos = clinks.get(0).getVideoname();

			CourseLink courseLink = clinks.get(0);
			List<CourseLink> clinks1 = new ArrayList<>();

			if (mud.getVideolink() != null && mud.getVideoname() != null) {

				clinks.remove(courseLink);
				links.clear();
				videos.clear();

				courseLink.setLinks(mud.getVideolink());
				courseLink.setVideoname(mud.getVideoname());
				clinks1.add(courseLink);
				courseModules.setClinks(clinks1);

			} else {
				courseModules.setClinks(clinks);
			}

		}

		Courses save = null;
		if (courseModules != null) {

			ml.remove(courseModules);
			ml.add(courseModules);
			courses.setCoursemodule(ml);

			save = cr.save(courses);

		}
		return save.getCoursemodule();
	}

	@Override
	public boolean deleteModule(String courseName, int modulenum) {
		Courses courses = cr.findBycoursename(courseName).get(0);

		List<CourseModules> ml = courses.getCoursemodule();

		CourseModules courseModules = ml.stream().filter(x -> x.getModulenum() == modulenum).findFirst().orElse(null);

		if (courseModules != null) {
			List<CourseLink> clinks = courseModules.getClinks();

			if (!clinks.isEmpty()) {
				CourseLink courseLink = clinks.get(0);

				courseLink.getLinks().clear();
				courseLink.getVideoname().clear();

				if (courseLink.getLinks().isEmpty() && courseLink.getVideoname().isEmpty()) {
					clinks.remove(courseLink);
				}
			}

			ml.remove(courseModules);
			courses.setCoursemodule(ml);
			cr.save(courses);

			return true;
		}

		return false;
	}

}
