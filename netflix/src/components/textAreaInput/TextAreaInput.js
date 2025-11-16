// TextAreaInput.js
import React from 'react';
import './TextAreaInput.css';
// TextAreaInput component to input text
const TextAreaInput = ({ id, value, onChange, label, required, placeholder }) => {
  return (
    <div>
      <label htmlFor={id} className="label">
        {label}:
      </label>
      <textarea
        id={id}
        value={value}
        onChange={onChange}
        required={required}
        placeholder= {placeholder}
        className="textarea-input"
      ></textarea>
    </div>
  );
};

export default TextAreaInput;