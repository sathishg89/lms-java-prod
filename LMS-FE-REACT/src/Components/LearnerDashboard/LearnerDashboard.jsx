import React from 'react'
import Header from '../Header'
import './LearnerDashboard.css';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';
const Dashboard = () => {
  const courses = useSelector((state) => state.login.userCourses?.coursesList)
  return (
    <div>
      <Header />
      <div className='banner text-white'><h1>Welcome to Kona LMS🚀</h1></div>
      <div className='container'>
        <div className='courses text-start mt-4'><h1>Your Courses</h1></div>
        {courses?.map((course) => (
          <div className='d-flex bg-light mb-5' style={{ boxShadow: 'rgba(100, 100, 111, 0.2) 0px 7px 29px 0px', }}>
            <div className='thumbnail p-2'>
              <img src={`data:image/jpeg;base64,${course.courseImage}`} alt="Thumbnail" width={'300px'} />
            </div>
            <div className='flex-grow-1 text-start'>
              <h1 className='ps-3'>{course.courseName}</h1>
              <p className='ps-3'>{course.courseTrainer}</p>
            </div>
            <div>
              <Link to={`/coursedashboard/${course.courseName}/${course.courseTrainer}`}><button className='btn btn-primary mt-5 p-2 px-3 m-3'>Continue <i class="fa-solid fa-arrow-right ms-1"></i></button><br /></Link>
              <button className='btn btn-white border mt-2 p-2 px-3'>Live class <i class="fa-solid fa-video ms-1"></i></button>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
export default Dashboard;