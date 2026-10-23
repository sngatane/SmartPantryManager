
package com.example.smartpantrymanager; // Places this class in the app's Java package.

import android.content.ContentValues; // Stores column values when inserting or updating records.
import android.content.Context; // Provides access to the Android application environment.
import android.database.Cursor; // Reads rows returned by database queries.
import android.database.sqlite.SQLiteDatabase; // Provides SQLite database operations.
import android.database.sqlite.SQLiteOpenHelper; // Creates and manages the SQLite database.
import java.util.ArrayList; // Stores lists of matching recipe IDs.
import java.util.List; // Defines the list interface used by the matching method.
import java.util.Locale; // Makes text normalisation consistent.
import java.util.regex.Matcher; // Extracts information from ingredient descriptions.
import java.util.regex.Pattern; // Defines the format of recipe ingredient descriptions.

public class DatabaseHelper extends SQLiteOpenHelper { // Manages the pantry and recipe database.

    private static final String DB_NAME = "smart_pantry.db"; // Sets the database filename.
    private static final int DB_VERSION = 1; // Sets the current database version.

    public DatabaseHelper(Context context) { // Creates the database helper.
        super(context, DB_NAME, null, DB_VERSION); // Passes the database settings to SQLiteOpenHelper.
    } // Ends the constructor.

    @Override // Overrides the method SQLiteOpenHelper calls when the database is first created.
    public void onCreate(SQLiteDatabase db) { // Creates the database tables.
        db.execSQL("CREATE TABLE pantry (" + // Starts the pantry table definition.
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + // Gives each pantry item a unique ID.
                "name TEXT NOT NULL," + // Stores the ingredient name.
                "quantity REAL NOT NULL," + // Stores the available quantity.
                "unit TEXT NOT NULL," + // Stores the quantity unit.
                "expiry_date TEXT)"); // Stores an optional expiry date.

        db.execSQL("CREATE TABLE recipes (" + // Starts the recipes table definition.
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + // Gives each recipe a unique ID.
                "name TEXT NOT NULL UNIQUE," + // Stores a unique recipe name.
                "ingredients TEXT NOT NULL," + // Stores the required ingredients separated by semicolons.
                "method TEXT NOT NULL)"); // Stores the recipe preparation instructions.

        seedRecipes(db); // Adds the initial set of 20 recipes.
    } // Ends onCreate.

    @Override // Overrides the database upgrade method.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // Handles future database changes.
        // Add migration statements here if the database structure changes in a later version.
    } // Ends onUpgrade.

    private void seedRecipes(SQLiteDatabase db) { // Adds the recipes available in the app.
        addRecipe(db, "Tomato Egg Scramble", "2 eggs;1 tomato", "Chop the tomato, cook it in a pan, add beaten eggs, and stir until cooked."); // Adds a tomato and egg recipe.
        addRecipe(db, "Cheese Omelette", "2 eggs;30 g cheese", "Beat the eggs, cook them in a pan, add cheese, fold, and cook through."); // Adds a cheese omelette.
        addRecipe(db, "Banana Pancakes", "2 bananas;2 eggs", "Mash the bananas, mix with eggs, and cook small pancakes in a lightly greased pan."); // Adds banana pancakes.
        addRecipe(db, "Garlic Tomato Pasta", "200 g pasta;2 tomatoes;2 cloves garlic", "Cook pasta, fry chopped garlic and tomatoes, then mix with the drained pasta."); // Adds garlic tomato pasta.
        addRecipe(db, "Potato Egg Hash", "2 potatoes;2 eggs;1 onion", "Dice and cook the potatoes and onion, then add eggs and cook until set."); // Adds potato egg hash.
        addRecipe(db, "Chicken Rice Bowl", "200 g chicken;200 g rice;1 onion", "Cook the rice, cook the chicken and onion thoroughly, then serve together."); // Adds a chicken rice bowl.
        addRecipe(db, "Tuna Pasta", "200 g pasta;1 can tuna;2 tbsp mayonnaise", "Cook and drain the pasta, then mix with drained tuna and mayonnaise."); // Adds tuna pasta.
        addRecipe(db, "Carrot Soup", "3 carrots;1 onion;500 ml vegetable stock", "Chop the vegetables, simmer in stock until soft, and blend carefully."); // Adds carrot soup.
        addRecipe(db, "Spinach Omelette", "2 eggs;50 g spinach;30 g cheese", "Cook spinach until wilted, add beaten eggs and cheese, and cook until set."); // Adds a spinach omelette.
        addRecipe(db, "Apple Banana Salad", "1 apple;1 banana", "Wash and chop the apple and banana, then combine."); // Adds apple banana salad.
        addRecipe(db, "Peanut Banana Toast", "2 slices bread;1 banana;2 tbsp peanut butter", "Toast the bread, spread peanut butter, and top with sliced banana."); // Adds peanut butter and banana toast.
        addRecipe(db, "Cheesy Potato Bake", "3 potatoes;100 g cheese;100 ml milk", "Slice and cook the potatoes until tender, layer with cheese and milk, and bake until golden."); // Adds a cheesy potato bake.
        addRecipe(db, "Chicken Tomato Stew", "200 g chicken;2 tomatoes;1 onion", "Cook onion and tomatoes, add chicken, and simmer until the chicken is fully cooked."); // Adds chicken tomato stew.
        addRecipe(db, "Garlic Spinach", "100 g spinach;2 cloves garlic;1 tbsp oil", "Fry chopped garlic briefly, add spinach, and cook until wilted."); // Adds garlic spinach.
        addRecipe(db, "Carrot Apple Salad", "2 carrots;1 apple", "Wash and grate the carrots and apple, then combine."); // Adds carrot apple salad.
        addRecipe(db, "Tuna Sandwich", "1 can tuna;2 slices bread;2 tbsp mayonnaise", "Drain the tuna, mix with mayonnaise, and spread on bread."); // Adds a tuna sandwich.
        addRecipe(db, "Tomato Cheese Toast", "2 slices bread;1 tomato;50 g cheese", "Top bread with sliced tomato and cheese, then toast until the cheese melts."); // Adds tomato cheese toast.
        addRecipe(db, "Onion Fried Rice", "200 g cooked rice;1 onion;2 eggs", "Cook chopped onion, add cooked rice, then stir in eggs and cook completely."); // Adds onion fried rice.
        addRecipe(db, "Banana Milk Smoothie", "1 banana;250 ml milk", "Blend the banana and milk until smooth."); // Adds a banana milk smoothie.
        addRecipe(db, "Simple Vegetable Soup", "2 carrots;1 potato;1 onion;500 ml vegetable stock", "Chop the vegetables and simmer in stock until all vegetables are tender."); // Adds vegetable soup.
    } // Ends seedRecipes.

    private void addRecipe(SQLiteDatabase db, String name, String ingredients, String method) { // Inserts one recipe into the database.
        ContentValues values = new ContentValues(); // Creates a container for recipe column values.
        values.put("name", name); // Sets the recipe name.
        values.put("ingredients", ingredients); // Sets the required ingredients.
        values.put("method", method); // Sets the preparation instructions.
        db.insertWithOnConflict("recipes", null, values, SQLiteDatabase.CONFLICT_IGNORE); // Avoids inserting a duplicate recipe name.
    } // Ends addRecipe.

    public long addPantryItem(String name, double quantity, String unit, String expiryDate) { // Saves a new pantry item.
        ContentValues values = new ContentValues(); // Creates a container for the new item's values.
        values.put("name", normalize(name)); // Saves a standardised ingredient name.
        values.put("quantity", quantity); // Saves the available quantity.
        values.put("unit", normalizeUnit(unit)); // Saves a standardised measurement unit.
        values.put("expiry_date", expiryDate); // Saves the optional expiry date.
        return getWritableDatabase().insert("pantry", null, values); // Inserts the item and returns its database ID.
    } // Ends addPantryItem.

    public int updatePantryItem(int id, String name, double quantity, String unit, String expiryDate) { // Updates an existing pantry item.
        ContentValues values = new ContentValues(); // Creates a container for the updated values.
        values.put("name", normalize(name)); // Updates the standardised ingredient name.
        values.put("quantity", quantity); // Updates the available quantity.
        values.put("unit", normalizeUnit(unit)); // Updates the standardised measurement unit.
        values.put("expiry_date", expiryDate); // Updates the expiry date.
        return getWritableDatabase().update("pantry", values, "id=?", new String[]{String.valueOf(id)}); // Updates only the selected item.
    } // Ends updatePantryItem.

    public int deletePantryItem(int id) { // Deletes a pantry item.
        return getWritableDatabase().delete("pantry", "id=?", new String[]{String.valueOf(id)}); // Deletes the record with the matching ID.
    } // Ends deletePantryItem.

    public Cursor getPantryItems() { // Retrieves every pantry item.
        return getReadableDatabase().rawQuery("SELECT id, name, quantity, unit, expiry_date FROM pantry ORDER BY name", null); // Returns pantry items sorted by name.
    } // Ends getPantryItems.

    public Cursor getPantryItem(int id) { // Retrieves one pantry item for editing.
        return getReadableDatabase().rawQuery("SELECT id, name, quantity, unit, expiry_date FROM pantry WHERE id=?", new String[]{String.valueOf(id)}); // Returns the selected record.
    } // Ends getPantryItem.

    public Cursor getAllRecipes() { // Retrieves all saved recipes.
        return getReadableDatabase().rawQuery("SELECT id, name, ingredients, method FROM recipes ORDER BY name", null); // Returns recipes sorted by name.
    } // Ends getAllRecipes.

    public Cursor getRecipe(int id) { // Retrieves one recipe for its detail screen.
        return getReadableDatabase().rawQuery("SELECT id, name, ingredients, method FROM recipes WHERE id=?", new String[]{String.valueOf(id)}); // Returns the selected recipe.
    } // Ends getRecipe.

    public List<Integer> getSuggestedRecipeIds() { // Finds recipes that can be made with the available pantry items.
        List<Integer> matches = new ArrayList<>(); // Stores the IDs of recipes that pass every requirement.
        Cursor recipes = getAllRecipes(); // Reads the recipes from SQLite.
        Cursor pantry = getPantryItems(); // Reads the current pantry contents.
        Pattern format = Pattern.compile("^(\\d+(?:\\.\\d+)?)\\s+(?:(g|kg|ml|l|cloves?|tbsp|tsp|slices?|cans?)\\s+)?(.+)$", Pattern.CASE_INSENSITIVE); // Recognises a quantity, optional unit, and ingredient name.

        try { // Ensures both cursors are closed after use.
            while (recipes.moveToNext()) { // Checks each recipe in the database.
                int recipeId = recipes.getInt(0); // Reads the recipe ID.
                String[] requirements = recipes.getString(2).split(";"); // Separates the recipe's ingredient requirements.
                boolean canMake = true; // Assumes the recipe is possible until a requirement fails.

                for (String requirement : requirements) { // Checks each required ingredient.
                    Matcher matcher = format.matcher(requirement.trim()); // Parses the ingredient description.
                    if (!matcher.matches()) { // Checks whether the ingredient description is valid.
                        canMake = false; // Rejects a recipe with an unreadable requirement.
                        break; // Stops checking this recipe.
                    } // Ends the format check.

                    double requiredQuantity = Double.parseDouble(matcher.group(1)); // Reads the required quantity.
                    String requiredUnit = matcher.group(2) == null ? "item" : normalizeUnit(matcher.group(2)); // Uses item as the default unit for unmeasured ingredients.
                    String requiredName = normalize(matcher.group(3)); // Standardises the required ingredient name.
                    boolean found = false; // Tracks whether enough of this ingredient is available.

                    if (pantry.moveToFirst()) { // Starts at the first pantry item.
                        do { // Checks each pantry item until a match is found.
                            String pantryName = normalize(pantry.getString(1)); // Reads and standardises the pantry ingredient name.
                            double availableQuantity = pantry.getDouble(2); // Reads the available quantity.
                            String pantryUnit = normalizeUnit(pantry.getString(3)); // Reads and standardises the pantry unit.

                            if (sameIngredient(pantryName, requiredName) && pantryUnit.equals(requiredUnit) && availableQuantity >= requiredQuantity) { // Checks name, unit, and required quantity.
                                found = true; // Records that the ingredient requirement is satisfied.
                                break; // Stops searching for this ingredient.
                            } // Ends the ingredient comparison.
                        } while (pantry.moveToNext()); // Continues through the pantry items.
                    } // Ends the pantry search.

                    if (!found) { // Checks whether this requirement was not satisfied.
                        canMake = false; // Rejects the recipe if any ingredient is missing or insufficient.
                        break; // Stops checking the remaining requirements.
                    } // Ends the missing-ingredient check.
                } // Ends the requirements loop.

                if (canMake) { // Checks whether every ingredient requirement was satisfied.
                    matches.add(recipeId); // Adds the complete recipe to the suggestions.
                } // Ends the complete-recipe check.
            } // Ends the recipes loop.
        } finally { // Runs even if a database operation fails.
            recipes.close(); // Releases the recipe cursor.
            pantry.close(); // Releases the pantry cursor.
        } // Ends cursor cleanup.

        return matches; // Returns only recipes with all required quantities available.
    } // Ends getSuggestedRecipeIds.

    private String normalizeUnit(String unit) { // Makes common unit spellings consistent.
        String value = unit.trim().toLowerCase(Locale.ROOT); // Removes extra spaces and converts text to lowercase.
        if (value.equals("items") || value.equals("item")) { // Checks the two common count-unit forms.
            return "item"; // Treats item and items as equivalent.
        } // Ends the count-unit check.
        return normalize(value); // Standardises simple plural units.
    } // Ends normalizeUnit.

    private String normalize(String name) { // Standardises names for simple singular/plural matching.
        String value = name.trim().toLowerCase(Locale.ROOT); // Removes surrounding spaces and converts to lowercase.
        if (value.endsWith("ies") && value.length() > 3) { // Checks for words such as berries.
            return value.substring(0, value.length() - 3) + "y"; // Converts berries to berry.
        } // Ends the ies check.
        if (value.endsWith("oes") && value.length() > 3) { // Checks for words such as potatoes and tomatoes.
            return value.substring(0, value.length() - 2); // Converts potatoes to potato and tomatoes to tomato.
        } // Ends the oes check.
        if (value.endsWith("s") && value.length() > 3 && !value.endsWith("ss")) { // Checks for most other simple plurals.
            return value.substring(0, value.length() - 1); // Removes the final s.
        } // Ends the plural check.
        return value; // Returns the standardised name.
    } // Ends normalize.

    private boolean sameIngredient(String first, String second) { // Compares two ingredient names.
        return normalize(first).equals(normalize(second)); // Matches names after simple plural normalisation.
    } // Ends sameIngredient.

} // Ends DatabaseHelper.
