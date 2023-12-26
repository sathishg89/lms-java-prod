import React from 'react'
import { useState, useEffect } from 'react';
import axios from 'axios';
import '../AdminDashboard/AdminDashboard.css';
import { url } from '../../utils';
const MainModule = (props) => {
    const [selectedOption, setSelectedOption] = useState('Course Info');
    const [moduleLength, setModuleLength] = useState(null);
    const [data, setData] = useState([]);
    const handleOptionClick = (option) => {
        setSelectedOption(option);
    };
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${url}admin/course/${props.selectedCourse.courseName}/${props.selectedCourse.trainerName}/getvideos`);
                setData(response.data);
                setModuleLength(response.data.length);
            } catch (error) {
                console.error('Error fetching data:', error);
                setModuleLength(0);
            }
        };
        fetchData();
    }, [props.selectedCourse.courseName, props.selectedCourse.trainerName]);
    console.log(data);
    const setnamefor = (mname, index) => {
        data[index].modulename = mname;

    }
    const [inputModuleName, setInputmodulename] = useState("");
    const addModule = () => {
        const tempData = {
            modulenum: data?.length,
            modulename: '',
            videos: {}
        }
        setData((data) => [...data, tempData])
    }
    const deleteVideo = (moduleIndex, videoKey) => {
        const newData = [...data];
        newData[moduleIndex].videos = { ...newData[moduleIndex].videos };
        delete newData[moduleIndex].videos[videoKey];
        setData(newData);
    };
    const addVideo = (moduleIndex) => {
        const newData = [...data];
        const videoKey = `Video ${Object.keys(newData[moduleIndex].videos).length + 1}`;
        
        newData[moduleIndex].videos = {
            ...newData[moduleIndex].videos,
            [videoKey]: ''
        };
        
        setData(newData);
    };
    

    const deleteModule = (moduleIndex) => {
        const newData = [...data];
        newData.splice(moduleIndex, 1);
        setData(newData);
    };



    return (
        <div>
            <div className="options d-flex flex-row px-3">
                <p onClick={() => handleOptionClick('Course Info')}>Course Info</p>
                <p onClick={() => handleOptionClick('Modules')}>Modules</p>
                <p onClick={() => handleOptionClick('Projects')}>Projects</p>
                <p onClick={() => handleOptionClick('Resources')}>Resources</p>
                <p onClick={() => handleOptionClick('Enrolled')}>Enrolled</p>
                <p onClick={() => handleOptionClick('Resume')}>Resume</p>
            </div>
            <div className="course px-3 text-start">
                {selectedOption === 'Course Info' && (
                    <div>
                        <p>
                            <b>Name</b>: {props.selectedCourse.courseName}
                        </p>
                        <p>
                            <b>Trainer Name</b>: {props.selectedCourse.trainerName}
                        </p>
                        <p>
                            <b>Modules</b>: {moduleLength}
                        </p>
                        <p className="w-75">
                            <b>Live Class Name</b>: NA
                        </p>
                        <p className="classNamelink">
                            <input className="col-sm-6 p-2" type="text" />
                            <button className='btn-primary p-2 ms-2'>Save Live Class Name Link</button>
                        </p>
                        <p className="archive">
                            <button> <i class="fa-solid fa-box-archive"></i>Archive</button>
                        </p>
                        <a className="ps-1" href="edit">
                            Edit <i class="fa-solid fa-pen-to-square"></i>
                        </a>
                    </div>
                )}
                {selectedOption === 'Modules' && (
                    <div className="modules">
                        <div className='row'>
                            <div className='col-12 pb-4 d-flex justify-content-between'>
                                <h3>{props.selectedCourse.courseName}</h3>
                                <button className='btn btn-primary' onClick={addModule}>Add Module</button>
                            </div>
                        </div>
                        {moduleLength === 0 ? <p>No moudles has been added</p> : <>{data?.map((singleModule, moduleIndex) => (
                            <div className='border p-2 mb-2'>
                                <div className='row py-3'>
                                    {/* <h6 className='col-6 ps-4 m-0'>{singleModule.modulename}</h6> */}
                                    <input type="text" value={singleModule.modulename} placeholder='Module Name' onChange={($event, index) => {
                                        setnamefor($event, index);
                                    }} className='border-0 col-5 ps-2 ms-4' />
                                    <div className='col-6 text-end pe-4'>
                                        <button className='btn btn-s btn-primary' onClick={()=>addVideo(moduleIndex)}>Add Video</button>
                                        <button className='btn btn-s btn-danger ms-2'onClick={()=>deleteModule(moduleIndex)}>Delete Module</button>
                                    </div>
                                </div>
                                <div className='row ps-3'>
                                    <div className='col-5'>
                                        <p>Video Name</p>
                                    </div>
                                    <div className='col-5'>
                                        <p>Video Link</p>
                                    </div>
                                </div>
                                {Object.keys(singleModule.videos).map((videoKey, index) => (
                                    <div className='row ps-3 mb-4' key={index}>
                                        <div className='col-5'>
                                            <input className='p-1 col-11' type="text" value={videoKey} />
                                        </div>
                                        <div className='col-5'>
                                            <input className='p-1 col-11' type="text" value={singleModule.videos[videoKey]} />
                                        </div>
                                        <div className='col-2 d-flex align-items-center'>
                                            <i className="fa-solid fa-trash text-danger" style={{ cursor: 'pointer' }}
                                         onClick={() => deleteVideo(moduleIndex, videoKey)}

                                            ></i>
                                        </div>
                                    </div>
                                ))}
                                <div className='p-2 ps-3'>
                                    <button className='btn btn-primary'>save</button>
                                </div>
                            </div>
                        ))}</>}
                    </div>
                )}
                {selectedOption === 'Projects' && (
                    <div className="projects px-3">
                        <p className="common">
                            No projects found. You can add projects by uploading them using the
                            form below.
                        </p>
                        <label for="">Upload new Project</label> <br />
                        <input type="file" name="" id="" className="mt-2 file" /> <br />
                        <button className="upload mt-3">Upload</button>
                    </div>
                )}
                {selectedOption === 'Resources' && (
                    <div className="resources px-3">
                        <p className="common">
                            No resources found. You can add resources by uploading them using the
                            form below.
                        </p>
                        <label for="">Upload new Resource</label> <br />
                        <input type="file" name="" id="" className="mt-2 file" /> <br />
                        <button className="upload mt-3">Upload</button>
                    </div>

                )}
                {selectedOption === 'Enrolled' && (
                    <div className="enrolled px-3">
                        <p>Backend students data Table</p>
                    </div>

                )}
                {selectedOption === 'Resume' && (
                    <div className="resume px-3">
                        <p className="common">
                            No resume
                            templates are updated for this course.
                        </p>
                        <label for="">Upload new Resume</label> <br />
                        <input type="file" name="" id="" className="mt-2 file" /> <br />
                        <button className="upload mt-3">Upload</button>
                    </div>

                )}
            </div>
        </div>
    )
}

export default MainModule
