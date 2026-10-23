
package com.example.smartpantrymanager; // Places this screen in the app's package.

import android.app.Activity; // Provides the base class for an Android screen.
import android.database.Cursor; // Reads recipe details from SQLite.
import android.graphics.Color; // Provides screen colours.
import android.os.Bundle; // Provides the saved activity state.
import android.view.Gravity; // Controls element alignment.
import android.widget.Button; // Creates a clickable button.
import android.widget.LinearLayout; // Arranges screen elements vertically.
import android.widget.ScrollView; // Allows long recipe instructions to scroll.
import android.widget.TextView; // Displays recipe information.
import android.widget.Toast; // Displays short messages.

public class RecipeDetailActivity extends Activity { // Defines the recipe detail screen.

    private DatabaseHelper database; // Provides access to the recipe database.

    @Override // Overrides the standard activity creation method.
    protected void onCreate(Bundle savedInstanceState) { // Runs when the screen opens.
        super.onCreate(savedInstanceState); // Performs the normal Android setup.
        database = new DatabaseHelper(this); // Opens the database helper.
        showRecipe(); // Loads and displays the selected recipe.
    } // Ends onCreate.

    private void showRecipe() { // Reads the recipe and builds the detail screen.
        int recipeId = getIntent().getIntExtra("recipe_id", -1); // Reads the selected recipe ID.
        Cursor cursor = database.getRecipe(recipeId); // Queries SQLite for the selected recipe.

        try { // Ensures the cursor is closed after reading.
            if (!cursor.moveToFirst()) { // Checks whether the recipe exists.
                Toast.makeText(this, "Recipe not found.", Toast.LENGTH_SHORT).show(); // Informs the user that the recipe is unavailable.
                finish(); // Closes the detail screen.
                return; // Stops further processing.
            } // Ends the missing-recipe check.

            String name = cursor.getString(1); // Reads the recipe name.
            String ingredients = cursor.getString(2); // Reads the ingredient requirements.
            String method = cursor.getString(3); // Reads the preparation instructions.

            LinearLayout root = new LinearLayout(this); // Creates the main layout.
            root.setOrientation(LinearLayout.VERTICAL); // Stacks the heading, content, and button.
            root.setPadding(28, 28, 28, 24); // Adds space around the screen content.
            root.setBackgroundColor(Color.rgb(248, 247, 242)); // Sets a light background.

            TextView heading = new TextView(this); // Creates the recipe heading.
            heading.setText(name); // Displays the recipe name.
            heading.setTextSize(26); // Makes the heading prominent.
            heading.setTextColor(Color.rgb(38, 75, 56)); // Uses a dark green colour.
            heading.setGravity(Gravity.CENTER); // Centres the heading.
            root.addView(heading); // Adds the heading to the screen.

            ScrollView scroll = new ScrollView(this); // Creates a scrollable content area.
            LinearLayout content = new LinearLayout(this); // Creates the content layout.
            content.setOrientation(LinearLayout.VERTICAL); // Stacks recipe information vertically.
            content.setPadding(0, 20, 0, 20); // Adds space around the recipe information.

            TextView ingredientsHeading = new TextView(this); // Creates the ingredients heading.
            ingredientsHeading.setText("Ingredients"); // Sets the heading label.
            ingredientsHeading.setTextSize(21); // Sets the heading size.
            content.addView(ingredientsHeading); // Adds the ingredients heading.

            TextView ingredientText = new TextView(this); // Creates the ingredient list.
            ingredientText.setText(ingredients.replace(";", "\n")); // Displays each ingredient on a separate line.
            ingredientText.setTextSize(17); // Makes the ingredients readable.
            ingredientText.setPadding(0, 8, 0, 20); // Adds space below the ingredient list.
            content.addView(ingredientText); // Adds the ingredient list.

            TextView methodHeading = new TextView(this); // Creates the method heading.
            methodHeading.setText("Method"); // Sets the heading label.
            methodHeading.setTextSize(21); // Sets the heading size.
            content.addView(methodHeading); // Adds the method heading.

            TextView methodText = new TextView(this); // Creates the preparation instructions.
            methodText.setText(method); // Displays the preparation method.
            methodText.setTextSize(17); // Makes the instructions readable.
            methodText.setPadding(0, 8, 0, 20); // Adds space around the instructions.
            content.addView(methodText); // Adds the instructions to the content.

            scroll.addView(content); // Places the recipe information inside the scroll view.
            root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1)); // Gives the content the remaining screen space.

            Button backButton = new Button(this); // Creates the back button.
            backButton.setText("Back to Recipes"); // Sets the button label.
            root.addView(backButton); // Adds the button to the screen.
            backButton.setOnClickListener(v -> finish()); // Returns to the recipe list when tapped.

            setContentView(root); // Displays the completed recipe detail screen.
        } finally { // Runs after reading the recipe.
            cursor.close(); // Releases the database cursor.
        } // Ends cursor cleanup.
    } // Ends showRecipe.

} // Ends RecipeDetailActivity.
