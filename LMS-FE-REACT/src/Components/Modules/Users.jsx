import React, { useState } from 'react'
import { Link } from 'react-router-dom';
import { url } from '../../utils';
import axios from 'axios';
const Users = () => {
    const [userEmail, setUserEmail] = useState(null);
    const [userInfo, setUserInfo] = useState(null);
    const [userFlag, setUserFlag] = useState()
    const getUserInfo = () => {
        console.log(userEmail);
        axios.get(`${url}admin/course/getcourseuserinfo/${userEmail}`).then(response => {
            setUserInfo(response.data);
            setUserFlag(1);
        }).catch(error => {
            console.log(error);
            setUserFlag(0);
            setUserInfo(null);
        });
    }
    return (
        <div className='users ps-5'>
            <div className='' style={{ paddingLeft: '300px' }}>
                <h3>Import Users</h3>
                <div className='text-start'>
                    <p className='fw-bolder'>Upload users file</p>
                    <Link>Sample-users.csv</Link>
                    <div>
                        <input type="file" name="" id="" className="mt-2 file" /> <br />
                        <button className="upload mt-3">Upload</button>
                    </div>
                </div>
                <h3 className='mt-5'>Find User Information</h3>
                <div className='text-start'>
                    <div>
                        <p className='fw-bolder'>Enter user email</p>
                        <input
                            className="w-100 p-3 px-3 enter"
                            type="text"
                            placeholder="Email"
                            name="courseName"
                            onChange={(e) => setUserEmail(e.target.value)} />
                    </div>
                    <button className='upload mt-3' onClick={getUserInfo}>Find User</button>
                    {userInfo ?
                        <table className='table table-bordered mt-5'>
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th colSpan={userInfo.coursesList.length}>Courses Enrolled</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{userInfo.userName}</td>
                                    <td>{userInfo.userEmail}</td>
                                    {userInfo.coursesList.map(singleCourse => (
                                        <td>{singleCourse.courseName}</td>
                                    ))}
                                </tr>
                            </tbody>
                        </table> : <></>
                    }
                    {userFlag === 0 ? <p>user not found</p> : <></>}
                </div>
            </div>
        </div>
    )
}
export default Users