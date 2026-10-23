
package com.example.smartpantrymanager; // Keeps this screen in the app's Java package.

import android.app.Activity; // Provides the base class for this screen.
import android.os.Bundle; // Provides the saved state used when the screen opens.
import android.database.Cursor; // Lets the screen read an existing ingredient from SQLite.
import android.graphics.Color; // Provides colours for the screen.
import android.view.Gravity; // Controls the alignment of text and buttons.
import android.widget.Button; // Creates buttons the user can tap.
import android.widget.EditText; // Creates fields where the user can enter information.
import android.widget.LinearLayout; // Arranges the screen elements vertically.
import android.widget.TextView; // Displays headings and field labels.
import android.widget.Toast; // Displays short messages to the user.

public class AddEditActivity extends Activity { // Defines the screen for adding or editing ingredients.

    private DatabaseHelper database; // Provides access to the SQLite database.
    private EditText nameInput; // Stores the ingredient name field.
    private EditText quantityInput; // Stores the ingredient quantity field.
    private EditText unitInput; // Stores the ingredient unit field.
    private EditText expiryInput; // Stores the optional expiry date field.
    private int itemId = -1; // Uses -1 to indicate that a new ingredient is being added.

    @Override // Replaces the normal Android activity creation method.
    protected void onCreate(Bundle savedInstanceState) { // Runs when this screen opens.
        super.onCreate(savedInstanceState); // Performs the standard Android setup.
        database = new DatabaseHelper(this); // Opens the database helper.
        itemId = getIntent().getIntExtra("item_id", -1); // Checks whether an existing ingredient was selected.
        buildScreen(); // Creates the screen layout.
        if (itemId != -1) { // Checks whether the user is editing an existing ingredient.
            loadExistingItem(); // Fills the fields with the saved information.
        } // Ends the edit-mode check.
    } // Ends onCreate.

    private void buildScreen() { // Creates the input fields and buttons.
        LinearLayout layout = new LinearLayout(this); // Creates the main screen layout.
        layout.setOrientation(LinearLayout.VERTICAL); // Stacks the fields vertically.
        layout.setPadding(32, 32, 32, 32); // Adds space around the screen content.
        layout.setBackgroundColor(Color.rgb(248, 247, 242)); // Sets a light background colour.

        TextView heading = new TextView(this); // Creates the screen heading.
        heading.setText(itemId == -1 ? "Add Ingredient" : "Edit Ingredient"); // Shows the correct heading.
        heading.setTextSize(26); // Sets the heading size.
        heading.setTextColor(Color.rgb(38, 75, 56)); // Colours the heading dark green.
        heading.setGravity(Gravity.CENTER); // Centres the heading.
        layout.addView(heading); // Adds the heading to the screen.

        nameInput = createInput("Ingredient name", layout); // Creates the ingredient name field.
        quantityInput = createInput("Quantity, for example 2", layout); // Creates the quantity field.
        quantityInput.setInputType(8194); // Allows decimal numbers in the quantity field.
        unitInput = createInput("Unit, for example kg, g, or items", layout); // Creates the unit field.
        expiryInput = createInput("Expiry date (optional, YYYY-MM-DD)", layout); // Creates the optional expiry field.

        Button saveButton = new Button(this); // Creates the save button.
        saveButton.setText("Save Ingredient"); // Sets the button label.
        layout.addView(saveButton); // Adds the save button to the screen.
        saveButton.setOnClickListener(v -> saveItem()); // Saves the ingredient when tapped.

        Button cancelButton = new Button(this); // Creates the cancel button.
        cancelButton.setText("Cancel"); // Sets the button label.
        layout.addView(cancelButton); // Adds the cancel button below Save.
        cancelButton.setOnClickListener(v -> finish()); // Returns to the previous screen without saving.

        setContentView(layout); // Displays the completed layout.
    } // Ends buildScreen.

    private EditText createInput(String hint, LinearLayout layout) { // Creates a labelled input field.
        EditText input = new EditText(this); // Creates the text input.
        input.setHint(hint); // Shows the user what to enter.
        input.setSingleLine(true); // Keeps the field on one line.
        layout.addView(input); // Adds the input to the screen.
        return input; // Returns the field so it can be used later.
    } // Ends createInput.

    private void loadExistingItem() { // Loads an ingredient selected for editing.
        Cursor cursor = database.getPantryItem(itemId); // Queries SQLite for this ingredient.
        try { // Ensures the cursor is closed after reading.
            if (cursor.moveToFirst()) { // Checks that the ingredient exists.
                nameInput.setText(cursor.getString(1)); // Displays the saved ingredient name.
                quantityInput.setText(String.valueOf(cursor.getDouble(2))); // Displays the saved quantity.
                unitInput.setText(cursor.getString(3)); // Displays the saved unit.
                String expiry = cursor.getString(4); // Reads the saved expiry date.
                expiryInput.setText(expiry == null ? "" : expiry); // Displays the date or an empty field.
            } // Ends the record-found check.
        } finally { // Runs even if reading fails.
            cursor.close(); // Releases the database cursor.
        } // Ends cursor cleanup.
    } // Ends loadExistingItem.

    private void saveItem() { // Validates and saves the ingredient.
        String name = nameInput.getText().toString().trim(); // Reads the ingredient name.
        String quantityText = quantityInput.getText().toString().trim(); // Reads the quantity.
        String unit = unitInput.getText().toString().trim(); // Reads the measurement unit.
        String expiry = expiryInput.getText().toString().trim(); // Reads the optional expiry date.

        if (name.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) { // Checks required fields.
            Toast.makeText(this, "Enter a name, quantity and unit.", Toast.LENGTH_SHORT).show(); // Explains what is missing.
            return; // Stops saving until required fields are completed.
        } // Ends required-field validation.

        double quantity; // Holds the numeric quantity.
        try { // Checks whether the quantity is a valid number.
            quantity = Double.parseDouble(quantityText); // Converts the entered quantity to a number.
        } catch (NumberFormatException e) { // Handles text that is not a valid number.
            Toast.makeText(this, "Enter a valid quantity.", Toast.LENGTH_SHORT).show(); // Shows a validation message.
            return; // Stops saving the invalid value.
        } // Ends quantity conversion.

        if (quantity <= 0) { // Prevents zero or negative quantities.
            Toast.makeText(this, "Quantity must be greater than zero.", Toast.LENGTH_SHORT).show(); // Explains the rule.
            return; // Stops saving the invalid quantity.
        } // Ends quantity validation.

        long result; // Stores the result of the database operation.
        if (itemId == -1) { // Checks whether a new ingredient is being added.
            result = database.addPantryItem(name, quantity, unit, expiry); // Inserts the new ingredient into SQLite.
        } else { // Handles editing an existing ingredient.
            result = database.updatePantryItem(itemId, name, quantity, unit, expiry); // Updates the saved ingredient.
        } // Ends the insert-or-update decision.

        if (result != -1 && result != 0) { // Checks whether the database operation succeeded.
            Toast.makeText(this, "Ingredient saved.", Toast.LENGTH_SHORT).show(); // Confirms the save.
            finish(); // Returns to the pantry list.
        } else { // Handles an unsuccessful database operation.
            Toast.makeText(this, "Could not save the ingredient.", Toast.LENGTH_SHORT).show(); // Explains that saving failed.
        } // Ends the save-result check.
    } // Ends saveItem.

} // Ends AddEditActivity.
