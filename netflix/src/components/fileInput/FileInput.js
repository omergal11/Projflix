// FileInput.js
import React from 'react';
import './FileInput.css';
// FileInput component to upload files
const FileInput = ({ label, accept, onChange, required, multiple }) => {
  return (
    <div className="file-input-container">
      <label className="file-label">{label}:</label>
      <input
        type="file"
        accept={accept}
        onChange={onChange}
        required={required}
        multiple={multiple}
        className="file-input-button"
      />
    </div>
  );
};

export default FileInput; 