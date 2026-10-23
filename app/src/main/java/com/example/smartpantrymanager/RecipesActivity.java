
package com.example.smartpantrymanager; // Uses the app's package.

import android.app.Activity; // Provides the base screen class.
import android.os.Bundle; // Provides screen state information.
import android.database.Cursor; // Reads recipe rows from SQLite.
import android.graphics.Typeface; // Styles the screen heading.
import android.view.ViewGroup; // Provides layout size constants.
import android.widget.Button; // Creates clickable buttons.
import android.widget.LinearLayout; // Arranges screen elements.
import android.widget.ScrollView; // Allows the content to scroll.
import android.widget.TextView; // Displays text.
import java.util.List; // Holds suggested recipe IDs.

public class RecipesActivity extends Activity { // Defines the recipe suggestions screen.

    private DatabaseHelper database; // Provides access to the local database.
    private LinearLayout recipeContainer; // Holds the recipe cards and messages.

    @Override // Replaces the parent class method.
    protected void onCreate(Bundle savedInstanceState) { // Runs when the screen opens.
        super.onCreate(savedInstanceState); // Initialises the Activity.
        database = new DatabaseHelper(this); // Connects to the app database.
        buildScreen(); // Displays the screen before loading recipes.
        loadRecipes(); // Loads the matching recipes.
    } // Ends onCreate.

    private void buildScreen() { // Creates the screen layout.
        ScrollView scrollView = new ScrollView(this); // Makes the page scrollable.
        LinearLayout page = new LinearLayout(this); // Creates the main layout.
        page.setOrientation(LinearLayout.VERTICAL); // Stacks elements vertically.
        page.setPadding(32, 32, 32, 32); // Adds space around the content.
        scrollView.addView(page); // Places the layout inside the scroll view.

        TextView heading = new TextView(this); // Creates the screen heading.
        heading.setText("Suggested Recipes"); // Sets the heading.
        heading.setTextSize(26); // Makes the heading larger.
        heading.setTypeface(null, Typeface.BOLD); // Makes the heading bold.
        page.addView(heading); // Displays the heading.

        TextView explanation = new TextView(this); // Creates an explanation.
        explanation.setText("Recipes appear when you have all the required ingredients in the required quantities."); // Explains strict matching.
        explanation.setTextSize(16); // Sets readable text size.
        explanation.setPadding(0, 12, 0, 20); // Adds space around the explanation.
        page.addView(explanation); // Displays the explanation.

        recipeContainer = new LinearLayout(this); // Creates the recipe list.
        recipeContainer.setOrientation(LinearLayout.VERTICAL); // Stacks recipe cards.
        page.addView(recipeContainer); // Adds the list to the page.

        Button backButton = new Button(this); // Creates the back button.
        backButton.setText("Back to Pantry"); // Sets the button label.
        backButton.setOnClickListener(view -> finish()); // Returns to the previous screen.
        page.addView(backButton); // Displays the back button.

        setContentView(scrollView); // Makes the screen visible.
    } // Ends buildScreen.

    private void loadRecipes() { // Finds recipes that match the pantry.
        recipeContainer.removeAllViews(); // Clears previous results.

        try { // Handles errors while reading the database.
            List<Integer> recipeIds = database.getSuggestedRecipeIds(); // Gets strict matches.

            if (recipeIds == null || recipeIds.isEmpty()) { // Checks whether any recipes match.
                TextView emptyMessage = new TextView(this); // Creates an empty-state message.
                emptyMessage.setText("No recipes match your pantry yet. Add more ingredients or check their quantities and units."); // Explains why the list may be empty.
                emptyMessage.setTextSize(16); // Makes the message readable.
                recipeContainer.addView(emptyMessage); // Displays the message.
                return; // Stops because there are no matching recipes.
            } // Ends the empty-list check.

            for (int recipeId : recipeIds) { // Processes every matching recipe.
                Cursor cursor = database.getRecipe(recipeId); // Reads one recipe.

                if (cursor != null) { // Checks that the query returned a cursor.
                    try { // Ensures the cursor is closed.
                        if (cursor.moveToFirst()) { // Checks that the recipe exists.
                            String name = cursor.getString(1); // Reads the recipe name.
                            String ingredients = cursor.getString(2); // Reads the ingredients.

                            LinearLayout card = new LinearLayout(this); // Creates a recipe card.
                            card.setOrientation(LinearLayout.VERTICAL); // Stacks card contents.
                            card.setPadding(16, 16, 16, 16); // Adds space inside the card.

                            TextView recipeName = new TextView(this); // Creates the recipe title.
                            recipeName.setText(name); // Displays the recipe name.
                            recipeName.setTextSize(20); // Sets the title size.
                            recipeName.setTypeface(null, Typeface.BOLD); // Makes the title bold.
                            card.addView(recipeName); // Adds the title to the card.

                            TextView ingredientText = new TextView(this); // Creates the ingredient summary.
                            ingredientText.setText("Ingredients: " + ingredients); // Displays the required ingredients.
                            ingredientText.setTextSize(15); // Sets the ingredient text size.
                            ingredientText.setPadding(0, 8, 0, 8); // Adds space around the summary.
                            card.addView(ingredientText); // Adds the summary to the card.

                            Button viewButton = new Button(this); // Creates the details button.
                            viewButton.setText("View Recipe"); // Sets the button label.
                            viewButton.setOnClickListener(view -> { // Handles button taps.
                                android.content.Intent intent = new android.content.Intent(RecipesActivity.this, RecipeDetailActivity.class); // Opens the detail screen.
                                intent.putExtra("recipe_id", recipeId); // Passes the selected recipe ID.
                                startActivity(intent); // Opens the recipe details.
                            }); // Ends the click listener.
                            card.addView(viewButton); // Adds the button to the card.

                            recipeContainer.addView(card); // Displays the completed card.

                            TextView separator = new TextView(this); // Creates space between cards.
                            separator.setText(" "); // Adds a small visual gap.
                            recipeContainer.addView(separator); // Displays the gap.
                        } // Ends the recipe check.
                    } finally { // Runs even if reading the recipe fails.
                        cursor.close(); // Releases the database cursor.
                    } // Ends cursor cleanup.
                } // Ends the cursor check.
            } // Ends the recipe loop.

        } catch (Exception exception) { // Handles database or loading errors.
            TextView errorMessage = new TextView(this); // Creates an error message.
            errorMessage.setText("Could not load recipes: " + exception.getMessage()); // Displays the error details.
            errorMessage.setTextSize(16); // Makes the message readable.
            recipeContainer.addView(errorMessage); // Shows the error on the screen.
        } // Ends error handling.
    } // Ends loadRecipes.

    @Override // Replaces the parent class method.
    protected void onResume() { // Runs whenever this screen becomes active.
        super.onResume(); // Performs the normal resume operation.
        if (database != null && recipeContainer != null) { // Checks that setup is complete.
            loadRecipes(); // Refreshes matches after returning to this screen.
        } // Ends the setup check.
    } // Ends onResume.
} // Ends RecipesActivity.
