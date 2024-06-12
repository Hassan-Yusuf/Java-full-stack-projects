import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Recipe from './Recipe';
import style from './recipe.module.css';

const APP_ID = process.env.REACT_APP_APP_ID; 
const APP_KEY = process.env.REACT_APP_API_KEY;

const fetchSavedRecipeIds = async (username) => {
    const response = await fetch(`http://localhost:8080/api/recipes/${username}`);
    const data = await response.json();
    return data;
};

const fetchRecipeById = async (recipeId) => {
    const response = await fetch(`https://api.edamam.com/api/recipes/v2/${recipeId}?type=public&app_id=${APP_ID}&app_key=${APP_KEY}`);
    const data = await response.json();
    return data;
};

export const SavedRecipes = () => {
    const [savedRecipes, setSavedRecipes] = useState([]);
    const [fetchedRecipes, setFetchedRecipes] = useState([]);

    useEffect(() => {
        const username = localStorage.getItem('username');
        if (username) {
            fetchSavedRecipeIds(username)
                .then(data => setSavedRecipes(data))
                .catch(error => console.error('Error fetching saved recipes:', error));
        }
    }, []);

    const clearSavedRecipes = async () => {
        const username = localStorage.getItem('username');
        try {
            for (const savedRecipe of savedRecipes) {
                const response = await fetch(`http://localhost:8080/api/recipes/${username}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ recipeId: savedRecipe.recipeId })
                });
                if (!response.ok) {
                    throw new Error('Failed to delete recipe.');
                }
            }
            setSavedRecipes([]); // Clear saved recipes after successful deletion
        } catch (error) {
            console.error('Error clearing saved recipes:', error.message);
        }
    };

    useEffect(() => {
        const fetchSavedRecipes = async () => {
            const fetchedRecipesData = await Promise.all(savedRecipes.map(async (savedRecipe) => {
                const data = await fetchRecipeById(savedRecipe.recipeId);
                console.log('Fetched recipe:', data);
                return data;
            }));
            setFetchedRecipes(fetchedRecipesData);
        };

        if (savedRecipes.length > 0) {
            fetchSavedRecipes();
        }
    }, [savedRecipes]);

    return (
        <div className={style.container}>
            <h2>Saved Recipes</h2>
            {savedRecipes.length > 0 ? (
                <div>
                    <button className={style.clearButton} onClick={clearSavedRecipes}>Clear Saved Recipes</button>
                    <div className="recipes">
                        {fetchedRecipes.map(({ recipe }) => (
                            <Recipe key={recipe.label} recipe={recipe} />
                        ))}
                    </div>
                </div>
            ) : (
                <div className={style.noSavedRecipes}>
                    <p>No saved recipes yet. <Link to="/">Go home</Link> to start exploring!</p>
                </div>
            )}
        </div>
    );
};

