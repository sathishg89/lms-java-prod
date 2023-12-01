import React from 'react';
 import './Header.css';
function Header(){
    return(
        
        <div className="d-flex justify-content-between bg-white px-4 py-1 w-100" style={{boxShadow: 'rgba(100, 100, 111, 0.2) 0px 7px 29px 0px',}}>
            <div>
                <img src="https://digital-lync.konalms.com/assets/logo.ab024049.png" alt="Logo" />
            </div>
            <div className="d-flex  justify-content-center align-items-center ">
                <h3 className="rounded-circle bg-secondary text-white fs-5" style={{padding: '15px 17px', fontWeight:'400', }}>VA</h3>
                <div className="profile-popup position-absolute d-flex flex-column rounded bg-light p-1 border invisible
                ">
           <a className='text-decoration-none' href='./profile'><i class="fa-solid fa-user"/>My Profile</a>
           <a className='text-decoration-none' href='./'><i class="fa-solid fa-right-from-bracket"/>Sign Out</a>
          </div>
          
            </div>
        </div>
    );
}
export default Header;