import React from 'react'
import { Link } from 'react-router-dom'
const Users = () => {
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
                            placeholder="student_name@digital-lync.com"
                            name="courseName" />
                    </div>
                    <button className='upload mt-3'>Find User</button>
                </div>
            </div>
        </div>
    )
}
export default Users