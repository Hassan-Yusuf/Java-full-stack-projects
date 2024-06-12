// src/App.js
import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import './App.css'; 
import Recipe from './components/Recipe';
import Login from './components/Login';
import Register from './components/Register';
import { SavedRecipes } from './components/SavedRecipes';

const App = () => { 
  const APP_ID = process.env.REACT_APP_APP_ID; 
  const APP_KEY = process.env.REACT_APP_API_KEY;
  const [recipes, setRecipes] = useState([]); 
  const [search, setSearch] = useState(""); 
  const [query, setQuery] = useState("chicken"); 
  
  useEffect(() => { 
    getRecipes(); 
  }, [query]);

  // useEffect(() => {
  //   const user = localStorage.getItem('username');
  //   if (!user) {
  //     window.location.href = '/login';
  //   }
  // }, []);
  
  
  const getRecipes = async () => { 
    const response = await fetch(`https://api.edamam.com/search?q=${query}&app_id=${APP_ID}&app_key=${APP_KEY}`); 
    const data = await response.json(); 
    setRecipes(data.hits); 
    console.log(data); 
  }; 
  
  const updateSearch = e => { 
    setSearch(e.target.value); 
  }; 
  
  const getSearch = e => { 
    e.preventDefault(); 
    setQuery(search); 
    setSearch(""); 
  }; 

  const logout = () => {
    localStorage.removeItem('username');
    // window.location.href = '/login';
  };

  return ( 
    <Router>
      <div className="App">
        <nav>
        <img src="/logo.png" alt="Login"/>
        <div class="nav-links">
          <Link to="/">Home</Link>
          <Link to="/saved">Saved Recipes</Link>
          <Link to="/login">Login</Link>
        <button onClick={logout}>Logout</button>
        </div>
        </nav>
        <Routes>
          <Route path="/" element={
            <div className="content">
              <form className="search-form" onSubmit={getSearch}> 
                <input className="search-bar" type="text" value={search} onChange={updateSearch} /> 
                <button className="search-button" type="submit">Search</button> 
              </form> 
              <div className="recipes"> 
                {recipes.map(({ recipe }) => ( 
                  <Recipe key={recipe.label} recipe={recipe} /> 
                ))} 
              </div> 
            </div>
          } />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/saved" element={<SavedRecipes />} />
        </Routes>
      </div>
    </Router>
  ); 
}; 

export default App;
