-- Insert values into the courses table
-- password is abc123

INSERT INTO user (id, email, username, password, img, is_active, role)
VALUES
(1, 'jessie@digital-edify.com', 'jessie', '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NUll,'1','superadmin'),
('2', 'praneeth@gmail.com', 'praneeth',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'user'),
('3', 'varun@gmail.com', 'varun',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'user'),
('4', 'raju@digital-edify.com', 'raju',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'admin'),
('5', 'ravi@digital-edify.com', 'ravi',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'admin'),
('6', 'farhath@gmail.com', 'farhath',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'user'),
('8', 'nithin@gmail.com', 'nithin',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'user'),
('40', 'ravikiran@gmail.com', 'ravikiran',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '0', 'user'),
('41', 'murali@gmail.com', 'murali',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '0', 'user'),
('43', 'akhil@gmail.com', 'akhil', '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'user');


INSERT INTO corseusers (userid, useremail, username)
VALUES
('1007', 'yellaiah@gmail.com', 'yellaiah'),
('1008', 'mubeen@gmail.com', 'mubeen'),
('1018', 'ravikiran@gmail.com', 'ravikiran'),
('1020', 'nithin@gmail.com', 'nithin'),
('1021', 'varun@gmail.com', 'varun'),
('1022', 'farhath@gmail.com', 'farhath'),
('1023', 'praneeth@gmail.com', 'praneeth');

INSERT INTO cusg (next_val) VALUES ('1034');

INSERT INTO courses (courseid, coursecreatedate, courseimage, coursename, coursetrainer, archived, description)
VALUES
('103', '02-12-2023 07:13:32', NULL, 'fsbasics2303', 'deepak', '0', NULL),
('104', '02-12-2023 07:13:47', NULL, 'fsreact2301', 'raju', '1', NULL),
('110', '12-12-2023 03:16:07', NULL, 'fsreact2304', 'manideep', '0', NULL),
('112', '15-12-2023 11:26:16', NULL, 'fsreact2305', 'manideep', '1', NULL),
('113', '15-12-2023 11:26:39', NULL, 'fspython2301', 'tirikesh', '0', NULL),
('114', '15-12-2023 11:27:27', NULL, 'powerbi2302', 'deepak', '1', NULL),
('115', '15-12-2023 11:27:39', NULL, 'awsdevops2307', 'ravi', '0', NULL),
('116', '15-12-2023 11:27:46', NULL, 'awsdevops2308', 'mubeen', '1', NULL),
('117', '15-12-2023 11:27:55', NULL, 'azuredevops2301', 'mubeen', '0', NULL),
('118', '15-12-2023 11:28:01', NULL, 'azuredevops2303', 'ravi', '1', NULL),
('119', '15-12-2023 11:28:48', NULL, 'fscorejava2302', 'saikumar', '0', NULL),
('120', '15-12-2023 11:29:06', NULL, 'fscorejava2302', 'sajeed', '1', NULL),
('121', '21-12-2023 10:50:11', NULL, 'fsadvjava2303', 'sajeed', '0', NULL);

INSERT INTO csg (next_val) VALUES ('122');

INSERT INTO courses_users (fk_userid, fk_courseid)
VALUES
('1007', '104'),
('1007', '112'),
('1007', '116'),
('1021', '104'),
('1021', '112'),
('1021', '120'),
('1022', '103'),
('1022', '104'),
('1022', '116'),
('1022', '120'),
('1023', '103'),
('1023', '112'),
('1023', '120'),
('1008', '103'),
('1018', '116'),
('1018', '120');


INSERT INTO coursemodules (cmid, modulename, modulenum, videoinserttime, fk_courseid)
VALUES
('13', 'updatredmodule1225', '2', '19-12-2023', '104'),
('35', 'intro to js', '1', '19-12-2023', '104'),
('37', 'fs fundamentals 2', '2', '20-12-2023', '103'),
('38', 'fs fundamentals 1', '1', '20-12-2023', '103');


INSERT INTO cmsg (next_val) VALUES ('39');

INSERT INTO courselinks (linkid, fk_cmid)
VALUES
('25', '13'),
('50', '35'),
('52', '37'),
('53', '38');

INSERT INTO courselinks_link (fk_linkid, link)
VALUES
('25', 'link1'),
('25', 'link2'),
('50', 'link1'),
('50', 'link2'),
('50', 'https://www.youtube.com/embed/MKqJBhOgapM?si=NYjVVjoH8C_as8C4'),
('52', 'link1'),
('52', 'link2'),
('52', 'link3'),
('52', 'link4'),
('53', 'link1'),
('53', 'link2'),
('53', 'link3');


INSERT INTO courselinks_videoname (fk_linkid, videoname)
VALUES
('25', 'video1'),
('25', 'video2'),
('50', 'tags'),
('50', 'attributes'),
('50', 'youtube'),
('52', 'sdlc'),
('52', 'agile'),
('52', 'scrum'),
('52', 'git'),
('53', 'scrum'),
('53', 'agile'),
('53', 'git');
