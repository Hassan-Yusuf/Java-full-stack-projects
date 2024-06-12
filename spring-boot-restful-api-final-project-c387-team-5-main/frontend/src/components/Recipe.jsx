import React, { useState, useEffect } from "react";
import style from './recipe.module.css';

const Recipe = ({ recipe }) => {
    const [isSaved, setIsSaved] = useState(false);
    const [showIngredients, setShowIngredients] = useState(false);

    useEffect(() => {
        const user = localStorage.getItem('username');
        if (!user) {
            window.location.href = '/login';
        } else {
            const username = localStorage.getItem('username');
            fetchSavedRecipes(username);
        }
    }, []);

    const fetchSavedRecipes = async (username) => {
        try {
            const response = await fetch(`http://localhost:8080/api/recipes/${username}`);
            if (response.ok) {
                const savedRecipes = await response.json();
                const uri = recipe.uri;
                const recipeId = uri.split('_')[1];
                setIsSaved(savedRecipes.some(savedRecipe => savedRecipe.recipeId === recipeId));
            } else {
                throw new Error('Failed to fetch saved recipes.');
            }
        } catch (error) {
            console.error('Error fetching saved recipes:', error.message);
        }
    };

    const toggleSave = async () => {
        const uri = recipe.uri;
        const recipeId = uri.split('_')[1];
        const username = localStorage.getItem('username');
        
        try {
            const response = await fetch(`http://localhost:8080/api/recipes/${username}`, {
                method: isSaved ? 'DELETE' : 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ recipeId })
            });

            if (response.ok) {
                setIsSaved(!isSaved);
            } else {
                throw new Error(`Failed to ${isSaved ? 'delete' : 'save'} recipe.`);
            }
        } catch (error) {
            console.error(`Error ${isSaved ? 'deleting' : 'saving'} recipe:`, error.message);
        }
    };

    const displayTime = recipe.totalTime && recipe.totalTime > 0 ? `${recipe.totalTime} minutes` : "Not available";
    const formattedCalories = recipe.calories.toFixed(2);

    return (
        <div className={style.recipe}>
            <img className={style.image} src={recipe.image} alt={recipe.label} />
            <button className={style.saveButton} onClick={toggleSave}>
                <i className={isSaved ? "fas fa-heart" : "far fa-heart"}></i>
            </button>
            <h2 className={style.title}>{recipe.label}</h2>
            <p className={style.displayText}>Time: {displayTime}</p>
            <p className={style.displayText}>Servings: {recipe.yield || "Servings not available"}</p>
            <p className={style.displayText}>Calories: {formattedCalories}</p>
            <button onClick={() => setShowIngredients(!showIngredients)} className={style.toggleButton}>
                {showIngredients ? "Hide Ingredients" : "Show Ingredients"}
            </button>
            {showIngredients && (
                <ul className={style.ingredients}>
                    {recipe.ingredients.map((ingredient, index) => (
                        <li key={index}>{ingredient.text}</li>
                    ))}
                </ul>
            )}
            <p><a href={recipe.url} target="_blank" rel="noopener noreferrer">Instruction Link</a></p>
        </div>
    );
};

export default Recipe;
