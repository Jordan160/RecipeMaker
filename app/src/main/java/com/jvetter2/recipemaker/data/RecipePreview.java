package com.jvetter2.recipemaker.data;

public class RecipePreview {
    private String recipeName;
    private String recipeCategory;
    private String recipeIngredients;
    private String recipeInstructions;

    public String getName() {
        return recipeName;
    }
    public String getRecipeCategory() {
        return recipeCategory;
    }
    public String getRecipeIngredients() {
        return recipeIngredients;
    }
    public String getRecipeInstructions() {
        return recipeInstructions;
    }

    public void setName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public void setRecipeInstructions(String recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }
}
