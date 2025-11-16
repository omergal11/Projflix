// TextInput.js
import React from 'react';
import './TextInput.css';

const TextInput = ({ id, value, onChange, label, required, placeholder, type = "text" }) => {
    // TextInput component to input text
    return (
    <div>
      <label htmlFor={id} className="label">
        {label}:
      </label>
      <input
        type={type}
        id={id}
        value={value}
        onChange={onChange}
        required={required}
        placeholder= {placeholder}
        className="text-input"
      />
    </div>
  );
};

export default TextInput;