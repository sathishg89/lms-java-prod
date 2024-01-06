import React from 'react'
import '../AdminDashboard/AdminDashboard.css';
const Resume = () => {
    return (
        <div className="resume px-3">
            <p className="common">
                No resume
                templates are updated for this course.
            </p>
            <label for="">Upload new Resume</label> <br />
            <input type="file" name="" id="" className="mt-2 file" /> <br />
            <button className="upload mt-3">Upload</button>
        </div>
    )
}

export default Resume
