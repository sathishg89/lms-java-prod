import React from 'react'
import { useState, useEffect } from 'react';
import axios from 'axios';
import Enrolled from './Enrolled';
import Resume from './Resume';
import Resources from './Resources';
import Edit from './Edit';
import '../AdminDashboard/AdminDashboard.css';
import { url } from '../../utils';



function MainModule(props) {
    const [selectedOption, setSelectedOption] = useState('Course Info');
    const [reload, setReload] = useState(true);
    const [moduleLength, setModuleLength] = useState(null);
    const [data, setData] = useState([]);
    const handleOptionClick = (option) => {
        setSelectedOption(option);
    };

    function updateData(moduleIndex) {
        console.log(data[moduleIndex].moduleNumber)
        const finalData = {
            moduleName: data[moduleIndex].moduleName,
            videoLink: Object.values(data[moduleIndex].videoInfo),
            videoName: Object.keys(data[moduleIndex].videoInfo)

        };
        console.log(finalData);
        const sendData = async () => {
            try {
                const response = await axios.put(`${url}admin/course/${props.selectedCourse.courseName}/${data[moduleIndex].moduleNumber}/updatemodules`, finalData);
                console.log(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        sendData();
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${url}admin/course/${props.selectedCourse.courseName}/${props.selectedCourse.courseTrainer}/getvideos`);
                const newData = response.data.sort((a, b) => a.moduleNumber - b.moduleNumber);

                setModuleLength(newData.length);

                // Check if data has changed before updating state
                if (JSON.stringify(newData) !== JSON.stringify(data)) {
                    setData(newData);
                }
            } catch (error) {
                console.error('Error fetching data:', error);
                setData([]);
                setModuleLength(0);
            }
        };

        fetchData();
    }, [props.selectedCourse.courseName, props.selectedCourse.courseTrainer, reload]);


    const [newModuleName, setNewModuleName] = useState('');
    const [tempvideoName, setVideoName] = useState('');
    const [tempVideoLink, setVideoLink] = useState('');
    const addModule = () => {
        const tempData = {
            moduleNumber: data?.length + 1,
            moduleName: newModuleName,
            videoInfo: {
                [tempvideoName]: tempVideoLink,
            }
        };

        setData((prevData) => [...prevData, tempData]);
        setModuleLength((prevLength) => prevLength + 1);
    };



    const saveModule = () => {
        const finalData = {
            courseName: props.selectedCourse.courseName,
            courseTrainer: props.selectedCourse.courseTrainer,
            moduleName: newModuleName,
            moduleNumber: data?.length + 1,
            videoName: [tempvideoName],
            videoLink: [tempVideoLink]

        };
        console.log(finalData);
        const sendData = async () => {
            try {
                const response = await axios.post(`${url}admin/course/savevideo`, finalData);
                console.log(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        sendData();
    }

    const deleteVideo = (moduleIndex, videoKey) => {
        const newData = [...data];
        newData[moduleIndex].videoInfo = { ...newData[moduleIndex].videoInfo };
        delete newData[moduleIndex].videoInfo[videoKey];
        setData(newData);

    };


    const addVideo = (moduleIndex) => {
        const newData = [...data];
        const videoKey = `Video ${Object.keys(newData[moduleIndex].videoInfo).length + 1}`;

        newData[moduleIndex].videoInfo = {
            ...newData[moduleIndex].videoInfo,
            [videoKey]: ''
        };
        setData(newData);
    };


    const deleteModule = (moduleIndex) => {
        // const newData = [...data];
        // newData.splice(moduleIndex, 1);
        // setData(newData);
        const deleteSelectedModule = async () => {
            try {
                const response = await axios.delete(`${url}admin/course/${props.selectedCourse.courseName}/${moduleIndex + 1}/deletemodule`);
                console.log(response.data);
                setReload((prevReload) => !prevReload);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        deleteSelectedModule();
    };


    const handleVideoNameChange = (moduleIndex, videoKey, newName) => {
        setData((prevData) => {
            const newData = [...prevData];
            newData[moduleIndex].videoInfo = { ...newData[moduleIndex].videoInfo };
            newData[moduleIndex].videoInfo[newName] = newData[moduleIndex].videoInfo[videoKey];
            delete newData[moduleIndex].videoInfo[videoKey];
            return newData;
        });
    };


    const handleVideoLinkChange = (moduleIndex, videoKey, newLink) => {
        const newData = [...data];
        newData[moduleIndex].videoInfo[videoKey] = newLink;
        setData(newData);
    };


    return (
        <div>
            <div className="options d-flex flex-row px-3">
                <p onClick={() => handleOptionClick('Course Info')}>Course Info</p>
                <p onClick={() => handleOptionClick('Modules')}>Modules</p>
                <p onClick={() => handleOptionClick('Projects')}>Projects</p>
                <p onClick={() => handleOptionClick('Resources')}>Resources</p>
                <p onClick={() => {
                    handleOptionClick('Enrolled');

                }}>Enrolled</p>
                <p onClick={() => handleOptionClick('Resume')}>Resume</p>
            </div>
            <div className="course px-3 text-start">



                {selectedOption === 'Course Info' && (
                    <div>
                        <p>
                            <b>Name</b>: {props.selectedCourse.courseName}
                        </p>
                        <p>
                            <b>Trainer Name</b>: {props.selectedCourse.courseTrainer}
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
                        <p className="ps-1" href="edit" onClick={() => handleOptionClick('editcourse')}>
                            Edit <i class="fa-solid fa-pen-to-square"></i>
                        </p>
                    </div>
                )}



                {selectedOption === 'Modules' && (
                    <div className="modules">
                        <div className='row'>
                            <div className='col-12 pb-4 d-flex justify-content-between'>
                                <h3>{props.selectedCourse.courseName}</h3>
                                <button className='btn btn-primary' type='button' data-bs-toggle="modal" data-bs-target="#exampleModal">Add Module</button>
                                <div class="modal fade" id="exampleModal" tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-body">
                                                <input type="text" className='col-12 p-2' placeholder='Module Name' onChange={(e) => setNewModuleName(e.target.value)} />
                                                <input type="text" className='col-5 p-2 mt-2' placeholder='Video Name' onChange={(e) => setVideoName(e.target.value)} />
                                                <input type="text" className='col-5 p-2 mt-1 ms-4' placeholder='Video Link' onChange={(e) => setVideoLink(e.target.value)} />
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                <button type="button" class="btn btn-primary" onClick={() => {
                                                    addModule();
                                                    saveModule();
                                                }} data-bs-dismiss="modal">Save Changes</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        {moduleLength === 0 ? <p>No moudles has been added</p> : <>{data?.map((singleModule, moduleIndex) => (
                            <div className='border p-2 mb-2' key={moduleIndex}>
                                <div className='row py-3'>
                                    {/* <h6 className='col-6 ps-4 m-0'>{singleModule.modulename}</h6> */}
                                    <input type="text" value={singleModule.moduleName} placeholder='Module Name' className='border-0 col-5 ps-2 ms-4' />
                                    <div className='col-6 text-end pe-4'>
                                        <button className='btn btn-s btn-primary' onClick={() => addVideo(moduleIndex)}>Add Video</button>
                                        <button className='btn btn-s btn-danger ms-2' onClick={() => deleteModule(moduleIndex)}>Delete Module</button>
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
                                {Object.keys(singleModule.videoInfo).map((videoKey, index) => (
                                    <div className='row ps-3 mb-4' key={index}>
                                        <div className='col-5'>
                                            <input className='p-1 col-11' type="text" value={videoKey} onChange={(e) => handleVideoNameChange(moduleIndex, videoKey, e.target.value)} />
                                        </div>
                                        <div className='col-5'>
                                            <input className='p-1 col-11' type="text" value={singleModule.videoInfo[videoKey]} onChange={(e) => handleVideoLinkChange(moduleIndex, videoKey, e.target.value)} />
                                        </div>
                                        <div className='col-2 d-flex align-items-center'>
                                            <i className="fa-solid fa-trash text-danger" style={{ cursor: 'pointer' }}
                                                onClick={() => deleteVideo(moduleIndex, videoKey)}
                                            ></i>
                                        </div>
                                    </div>
                                ))}
                                <div className='p-2 ps-3'>
                                    <button className='btn btn-primary' onClick={() => updateData(moduleIndex)}>save</button>
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
                    <Resources />
                )}



                {selectedOption === 'Enrolled' && (
                    <Enrolled selectedCourse={props.selectedCourse} />
                )}



                {selectedOption === 'Resume' && (
                    <Resume />
                )}

                {selectedOption === 'editcourse' && (
                    <Edit courseName={props.selectedCourse.courseName} />
                )}







            </div>
        </div>
    )
}

export default MainModule
