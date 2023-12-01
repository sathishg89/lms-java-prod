import React from 'react'
import Header from '../Header'
import './Dashboard.css';
const Dashboard = () => {          
  return (
    <div>
      <Header/>
      <div className='banner'></div>
      <div className='container'>
        <div><h1>Your Courses</h1></div>
        <div className='d-flex bg-light mb-5' style={{boxShadow: 'rgba(100, 100, 111, 0.2) 0px 7px 29px 0px',}}>
          <div className='thumbnail p-2'>
            <img src="https://www.thestreet.com/.image/c_limit%2Ccs_srgb%2Cq_auto:good%2Cw_700/MTg4MjUzMDg2ODIzNzUzMTU2/fundamentals-3.webp" alt="Thumbnail" width={'300px'}/>
          </div>
          <div className='flex-grow-1'>
            <h1>Course Name</h1>
            <p>Trainer Name</p>
          </div>
          <div>
            <button  className='btn-primary mt-5 p-2 m-3'>Continue <i class="fa-solid fa-arrow-right"></i></button><br/>
            <button clas className='btn-white mt-2 p-2'>Live class <i class="fa-solid fa-video"></i></button>

          </div>
        </div>
      </div>
    </div>
  )
}
export default Dashboard;