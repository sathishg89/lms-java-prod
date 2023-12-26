import React, { useState, useEffect } from 'react';
import Header from '../Header';
import './AdminDashboard.css';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { url } from '../../utils';
import MainModule from '../Modules/MainModule';
import AddingCourses from '../AddingCourses/AddingCourses';
const AdminDashboard = () => {
    const [selectedOption, setSelectedOption] = useState(null);
    const [selectedCourse, setSelectedCourse] = useState('');
    const [allCourses, setAllCourses] = useState([]);
    const [searchInput,setSearchInput]=useState('');
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${url}admin/course/getallcourses`);
                console.log(response);
                setAllCourses(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, []);
    const filteredCourses = allCourses.filter((course) => {
        const courseName = course.courseName.toLowerCase();
        return courseName.includes(searchInput.toLowerCase());
    });                       
    return (
        <div>
            <Header/>
            <div className="container-fluid">
                <div className="row">
                    <div className="col-sm-2 nav">
                        <div className="row left">
                            <a className="col-sm-8 courses" href="courses">
                                Courses
                            </a>
                            <button onClick={() => {
                                setSelectedOption('addcourse');
                                setSelectedCourse(false);
                            }} className="col-sm-4 add">+ ADD</button>
                        </div>

                        <div className="row left">
                            <a className="col-sm-8 users" href="users">
                                Users
                            </a>
                        </div>
                    </div>
                    <div className="col-sm-2 list">
                        <input
                            className="search"
                            type="text"
                            placeholder="Search Courses"
                         value={searchInput}
                         onChange={(e)=> setSearchInput(e.target.value)}
                         />
                        {filteredCourses.map((singleCourse, index) => (
                            <div className="title" key={index}>
                                <Link onClick={() => {
                                    setSelectedCourse(singleCourse);
                                    setSelectedOption(null);
                                }}><p>{singleCourse.courseName}</p></Link>
                            </div>
                        ))}
                    </div>
                    <div className="editcourse col-sm-7 mt-5 ms-2 ps-2">
                        {selectedCourse ? <MainModule selectedCourse={selectedCourse} /> : <></>}
                        {selectedOption === 'addcourse' ? <AddingCourses/> : <></>}

                    </div>

                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
