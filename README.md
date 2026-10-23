# Smart Pantry Manager

## Project Overview
Smart Pantry Manager is an Android application developed in Java that helps users manage pantry ingredients and discover recipes based on the ingredients they have available.

## Features
- Add pantry ingredients with details such as quantity and expiry date.
- View pantry items in a list.
- Edit and delete pantry items.
- Store pantry and recipe data using SQLite.
- Browse suggested recipes.
- View recipe details.
- Apply strict recipe matching so recipes are suggested only when all required ingredients and quantities are available.
- View application information and recipe-matching details in Settings.
- Display an empty state when no pantry items or matching recipes are available.

## Technologies
- Java
- Android Studio
- SQLite
- Android RecyclerView
- Android Activities and Intents

## Main Screens
1. Pantry List
2. Add/Edit Ingredient
3. Suggested Recipes
4. Recipe Detail
5. Settings

## Project Structure
- `DatabaseHelper.java` — SQLite database operations, pantry CRUD and recipe matching.
- `PantryItem.java` — pantry item model.
- `MainActivity.java` — main pantry screen.
- `AddEditActivity.java` — add and edit pantry ingredients.
- `RecipesActivity.java` — suggested recipes.
- `RecipeDetailActivity.java` — recipe information.
- `SettingsActivity.java` — application information.
- `PantryAdapter.java` — displays pantry items and their actions.

## Running the Application
1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Gradle to sync.
4. Run the app on an Android emulator or compatible device.

## Database
The application uses SQLite to persist pantry ingredients and recipe information locally on the device.

## Developer
Student project for Mobile App Development 700.
