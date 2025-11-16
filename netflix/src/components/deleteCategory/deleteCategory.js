// DeleteCategory.js
import React, { useState, useEffect } from 'react';
import { categoryServices } from '../../services/services';
import './deleteCategory.css';

const DeleteCategory = () => {
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  // Fetch all categories
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const data = await categoryServices.getAllCategories();
        if (data.error) {
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
  // Delete category
  const handleDelete = async () => {
    try {
      await categoryServices.deleteCategory(selectedCategory);
      window.alert('Category deleted successfully');
      setError('');
      // Refresh categories list
      const updatedCategories = await categoryServices.getAllCategories();
      if (updatedCategories.error) {
        throw new Error(updatedCategories.error);
      } 
      setCategories(updatedCategories);
      setSelectedCategory('');
    } catch (error) {
      window.alert('Failed to delete category');
      setSuccess('');
    }
  };
   // Display delete category form
  return (
    <div className="container">
      <h2 className="heading">Delete Category</h2>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}
      {categories.length > 0 && (
        <>
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
          <button onClick={handleDelete} className="button">
            Delete Category
          </button>
        </>
      )}
    </div>
  );
};

export default DeleteCategory;