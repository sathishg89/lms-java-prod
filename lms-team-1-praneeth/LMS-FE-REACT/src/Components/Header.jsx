import React from 'react';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux/es/hooks/useSelector';
import './Header.css';
function Header() {
    const userRole = useSelector((state) => state.login.role)
    const userImage = useSelector((state) => state.login.userImage);
    console.log()
    return (

        <div className="d-flex justify-content-between bg-white px-4 py-1 w-100" style={{ boxShadow: 'rgba(100, 100, 111, 0.2) 0px 7px 29px 0px', }} >
            <div><Link to='/learnerdashboard'>
                <img src="https://digital-lync.konalms.com/assets/logo.ab024049.png" alt="Logo" /></Link>
            </div>
            <div className="d-flex  justify-content-center align-items-center">
                {
                    userRole === 'admin' && <div className='me-4'><Link to='/admindashboard'><p className="m-0 p-0 text-primary">Admin Portal <i class="fa-solid fa-location-arrow"></i></p></Link></div>
                }
                <div className='position-relative'>
                    {userImage?.length === 2 ?
                        <h3 className="rounded-circle bg-secondary text-white fs-5 m-0" style={{ padding: '14px 16px', fontWeight: '400' }}>{userImage}</h3>
                        : <img src={userImage} alt="profile pic" width={'50px'} height={'50px'} style={{ borderRadius: '50%' }} />}
                    <div className="profile-popup bg-white ">
                        <Link className='text-decoration-none text-dark' to='/myprofile'><p><i class="fa-solid fa-user me-2" />My Profile</p></Link>
                        <Link className='text-decoration-none text-dark' to='/' onClick={() => localStorage.clear()}><p><i class="fa-solid fa-right-from-bracket me-2" />Sign Out</p></Link>
                    </div>
                </div>
            </div>
        </div>
    );
}
export default Header;