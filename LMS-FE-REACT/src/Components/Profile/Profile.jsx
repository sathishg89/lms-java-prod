import React, { useState, useEffect } from 'react';
import Header from '../Header';
import { useSelector, useDispatch } from 'react-redux';  // Updated import statement
import axios from 'axios';
import { url } from '../../utils';
import './Profile.css';
const Profile = () => {
  const dispatch = useDispatch();

  const loginState = useSelector((state) => state.login);
  const [name, setName] = useState(loginState.name);
  const [email, setEmail] = useState(loginState.email);
  const [image, setImage] = useState(null);
  const [newPassword, setNewPassword] = useState('');

  // useEffect(() => {
  // Update header with new profile picture and login credentials
  //   dispatch({ type: 'UPDATE_PROFILE', payload: { name, email, image } });
  // }, [name, email, image, dispatch]);

  // const handleFileChange = (e) => {
  //   const file = e.target.files[0];
  //   setImage(file);
  // };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    console.log('Selected File:', file);
    setImage(null);
    setImage(file);
  };

  const handleSave = async () => {
    try {
      const formData = new FormData();
      formData.append('userName', name);
      formData.append('userEmail', email);

      const response = await axios.put(`${url}user/update/${localStorage.getItem('Email')}`, formData);
      if (response.status === 200) {
        console.log('Data changed successfully');
      } else {
        console.error('Error changing data');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const uploadImage = async () => {
    const formImgData = new FormData();
    formImgData.append('photo', image);
    console.log('Image State:', image);

    try {
      const response = await axios.post(`${url}user/uploadimage/${localStorage.getItem('Email')}`, formImgData);
      if (response.status === 200) {
        console.log('Data changed successfully');
      } else {
        console.error('Error changing data');

      }
    } catch (error) {
      console.error('Error:', error);
    }

  };

  const handleResetPassword = async () => {
    try {
      const resetPasswordData = {
        userEmail: email,
        newPassword: newPassword,
      };
      const response = await axios.post(`${url}user/resetpassword`, resetPasswordData);
      if (response.status === 200) {
        console.log('Password reset successful');
      } else {
        console.error('Error resetting password');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };
  return (
    <div>
      <Header />
      <div className='container pt-5'>
        <div className='col-sm-5 m-auto text-start'>
          <h3 className='h3'>Edit Profile</h3>
          <p className='name mb-0'>Name:</p>
          <input
            type='text'
            className='form p-2 mb-4 col-12'
            placeholder='Name'
            name='name'
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <p className='name mb-0'>Email:</p>
          <input
            type='email'
            className='form p-2 col-12 mb-3'
            placeholder='name@example.com'
            name='email'
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <p className='picture'>Profile Picture:</p>
          <div className='bg-white col-4' style={{ height: '120px', backgroundImage: `url(${image && URL.createObjectURL(image)})`, backgroundPosition: 'center', backgroundSize: 'cover', backgroundRepeat: 'no-repeat' }}></div>
          <input
            type='file'
            className='file btn-sm mb-3 '
            accept='image/png, image/jpeg'
            name='picture'
            onChange={handleFileChange}
          />
          <button className='btn btn-primary pb-3 ms-3 pt-3' onClick={uploadImage}>upload Image</button>
          <div className='buttons'>
            <button className='bt1 p-2 col-12 border-0 text-white' onClick={handleSave}>
              Save
            </button>
            <br></br>
            <br></br>
            <button className='bt2 p-2 col-12 border-0 text-white' onClick={handleResetPassword}>Reset Password</button>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Profile;