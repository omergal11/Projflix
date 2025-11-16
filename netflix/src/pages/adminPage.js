import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Popup from "../components/popup/Popup"; // ייבוא רכיב ה-Popup
import AddMovie from "../components/addMovie/AddMovie"; // ייבוא הרכיב AddMovie
import AddCategory from "../components/addCategory/addCategory"; 
import PatchCategory from "../components/patchCategory/patchCategory";   
import DeleteCategory from "../components/deleteCategory/deleteCategory";
import PutMovie from "../components/putMovie/putMovie";
import DeleteMovie from "../components/deleteMovie/deleteMovie";
import "./adminPage.css";

// page for admin to manage movies and categories
const AdminPage = () => {
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [popupContent, setPopupContent] = useState(null); 
  const [selectedOption, setSelectedOption] = useState(""); 
  const navigate = useNavigate();
  // handleSelection function
  const handleSelection = (event) => {
    const selectedValue = event.target.value;
    setSelectedOption(selectedValue);

    // switch statement to determine which popup to display
    switch (selectedValue) {
      case "add-category":
        setPopupContent(<AddCategory />);
        setIsPopupOpen(true);
        break;
      case "add-movie":
        setPopupContent(<AddMovie />);
        setIsPopupOpen(true);
        break;
      case "edit-category":
        setPopupContent(<PatchCategory />);
        setIsPopupOpen(true);  
        break;
      case "delete-category":  
        setPopupContent(<DeleteCategory />);
        setIsPopupOpen(true);  
        break; 
       case "edit-movie":
        setPopupContent(<PutMovie />);
        setIsPopupOpen(true);  
        break; 
      case "delete-movie":
        setPopupContent(<DeleteMovie />);
        setIsPopupOpen(true);  
        break;  
      default:
        navigate(`/admin`);
    }
  };

  const handleClosePopup = () => {
    setIsPopupOpen(false);
    setSelectedOption(""); 
  };
  // return AdminPage component
  return (
    <div className="admin-area">
      <h1>Hello Admin!</h1>
      <h2>What would you like to do?</h2>
      <div className="dropdown-wrapper">
        <select onChange={handleSelection} value={selectedOption}>
          <option value="" disabled>
            Select an option
          </option>
          <option value="add-movie">Add Movie</option>
          <option value="edit-movie">Edit Movie</option>
          <option value="delete-movie">Delete Movie</option>
          <option value="add-category">Add Category</option>
          <option value="edit-category">Edit Category</option>
          <option value="delete-category">Delete Category</option>
        </select>
      </div>

      <Popup isOpen={isPopupOpen} onClose={handleClosePopup}>
        {popupContent}
      </Popup>
    </div>
  );
};

export default AdminPage;
