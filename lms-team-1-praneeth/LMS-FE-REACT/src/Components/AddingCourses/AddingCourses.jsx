import '../AdminDashboard/AdminDashboard.css'
import React, { useState } from 'react';
import axios from 'axios';
import { url } from '../../utils';

const AddingCourses = ({ onReload }) => {
    const [formData, setFormData] = useState({
        courseName: '',
        courseTrainer: '',
        courseDescription: '',
        courseImage: null,
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        // Do whatever you need with the file, such as uploading it to the server
        setFormData((prevData) => ({
            ...prevData,
            courseImage: file,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const headers = {
                Authorization: `Bearer ${localStorage.getItem('JWT Token')}`,
            };
            const backFormData = new FormData();
            backFormData.append('courseName', formData.courseName);
            backFormData.append('courseTrainer', formData.courseTrainer);
            backFormData.append('courseDescription', formData.courseDescription);

            if (formData.courseImage) {
                backFormData.append('courseImage', formData.courseImage);
            }

            const response = await axios.post(`${url}admin/course/addcourse`, backFormData, { headers });

            console.log('Backend response:', response.data);
            onReload();
        } catch (error) {
            console.error('Error submitting form:', error);
        }
    };


    return (
        <div>
            <div className="col-sm-8 addcourse mx-auto text-start">
                <h1 className='fs-2 fw-bold'>Create course</h1>
                <form onSubmit={handleSubmit}>
                    <label className="mt-2  fw-bolder">Title</label>
                    <input
                        className="w-100 p-3 px-3 enter"
                        type="text"
                        placeholder="Title"
                        name="courseName"
                        value={formData.courseName}
                        onChange={handleChange}
                    />

                    <label className="mt-3 fw-bolder">Trainer</label>
                    <input
                        className="w-100 p-3 px-3 enter"
                        type="text"
                        placeholder="Trainer"
                        name="courseTrainer"
                        value={formData.courseTrainer}
                        onChange={handleChange}
                    />

                    <label className="mt-3 fw-bolder">Description</label>
                    <textarea
                        id="description"
                        placeholder="Description"
                        rows="12"
                        className="w-100 p-2 px-3 enter"
                        name="courseDescription"
                        value={formData.courseDescription}
                        onChange={handleChange}
                    ></textarea>

                    <label className="mt-2">Picture</label>
                    <input
                        className="w-100 file"
                        type="file"
                        onChange={handleFileChange}
                    />

                    <p className="mt-3 mb-0">Current Picture</p>

                    <button type="submit" className="mt-3 w-100">
                        CREATE
                    </button>
                </form>
            </div>
        </div>
    );
};

export default AddingCourses;
