import React, { useState, useEffect } from "react";
import "./searchBox.css";

const SearchBox = ({ data, onSearch, onSelect }) => {
    // initial state
  const [query, setQuery] = useState("");
  const [filteredData, setFilteredData] = useState([]);
  const [isDropdownVisible, setDropdownVisible] = useState(false);
  // handle search change
  const handleSearchChange = (event) => {
    const searchTerm = event.target.value;
    setQuery(searchTerm);
     // if search term is empty, set filtered data to empty array
    if (searchTerm.trim() === "") {
      setFilteredData([]);
      setDropdownVisible(false);
      return;
    }
    
    onSearch(searchTerm); 
    // filter data
    const filtered = data.filter((item) =>
      item.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredData(filtered);
    setDropdownVisible(true);
  };
    // handle item click
  const handleItemClick = (item) => {
    setQuery(item);
    setDropdownVisible(false);
    onSelect(item); 
  };
    // useEffect to set filtered data
  useEffect(() => {
    setFilteredData(data);
  }, [data]);
  // return search box
  return (
    <div className="search-box">
      <form className="search-box-form" role="search">
        <input
          className="form-control search-box-input"
          type="search"
          placeholder="Search movies..."
          value={query}
          onChange={handleSearchChange}
          onFocus={() => setDropdownVisible(true)}
          onBlur={() => setTimeout(() => setDropdownVisible(false), 200)}
        />
      </form>

      {isDropdownVisible && filteredData.length > 0 && (
        <ul className="dropdown-search-menu">
          {filteredData.map((item, index) => (
            <li
              key={index}
              className="dropdown-search-item"
              onMouseDown={() => handleItemClick(item)}
            >
              {item}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default SearchBox;
