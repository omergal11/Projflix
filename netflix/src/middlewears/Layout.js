import React from "react";
import { Outlet, useLocation } from "react-router-dom";
import NavBar from "../components/NavBar/NavBar";
// Layout component to display the navbar
const Layout = () => {
  const location = useLocation();
  
  const showNavbar = location.pathname !== "/login" && location.pathname !== "/" && location.pathname !== "/videoPlayer"
   && location.pathname !== "/signup";

  return (
    <div>
      {showNavbar && <NavBar />}
      <Outlet />
    </div>
  );
};

export default Layout;
