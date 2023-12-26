import React, { useEffect } from 'react'
import Header from '../Header'
import './CourseDashboard.css'
import { useState } from 'react'
import axios from 'axios'
import { useParams } from 'react-router-dom';
import { url } from '../../utils'
const CourseDashboard = () => {
  const [data, setData] = useState(null);
  const userEmail = localStorage.getItem('Email');
  const { courseName, trainerName } = useParams();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(`${url}admin/course/${courseName}/${trainerName}/getvideos`);
        console.log(response);
        setData(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData();
  }, [userEmail, courseName, trainerName]);


  const [videoLink, setvideoLink] = useState("");
  return (
    <div>
      <Header />
      <h1 className='mt-5'>{courseName}</h1>
      <div className='container-fluid d-flex'>
        <div className='col-3' style={{ overflowY: 'scroll', maxHeight: '70vh' }}>
          <div className="accordion" id="accordionPanelsStayOpenExample">
            {data?.map((singleMoudle, index) => (
              <div className="accordion-item">
                <h2 className="accordion-header" id={`panelsStayOpen-heading${index}`}>
                  <button className="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target={`#panelsStayOpen-collapse${index}`} aria-expanded="false" aria-controls={`panelsStayOpen-collapse${index}`}>
                    <p className='bg-primary rounded-circle m-0 px-2 py-1'>{index + 1}</p>
                    <p className='m-0 ms-1'>{singleMoudle.modulename}</p>
                  </button>
                </h2>
                <div id={`panelsStayOpen-collapse${index}`} className="accordion-collapse collapse" aria-labelledby={`panelsStayOpen-heading${index}`}>
                  {Object.keys(singleMoudle.videos).map((videoKey, index) => (
                    <div className="accordion-body border" key={`video${index}${videoKey}`} style={{ cursor: 'pointer' }} onClick={() => setvideoLink(singleMoudle.videos[videoKey])}>
                      <p className='m-0 text-start'>{videoKey}</p>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </div>
        <div className='col-9 d-flex align-items-center justify-content-center' style={{ height: '75vh' }}>
          {videoLink ? <iframe className='p-3' width={'100%'} height={'100%'} src={videoLink} title="YouTube video player" frameBorder="0" allow="accelerometer; encrypted-media;" allowFullScreen></iframe> : <h4>Select the topic for display</h4>}
        </div>
      </div>
    </div >

  )
}

export default CourseDashboard
