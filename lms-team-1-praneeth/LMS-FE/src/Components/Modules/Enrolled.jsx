import React from 'react'
import { useState, useEffect } from 'react';
import axios from 'axios';
import '../AdminDashboard/AdminDashboard.css';
import { url } from '../../utils';

const Enrolled = (props) => {
    const [allusers, setAllUsers] = useState({});
    const [userLength, setUserLength] = useState(null);
    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get(`${url}admin/course/getcourseusers/${props.selectedCourse.courseName}/${props.selectedCourse.courseTrainer}`);
                setAllUsers(response.data[0]);
                setUserLength(response.data[0].courseusers.length);
            } catch (error) {
                console.error('Error fetching data:', error);
                setUserLength(0);
            }
        };
        fetchUserData();
    }, [props.selectedCourse.courseName, props.selectedCourse.courseTrainer]);

    return (
        <div className="enrolled" style={{ padding: '20px 100px' }}>
            <div className=''>
            <h2>Add user to FS-Basics 9AM</h2>
            <input className='w-50 p-2 m-3' type='text'placeholder='Email address' />
            <button className='text-white bg-primary pt-2 pb-2 ps-5 pe-5 '>Enroll</button>
            </div>
            {userLength === 0 ? <p>Add user to showcase</p> :
            
                <table className='table table-bordered table-hover'>
                    
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Remove Access</th>
                        </tr>
                    </thead>
                    <tbody>
                        {allusers?.courseusers?.map((singleUser, index) => (
                            <tr>
                                <td>{singleUser.userName}</td>
                                <td>{singleUser.userEmail}</td>
                                <td><i className="fa-solid fa-trash text-danger" style={{ cursor: 'pointer' }}></i></td>
                            </tr>
                        ))}
                    </tbody>
                </table>}
        </div>
    )
};
export default Enrolled;