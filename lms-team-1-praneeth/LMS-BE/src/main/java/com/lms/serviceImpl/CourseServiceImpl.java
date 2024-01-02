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
	private CourseUsersRepo cur;

	@Autowired
	private CoursesRepo cr;

	@Autowired
	private ResumeRepo rr;

	@Override
	public boolean saveCourseUser(CourseUsers courseUsers) {

		if (!cur.existsByuserEmail(courseUsers.getUserEmail())) {
			CourseUsers save = cur.save(courseUsers);
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

		if (cur.existsByuserEmail(userEmail)) {
			CourseUsers findByuserEmail = cur.findByuserEmail(userEmail);

			if (courseUsers != null && courseUsers.getUserEmail() != null) {
				findByuserEmail.setUserEmail(courseUsers.getUserEmail());
			}
			if (courseUsers != null && courseUsers.getUserName() != null) {
				findByuserEmail.setUserName(courseUsers.getUserName());
			}
			CourseUsers save = cur.save(findByuserEmail);
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
	public boolean saveCourses(Courses courses) {

		courses.setCourseCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));

		if (!cr.existsBycourseName(courses.getCourseName())) {
			Courses save = cr.save(courses);

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
	public boolean updateCourses(Courses courses, String courseName, String courseTrainer) {

		if (cr.existsBycourseName(courseName)) {

			List<Courses> lcourses = cr.findBycourseNameAndcourseTrainer(courseName, courseTrainer);

			if (!lcourses.isEmpty()) {
				Courses courses1 = lcourses.get(0);
				if (courses.getCourseName() != null && !courses.getCourseName().isEmpty()) {
					courses1.setCourseName(courses.getCourseName());

				}
				if (courses.getCourseTrainer() != null && !courses.getCourseTrainer().isEmpty()) {
					courses1.setCourseTrainer(courses.getCourseTrainer());

				}
				if (courses.getCourseDescription() != null && !courses.getCourseDescription().isEmpty()) {
					courses1.setCourseDescription(courses.getCourseDescription());

				}
				if (courses.getCourseImage() != null) {
					courses1.setCourseImage(courses.getCourseImage());
				}
				if (courses.isArchived() != false) {
					courses1.setArchived(courses.isArchived());
				}

				Courses save = cr.save(courses1);

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
	public boolean accessCouresToUser(String userEmail, String courseName, String courseTrainer) {

		boolean userExists = cur.existsByuserEmail(userEmail);
		boolean courseExists = cr.existsBycourseNameAndCourseTrainer(courseName, courseTrainer);

		if (userExists && courseExists) {

			CourseUsers fun = cur.findByuserEmail(userEmail);
			List<Courses> fcn = cr.findBycourseNameAndcourseTrainer(courseName, courseTrainer);

			Courses courses = fcn.get(0);

			List<CourseUsers> courseUsers = courses.getCourseUsers();

			Optional<CourseUsers> findFirst = courseUsers.stream().filter(x -> x.getUserEmail().equals(userEmail))
					.findFirst();

			if (findFirst.isPresent()) {
				throw new CustomException(CustomErrorCodes.USER_ALREADY_ENROLLED.getErrorMsg(),
						CustomErrorCodes.USER_ALREADY_ENROLLED.getErrorCode());
			} else {

				if (!fun.getCoursesList().containsAll(fcn)) {
					fun.getCoursesList().add(courses);
					cur.save(fun);
					return true;
				} else {
					return false;
				}

			}

		} else {
			throw new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
					CustomErrorCodes.INVALID_DETAILS.getErrorCode());
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
	public CourseUserDto getCourseUsers(String userEmail) {

		try {

			CourseUsers fun = cur.findByuserEmail(userEmail);

			CourseUserDto ucd = CourseUserDto.builder().userName(fun.getUserName()).userEmail(fun.getUserEmail())
					.coursesList(fun.getCoursesList()).build();

			return ucd;
		} catch (Exception e) {
			throw new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());
		}

	}

	@Override
	public List<CourseUsersInfoDto> getCourses(String courseName, String courseTrainer) {

		try {
			List<Courses> findByusername = cr.findBycourseName(courseName);

			List<CourseUsersInfoDto> collect = findByusername.stream()
					.filter(fil -> fil.getCourseTrainer().equals(courseTrainer))
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
	public boolean deleterCourseUser(String userEmail) {

		CourseUsers user = cur.findByuserEmail(userEmail);

		if (user != null) {
			user.getCoursesList().clear();

			cur.save(user);

			cur.delete(user);

			return true;
		}

		else {
			return false;
		}

	}

	@Override
	public boolean deleteCourse(String courseName, String courseTrainer) {

		List<Courses> findBycoursenameAndcoursetrainer = cr.findBycourseNameAndcourseTrainer(courseName, courseTrainer);

		if (!findBycoursenameAndcoursetrainer.isEmpty()) {
			Courses courses = findBycoursenameAndcoursetrainer.get(0);

			cr.delete(courses);
			return true;

		} else {
			return false;
		}

	}

	@Override
	public boolean removeCourseAccess(String userEmail, String courseName, String courseTrainer) {

		CourseUsers findByuserEmail = cur.findByuserEmail(userEmail);

		if (findByuserEmail != null) {
			List<Courses> coursesList = findByuserEmail.getCoursesList();

			coursesList.removeIf(course -> course.getCourseName().equals(courseName)
					&& course.getCourseTrainer().equals(courseTrainer));

			findByuserEmail.setCoursesList(coursesList);

			cur.save(findByuserEmail);
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
	public List<CourseModules> getCourseModules(String courseName, String courseTrainer) {

		boolean existsBycoursename = cr.existsBycourseName(courseName);

		if (existsBycoursename) {
			List<CourseModules> collect = cr.findCourseModulesByCourseName(courseName, courseTrainer);

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
	public boolean saveResume(String userEmail, MultipartFile resume) throws Exception {

		byte[] file = resume.getBytes();

		Resume r = Resume.builder().userEmail(userEmail).resume(file).build();

		CourseUsers findByuserEmail = cur.findByuserEmail(userEmail);

		if (findByuserEmail != null) {
			List<Resume> lresume = rr.findByUserEmail(userEmail);

			if (lresume.isEmpty()) {
				rr.save(r);
				return true;
			} else {
				lresume.get(0).setResume(resume.getBytes());
				return true;
			}

		} else {
			throw new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());

		}

	}

	@Override
	public byte[] getResume(String userEmail) {

		List<Resume> resume = rr.findByUserEmail(userEmail);

		if (resume.isEmpty()) {
			throw new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());

		} else {
			return resume.get(0).getResume();
		}

	}

	@Override
	public boolean deleteResume(String userEmail) {

		List<Resume> resume = rr.findByUserEmail(userEmail);

		if (resume.isEmpty()) {
			throw new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());
		} else {
			rr.delete(resume.get(0));
			return true;
		}

	}

	@Override
	public List<CourseModules> updateModule(String courseName, int moduleNumber, ModuleUpdateDto mud) {

		LocalDateTime now = LocalDateTime.now();

		Courses courses = cr.findBycourseName(courseName).get(0);

		if (courses == null) {

			throw new CustomException(CustomErrorCodes.COURSE_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.COURSE_NOT_FOUND.getErrorCode());

		}

		List<CourseModules> ml = courses.getCourseModule();

		Optional<CourseModules> optionalCourseModules = ml.stream().filter(x -> x.getModuleNumber() == moduleNumber)
				.findFirst();

		if (optionalCourseModules.isPresent()) {
			CourseModules courseModules = optionalCourseModules.get();

			courseModules.setModuleName(mud.getModuleName());
			courseModules.setModuleNumber(moduleNumber);
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
	public boolean deleteModule(String courseName, int moduleNumber) {
		Courses courses = cr.findBycourseName(courseName).get(0);

		List<CourseModules> ml = courses.getCourseModule();

		CourseModules courseModules = ml.stream().filter(x -> x.getModuleNumber() == moduleNumber).findFirst()
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
