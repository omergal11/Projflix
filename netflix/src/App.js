import React from "react";
import { BrowserRouter as router, Routes, Route } from "react-router-dom";
import { useLocation } from "react-router-dom";
import Login from "./components/login/Login";
import SignUp from "./components/signup/Signup";
import HomePage from "./pages/HomePage";
import VideoPlayer  from "./components/videoPlayer/VideoPlayer";  
import MoviesPage from "./pages/MoviesPage";  
import SearchPage from "./pages/searchPage";
import AdminPage from "./pages/adminPage";
import Layout from "./middlewears/Layout";
import WelcomePage from "./pages/WelcomePage";
import Footer from "./components/Footer/Footer";
import Profile from "./pages/Profile";
import PrivateRoute from "./middlewears/PrivateRoute";
import "./App.css";
// App component
function App() {
  const location = useLocation(); 
  // WithFooter function
  const WithFooter = ({ children }) => {
    return (
      <div className="page-container">
        <div className="content-wrapper">
          {children}
        </div>
        <Footer />
      </div>
    );
  };

  return (
    <Routes>
      {/* Routes without NavBar */}
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<WelcomePage />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="/videoPlayer" element={<PrivateRoute><VideoPlayer /></PrivateRoute>} />

      {/* Routes with NavBar */}
      <Route element={<Layout />}>
        <Route path="/homePage" element={<PrivateRoute><WithFooter><HomePage /></WithFooter></PrivateRoute>} />
        <Route path="/moviesPage" element={<PrivateRoute><WithFooter><MoviesPage /></WithFooter></PrivateRoute>} />
        <Route path="/search/:query" element={<PrivateRoute><WithFooter><SearchPage /></WithFooter></PrivateRoute>} />
        <Route path="/admin" element={<PrivateRoute adminOnly={true}><WithFooter><AdminPage /></WithFooter></PrivateRoute>} />
        <Route path="/profile" element={<PrivateRoute><WithFooter><Profile /></WithFooter></PrivateRoute>} />
      </Route>
    </Routes>
  );
}

export default App;
