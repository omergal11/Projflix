// Popup.js
import React from "react";
import ReactDOM from "react-dom";
import "./Popup.css";
//  Popup component to display a popup
const Popup = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null; 
   // The portal is created here
  return ReactDOM.createPortal(
    <div className="popup-overlay" onClick={onClose}>
      <div className="popup-content" onClick={(e) => e.stopPropagation()}>
        <button className="close-button" onClick={onClose}>
          X
        </button>
        {children} 
      </div>
    </div>,
    document.getElementById("portal-root") 
  );
};

export default Popup;
