import React from 'react'
import '../AdminDashboard/AdminDashboard.css'
const AddingCourses = () => {
    return (
        <div>
            <div className="col-sm-8 addcourse mx-auto text-start">
                <h1 className='fs-2 fw-bold'>Create course</h1>
                <label className="mt-2  fw-bolder">Title</label>
                <input
                    className="w-100 p-3 px-3 enter"
                    type="text"
                    placeholder="Title"
                />
                <label className="mt-3 fw-bolder">Description</label>
                <textarea
                    id="description"
                    required
                    placeholder="Description"
                    rows="12"
                    className="w-100 p-2 px-3 enter"
                ></textarea>
                <label className="mt-2">Picture</label>
                <input className="w-100 file" type="file" />
                <p className="mt-3 mb-0">Current Picture</p>
                <button
                    type="submit"
                    className="mt-3 w-100"
                >
                    CREATE
                </button>
            </div>

        </div>
    )
}

export default AddingCourses
