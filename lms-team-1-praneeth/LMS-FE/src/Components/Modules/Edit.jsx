import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { url } from '../../utils';

const Edit = (props) => {
  const [courseData, setCourseData] = useState({});
  const [formData, setFormData] = useState({});


  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(`${url}admin/course/${props.courseName}/courseinfo`);
        setCourseData(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [props.courseName]);

  useEffect(() => {
    // Set initial form data based on the fetched data
    setFormData({
      courseName: courseData.courseName || '',
      courseTrainer: courseData.courseTrainer || '',
      description: courseData.courseDescription || '',
      courseImage: null
    });
  }, [courseData]);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    console.log(e.target.files[0]);
    const file = e.target.files[0];
    setFormData((prevData) => ({
      ...prevData,
      courseImage: file,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const formDataForBackend = new FormData();
      formDataForBackend.append('courseName', formData.courseName);
      formDataForBackend.append('courseTrainer', formData.courseTrainer);
      formDataForBackend.append('courseDescription', formData.description);
      formDataForBackend.append('archived', true);

      // Append courseImage only if it exists
      if (formData.courseImage) {
        formDataForBackend.append('courseImage', formData.courseImage);
      }

      const response = await axios.put(
        `${url}admin/course/updatecourse/${courseData.courseName}/${courseData.courseTrainer}`,
        formDataForBackend
      );

      console.log('Backend response:', response);
    } catch (error) {
      console.error('Error submitting form:', error);
    }
  };


  return (
    <div>
      <div className="col-sm-8 addcourse mx-auto text-start">
        <h1 className='fs-2 fw-bold'>Edit course</h1>
        <form onSubmit={handleSubmit}>
          <label className="mt-2 fw-bolder">Title</label>
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
            name="description"
            value={formData.description}
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

export default Edit;
