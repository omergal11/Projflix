// PatchCategory.js
import React, { useState, useEffect } from 'react';
import { categoryServices } from '../../services/services';
import './patchCategory.css';
import TextInput from '../textInput/TextInput';

const PatchCategory = () => {
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [name, setName] = useState('');
  const [promoted, setPromoted] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const token = localStorage.getItem('jwtToken');
 // Fetch all categories
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const data = await categoryServices.getAllCategories();
        if(data.error){
          throw new Error(data.error);
        }
        if (Array.isArray(data)) {
          setCategories(data);
        }
      } catch (error) {
        setError('Failed to fetch categories');
        console.error('Error:', error);
      }
    };

    fetchCategories();
  }, []);
  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!selectedCategory) {
      setError('Please select a category');
      return;
    }

    if (!name && promoted === undefined) {
      setError('At least one field must be updated');
      return;
    }

    const updateData = {};
    if (name) {
      updateData.name = name;
    }
    if (promoted !== undefined) {
      updateData.promoted = promoted;
    }
    // Call update category service
    try {
      const response = await categoryServices.updateCategory(selectedCategory, updateData);
      if (response.error) {
        throw new Error(response.error);
      }
      window.alert('Category updated successfully');
      setError('');
      
      // Refresh categories list
      const updatedCategories = await categoryServices.getAllCategories();
      if (updatedCategories.error) {
        throw new Error(updatedCategories.error);
      }
      setCategories(updatedCategories);
      
      // Reset form
      setName('');
      setPromoted(false);
      setSelectedCategory('');
    } catch (error) {
      window.alert(error.message || 'Failed to update category');
      setSuccess('');
    }
  };
  // Display form
  return (
    <div className="container">
      <h2 className="heading">Update Category</h2>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}
      <form onSubmit={handleSubmit}>
        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
          className="input-select"
        >
          <option value="">-- Select a category --</option>
          {categories.map((category) => (
            <option key={category._id} value={category._id}>
              {category.name}
            </option>
          ))}
        </select>
        <TextInput
          id="name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          label="New Name"
          placeholder="Enter new category name..."
        />
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
          Update Category
        </button>
      </form>
    </div>
  );
};

export default PatchCategory;