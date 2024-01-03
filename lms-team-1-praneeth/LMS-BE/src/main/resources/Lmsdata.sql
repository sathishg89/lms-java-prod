-- Insert values into the courses table
-- password is abc123

INSERT INTO user (id, email, username, password, profilephoto, isactive, role)
VALUES
(1, 'jessie@digital-edify.com', 'jessie', '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NUll,'1','superadmin'),
('2', 'praneeth@gmail.com', 'praneeth',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'admin'),
('3', 'varun@gmail.com', 'varun',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'user'),
('4', 'raju@digital-edify.com', 'raju',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'admin'),
('5', 'ravi@digital-edify.com', 'ravi',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'admin'),
('6', 'farhath@gmail.com', 'farhath',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'user'),
('7', 'nithin@gmail.com', 'nithin',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'user'),
('8', 'ravikiran@gmail.com', 'ravikiran',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '0', 'user'),
('9', 'murali@gmail.com', 'murali',  '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '0', 'admin'),
('10', 'akhil@gmail.com', 'akhil', '$2a$12$lbwy2DULFi87.iqKiV6zduCnzM6hxdq5abaa1ID5HO/jADt7y4PMm', NULL, '1', 'user');


INSERT INTO courseusers (userid, useremail, username)
VALUES
('1001', 'yellaiah@gmail.com', 'yellaiah'),
('1002', 'mubeen@gmail.com', 'mubeen'),
('1003', 'ravikiran@gmail.com', 'ravikiran'),
('1004', 'nithin@gmail.com', 'nithin'),
('1005', 'varun@gmail.com', 'varun'),
('1006', 'farhath@gmail.com', 'farhath'),
('1007', 'praneeth@gmail.com', 'praneeth'),
('1008', 'raju@digital-edify.com', 'raju'),
('1009', 'ravi@digital-edify.com', 'ravi'),
('1010', 'murali@gmail.com', 'murali'),
('1011', 'akhil@gmail.com', 'akhil');

-- INSERT INTO cusg (next_val) VALUES ('1014');

INSERT INTO courses (courseid, coursecreatedate, courseimage, coursename, coursetrainer, archived, coursedescription)
VALUES
('101', '02-12-2023 07:13:32', NULL, 'fsbasics2303', 'deepak', '0', NULL),
('102', '02-12-2023 07:13:47', NULL, 'fsreact2301', 'raju', '1', NULL),
('103', '12-12-2023 03:16:07', NULL, 'fsreact2304', 'manideep', '0', NULL),
('104', '15-12-2023 11:26:16', NULL, 'fsreact2305', 'manideep', '1', NULL),
('105', '15-12-2023 11:26:39', NULL, 'fspython2301', 'tirikesh', '0', NULL),
('106', '15-12-2023 11:27:27', NULL, 'powerbi2302', 'deepak', '1', NULL),
('107', '15-12-2023 11:27:39', NULL, 'awsdevops2307', 'ravi', '0', NULL),
('108', '15-12-2023 11:27:46', NULL, 'awsdevops2308', 'mubeen', '1', NULL),
('109', '15-12-2023 11:27:55', NULL, 'azuredevops2301', 'mubeen', '0', NULL),
('110', '15-12-2023 11:28:01', NULL, 'azuredevops2303', 'ravi', '1', NULL),
('111', '15-12-2023 11:28:48', NULL, 'fscorejava2302', 'saikumar', '0', NULL),
('112', '15-12-2023 11:29:06', NULL, 'fscorejava2302', 'sajeed', '1', NULL),
('113', '21-12-2023 10:50:11', NULL, 'fsadvjava2303', 'sajeed', '0', NULL);

-- INSERT INTO csg (next_val) VALUES ('114');

INSERT INTO courses_users (fk_userid, fk_courseid)
VALUES
('1001', '102'),
('1001', '104'),
('1001', '108'),
('1007', '102'),
('1007', '104'),
('1002', '112'),
('1002', '101'),
('1003', '102'),
('1003', '108'),
('1004', '112'),
('1004', '101'),
('1005', '104'),
('1005', '112'),
('1006', '101'),
('1006', '108'),
('1007', '112');


INSERT INTO coursemodules (cmid, modulename, modulenumber, videoinserttime, fk_courseid)
VALUES
('1', 'routing', '2', '19-12-2023', '102'),
('2', 'usestate', '1', '19-12-2023', '102'),
('3', 'fs fundamentals 2', '2', '20-12-2023', '101'),
('4', 'fs fundamentals 1', '1', '20-12-2023', '101');


-- INSERT INTO cmsg (next_val) VALUES ('5');

INSERT INTO courselinks (linkid, fk_cmid)
VALUES
('1', '1'),
('2', '2'),
('3', '3'),
('4', '4');

INSERT INTO courselinks_link (fk_linkid, videolink)
VALUES
('1', 'link1'),
('1', 'link2'),
('2', 'link1'),
('2', 'link2'),
('2', 'https://www.youtube.com/embed/MKqJBhOgapM?si=NYjVVjoH8C_as8C4'),
('3', 'https://www.youtube.com/embed/5b36UTNRmtI?si=PBIhvskDD1aZ8oK-'),
('3', 'https://www.youtube.com/embed/8eVXTyIZ1Hs?si=OKI25yytfv42binv'),
('3', 'https://www.youtube.com/embed/9TycLR0TqFA?si=NpZ5XumzsrEEwevU'),
('3', 'https://www.youtube.com/embed/vMdSqMf6BPY?si=8zH0Kpqy3ViaDHIi'),
('4', 'https://www.youtube.com/embed/9TycLR0TqFA?si=NpZ5XumzsrEEwevU'),
('4', 'https://www.youtube.com/embed/8eVXTyIZ1Hs?si=OKI25yytfv42binv'),
('4', 'https://www.youtube.com/embed/vMdSqMf6BPY?si=8zH0Kpqy3ViaDHIi');


INSERT INTO courselinks_videoname (fk_linkid, videoname)
VALUES
('1', 'video1'),
('1', 'video2'),
('2', 'tags'),
('2', 'attributes'),
('2', 'youtube'),
('3', 'sdlc'),
('3', 'agile'),
('3', 'scrum'),
('3', 'git'),
('4', 'scrum'),
('4', 'agile'),
('4', 'git');
