
package com.example.smartpantrymanager;

// Imports Android screen and interface components.
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// Imports RecyclerView and its vertical layout manager.
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

// Imports the list used to store pantry items.
import java.util.ArrayList;

// Main screen of Smart Pantry Manager.
public class MainActivity extends Activity {

    // Connects the app to its SQLite database.
    private DatabaseHelper database;

    // Displays pantry items in a scrollable list.
    private RecyclerView pantryRecyclerView;

    // Connects pantry items to the RecyclerView.
    private PantryAdapter pantryAdapter;

    // Holds the pantry items loaded from the database.
    private ArrayList<PantryItem> pantryItems;

    // Displays a message when the pantry is empty.
    private TextView emptyMessage;

    // Builds the main screen when the app opens.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Performs the normal Android screen setup.
        super.onCreate(savedInstanceState);

        // Creates the database helper.
        database = new DatabaseHelper(this);

        // Creates an empty list of pantry items.
        pantryItems = new ArrayList<>();

        // Creates the main vertical screen layout.
        LinearLayout mainLayout = new LinearLayout(this);

        // Places screen elements vertically.
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Adds spacing around the screen content.
        mainLayout.setPadding(24, 24, 24, 24);

        // Creates the screen heading.
        TextView heading = new TextView(this);

        // Sets the heading text.
        heading.setText("Smart Pantry Manager");

        // Makes the heading larger.
        heading.setTextSize(26);

        // Adds the heading to the screen.
        mainLayout.addView(heading);

        // Creates the Add Pantry Item button.
        Button addButton = new Button(this);

        // Sets the button label.
        addButton.setText("Add Pantry Item");

        // Opens the screen for adding an item.
        addButton.setOnClickListener(view -> {
            // Creates an intent for the add screen.
            Intent intent = new Intent(this, AddEditActivity.class);

            // Opens the add screen without an existing item ID.
            startActivity(intent);
        });

        // Adds the Add button to the screen.
        mainLayout.addView(addButton);

        // Creates the Suggested Recipes button.
        Button recipesButton = new Button(this);

        // Sets the button label.
        recipesButton.setText("Suggested Recipes");

        // Opens the suggested recipes screen.
        recipesButton.setOnClickListener(view -> {
            // Creates an intent for the recipes screen.
            Intent intent = new Intent(this, RecipesActivity.class);

            // Opens the recipes screen.
            startActivity(intent);
        });

        // Adds the recipes button to the screen.
        mainLayout.addView(recipesButton);

        // Creates the Settings button.
        Button settingsButton = new Button(this);

        // Sets the button label.
        settingsButton.setText("Settings");

        // Opens the settings screen.
        settingsButton.setOnClickListener(view -> {
            // Creates an intent for the settings screen.
            Intent intent = new Intent(this, SettingsActivity.class);

            // Opens the settings screen.
            startActivity(intent);
        });

        // Adds the Settings button to the screen.
        mainLayout.addView(settingsButton);

        // Creates a message for an empty pantry.
        emptyMessage = new TextView(this);

        // Sets the empty pantry message.
        emptyMessage.setText("Your pantry is empty. Add an item to get started.");

        // Makes the message readable.
        emptyMessage.setTextSize(16);

        // Adds the message to the screen.
        mainLayout.addView(emptyMessage);

        // Creates the RecyclerView for pantry items.
        pantryRecyclerView = new RecyclerView(this);

        // Arranges pantry items vertically.
        pantryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Lets the pantry list use the remaining screen space.
        LinearLayout.LayoutParams listParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                );

        // Adds the RecyclerView to the screen.
        mainLayout.addView(pantryRecyclerView, listParams);

        // Creates the custom adapter for pantry items.
        pantryAdapter = new PantryAdapter(this, pantryItems, database);

        // Connects the adapter to the RecyclerView.
        pantryRecyclerView.setAdapter(pantryAdapter);

        // Displays the completed screen.
        setContentView(mainLayout);

        // Loads the saved pantry items.
        refreshPantryList();
    }

    // Reloads pantry data and updates the RecyclerView.
    public void refreshPantryList() {
        // Removes old items from the displayed list.
        pantryItems.clear();

        // Retrieves pantry items from SQLite.
        Cursor cursor = database.getPantryItems();

        // Reads each saved pantry item.
        while (cursor.moveToNext()) {
            // Converts the database row into a PantryItem object.
            PantryItem item = new PantryItem(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );

            // Adds the item to the list.
            pantryItems.add(item);
        }

        // Closes the database cursor.
        cursor.close();

        // Shows the empty message only when no items exist.
        emptyMessage.setVisibility(
                pantryItems.isEmpty() ? View.VISIBLE : View.GONE
        );

        // Refreshes the RecyclerView rows.
        pantryAdapter.notifyDataSetChanged();
    }

    // Refreshes the pantry whenever this screen becomes active again.
    @Override
    protected void onResume() {
        // Performs the normal Android resume operation.
        super.onResume();

        // Reloads the pantry if the screen has been initialized.
        if (database != null && pantryAdapter != null) {
            // Displays any items added or edited on another screen.
            refreshPantryList();
        }
    }
}

