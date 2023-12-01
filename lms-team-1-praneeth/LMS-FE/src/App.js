import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Home from './Components/Home/Home';
import Login from './Components/Login/Login';
import Forgot from './Components/Forgot';
import Dashboard from './Components/LearnerDashboard/LearnerDashboard';


function App() {
  return (
   
      <div className="App">
        <Router>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/forgotpassword" element={<Forgot />} />
            <Route path="/learnerdashboard" element={<Dashboard/>}/>
          </Routes>
        </Router>
      </div>
  );
}

export default App;
