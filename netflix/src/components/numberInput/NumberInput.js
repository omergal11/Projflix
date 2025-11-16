// NumberInput.js
import React from 'react';
import './NumberInput.css';

const NumberInput = ({ id, value, onChange, label, required, placeholder }) => {
  return (
    <div>
      <label htmlFor={id} className="label">
        {label}:
      </label>
      <input
        type="number"
        id={id}
        value={value}
        onChange={onChange}
        required={required}
        placeholder= {placeholder}
        className="number-input"
      />
    </div>
  );
};

export default NumberInput;