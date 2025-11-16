import React from "react";
import "./Button.css";

const Button = ({ children, onClick, className = "", type = "button" }) => {
  return (
    <button className={`button ${className}`} onClick={onClick} type={type}>
      {children}
    </button>
  );
};

export default Button;
