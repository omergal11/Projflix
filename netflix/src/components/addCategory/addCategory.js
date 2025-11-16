import React, { useState } from "react";
import TextInput from "../textInput/TextInput";
import { categoryServices } from "../../services/services";
import "./addCategory.css";

const AddCategory = () => {
  const [categoryName, setCategoryName] = useState(""); 
  const [promoted, setPromoted] = useState(""); 
  const [error, setError] = useState(""); 
  const [success, setSuccess] = useState(""); 

  // Function to handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const requestBody = { name: categoryName };
      if (promoted) {
        requestBody.promoted = promoted; 
      }
      // Call addCategory service
      const response = await categoryServices.addCategory(requestBody);
    if (response.status !== 201) {
        const responseError = await response.json();
        if (responseError.error) {
        throw new Error(responseError.error);
        }
    }
     // Display success message
      window.alert(` added successfully!`);
      setError("");
    } catch (err) {
      window.alert(err.message);
      setSuccess("");
    }
  };
   // Return the form
  return (
    <div className="container">
      <h2 className="heading">Add Category</h2>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}
      <form onSubmit={handleSubmit} className="form">
        <TextInput id="name" value={categoryName} onChange={(e) => setCategoryName(e.target.value)} label="Category Name" required placeholder="Enter category name ..." />
        <label htmlFor="promoted" className="label">
          Promoted Category (Optional):
        </label>
        <select
          id="promoted"
          value={promoted}
          onChange={(e) => setPromoted(e.target.value === "true")}
          className="input-select"
        >
          <option value="false">default (false)</option>
          <option value="true">True</option>
        </select>

        <button type="submit" className="button">
          Add Category
        </button>
      </form>
    </div>
  );
};

export default AddCategory;
