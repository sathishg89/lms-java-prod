-- Insert values into the courses table
-- password is abc123
INSERT INTO user (id, email, username, password, role, img, is_active)
VALUES
(1, 'jessie@digital-edify.com', 'jessie', '$2a$10$HHb8UqUNaLB4bWAkdWzKs.WoDuAiWm5QHhQq00kPiWLJpkHxRKQRa', 'superadmin', null, true),
(2, 'praneeth@gmail.com', 'praneeth', '$2a$10$Xp.ZX88BX2G/WcBe0wWZo.LtClA.DwRSjDCiOzWckihUobkh872m.', 'user', null, true),
(3, 'varun@gmail.com', 'varun', '$2a$10$mcvZYPO9HSL/luoIPFajU.AlEzRWBA58iy3kelx4oaeTnLRSkDBri', 'user', null, true),
(4, 'raju@digital-edify.com', 'raju', '$2a$10$lbqUCzxPwn.TNb7umLTmBeCAwdx/1hOOIQLTxEXxabjJv257oENm6', 'admin', null, true),
(5, 'ravi@digital-edify.com', 'ravi', '$2a$10$c9rtpTX8DOGoFi65hKF2Se5SADmiJQNRKb0mGzsMCidRj56sP9OfS', 'admin', null, true),
(6, 'farhath@gmail.com', 'farhath', '$2a$10$CMpmYyJIMcySe1yP57D55uTqzrqpmFHL2numt4wD4lkd3n0BQiLCy', 'user', null, true),
(7, 'nithin@gmail.com', 'nithin', '$2a$10$DGCAWkzJbSMM2.pLhYH4wOnbzg2DQGES/vlQ8v3CD/tT8mXvjn8ky', 'user', null, true);


INSERT INTO corseusers (userid, username, useremail) VALUES
(101, 'praneeth', 'praneeth@gmail.com'),
(102, 'varun', 'varun@gmail.com'),
(103, 'farhath', 'farhath@gmail.com'),
(104, 'nithin', 'nithin@gmail.com'),
(105,'raju','raju@digital-edify.com');

INSERT INTO courses (courseid, coursename,coursetrainer,archieved) VALUES
(1, 'fsbasics2301','saikumar',0),
(2, 'fsbasics2302','saikumar',0),
(3, 'fscorejava2301','sajeed',0),
(4, 'fsadvjava2302','trikesh',0),
(5, 'fsreact2301','manideep',0),
(6, 'fsreact2302','raju',0),
(7, 'awsdevops2301','ravi',0);

-- Insert values into the courseusers table


-- Insert values into the courses_users join table
INSERT INTO courses_users (fk_userid, fk_courseid) VALUES
(101, 1), -- praneeth enrolled in fsbasics2301
(102, 1), -- varun enrolled in fsbasics2301
(103, 1), -- farhath enrolled in fsbasics2301
(104, 1), -- nithin enrolled in fsbasics2301
(105, 1), -- raju enrolled in fsbasics2301
(101, 2), -- praneeth enrolled in fsbasics2302
(102, 2), -- varun enrolled in fsbasics2302
-- Add more entries for other courses as needed
(103, 3), -- farhath enrolled in fscorejava2301
(104, 4), -- nithin enrolled in fsadvjava2302
(105, 5), -- raju enrolled in fsreact2301
(101, 6), -- praneeth enrolled in fsreact2302
(102, 7); -- varun enrolled in awsdevops2301


INSERT INTO coursemodules (cmid,modulenum,videoinserttime,fk_courseid)
values 
(1,1,null,1),
(2,2,null,2),
(3,3,null,1),
(4,2,null,5),
(5,1,null,7);

INSERT INTO courselinks (linkid,courselinktable)
values (1,1),(2,2),(3,3),(4,1),(5,2);

INSERT INTO courselinks_link(fk_linkid,link)
values (1,'link1'),(1,'link2'),(2,'link1'),(2,'link2'),(2,'link3'),(3,'link1');

INSERT INTO courselinks_videoname(fk_linkid,videoname)
values (1,'html'),(1,'css'),(2,'js'),(2,'elements'),(2,'tags'),(3,'div');