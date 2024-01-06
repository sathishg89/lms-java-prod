import React from 'react'
import '../AdminDashboard/AdminDashboard.css';
const Resources = () => {
    return (
        <div className="resources px-3">
            <p className="common">
                No resources found. You can add resources by uploading them using the
                form below.
            </p>
            <label for="">Upload new Resource</label> <br />
            <input type="file" name="" id="" className="mt-2 file" /> <br />
            <button className="upload mt-3">Upload</button>
        </div>
    )
}

export default Resources
