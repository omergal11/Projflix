import React from "react";
import Movie from "../movie/Movie";
import "./searchList.css";

const SearchList = ({movies}) => {
    // return search list
    return (
            <div className="movie-list-fixed">
              {movies.map((movie,index) => (
                <Movie key={index} movie={movie} /> 
              ))}
            </div>
          );
        };
        
    export default SearchList;