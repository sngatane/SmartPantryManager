
package com.example.smartpantrymanager; // Places this class in the app's package.

import android.app.Activity; // Provides the base class for this screen.
import android.os.Bundle; // Provides saved screen state information.
import android.graphics.Typeface; // Allows us to style the screen heading.
import android.view.Gravity; // Allows us to centre text on the screen.
import android.view.ViewGroup; // Provides layout size constants.
import android.widget.Button; // Provides clickable buttons.
import android.widget.LinearLayout; // Arranges screen elements vertically.
import android.widget.ScrollView; // Allows the screen to scroll if needed.
import android.widget.TextView; // Displays text on the screen.

public class SettingsActivity extends Activity { // Defines the Settings screen.

    @Override // Indicates that this method replaces the parent class method.
    protected void onCreate(Bundle savedInstanceState) { // Runs when the screen opens.
        super.onCreate(savedInstanceState); // Performs the normal Activity setup.
        createSettingsScreen(); // Builds the screen's user interface.
    } // Ends the onCreate method.

    private void createSettingsScreen() { // Creates and arranges the screen elements.
        ScrollView scrollView = new ScrollView(this); // Makes the content scrollable.
        LinearLayout layout = new LinearLayout(this); // Creates the main screen layout.
        layout.setOrientation(LinearLayout.VERTICAL); // Places elements one below another.
        layout.setPadding(40, 40, 40, 40); // Adds space around the content.
        scrollView.addView(layout); // Places the layout inside the scrollable area.

        TextView heading = new TextView(this); // Creates the screen heading.
        heading.setText("Settings"); // Sets the heading text.
        heading.setTextSize(28); // Makes the heading larger.
        heading.setTypeface(null, Typeface.BOLD); // Makes the heading bold.
        heading.setGravity(Gravity.CENTER); // Centres the heading text.
        layout.addView(heading); // Adds the heading to the screen.

        TextView appName = new TextView(this); // Creates the app information heading.
        appName.setText("Smart Pantry Manager"); // Displays the app name.
        appName.setTextSize(21); // Sets the app name text size.
        appName.setTypeface(null, Typeface.BOLD); // Makes the app name bold.
        appName.setPadding(0, 35, 0, 10); // Adds space above and below the heading.
        layout.addView(appName); // Adds the app name to the layout.

        TextView version = new TextView(this); // Creates the version information.
        version.setText("Version 1.0"); // Displays the current app version.
        version.setTextSize(16); // Sets the version text size.
        layout.addView(version); // Adds the version information to the screen.

        TextView matchingHeading = new TextView(this); // Creates a heading for recipe matching.
        matchingHeading.setText("How recipe matching works"); // Explains the matching section.
        matchingHeading.setTextSize(20); // Sets the section heading size.
        matchingHeading.setTypeface(null, Typeface.BOLD); // Makes the section heading bold.
        matchingHeading.setPadding(0, 30, 0, 10); // Adds space around the heading.
        layout.addView(matchingHeading); // Adds the matching heading to the layout.

        TextView matchingInfo = new TextView(this); // Creates the matching explanation.
        matchingInfo.setText("Recipes are suggested only when your pantry contains every required ingredient in the required quantity. A recipe is not suggested when an ingredient is missing or the available quantity is too low. Ingredient units must also match."); // Explains the strict matching rules.
        matchingInfo.setTextSize(16); // Sets the explanation text size.
        matchingInfo.setLineSpacing(4, 1.0f); // Makes the paragraph easier to read.
        layout.addView(matchingInfo); // Adds the explanation to the screen.

        TextView storageHeading = new TextView(this); // Creates a heading for storage information.
        storageHeading.setText("Data storage"); // Sets the storage heading.
        storageHeading.setTextSize(20); // Sets the heading text size.
        storageHeading.setTypeface(null, Typeface.BOLD); // Makes the heading bold.
        storageHeading.setPadding(0, 30, 0, 10); // Adds space around the heading.
        layout.addView(storageHeading); // Adds the storage heading to the layout.

        TextView storageInfo = new TextView(this); // Creates the storage explanation.
        storageInfo.setText("Pantry items and recipe information are stored in a local SQLite database on this device. Pantry changes remain available after you close and reopen the app."); // Explains local database storage.
        storageInfo.setTextSize(16); // Sets the storage explanation text size.
        storageInfo.setLineSpacing(4, 1.0f); // Improves paragraph readability.
        layout.addView(storageInfo); // Adds the storage explanation to the screen.

        Button backButton = new Button(this); // Creates a button to leave Settings.
        backButton.setText("Back to Pantry"); // Sets the button label.
        backButton.setOnClickListener(view -> finish()); // Closes Settings and returns to the previous screen.
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); // Makes the button fill the available width.
        buttonParams.setMargins(0, 35, 0, 0); // Adds space above the button.
        layout.addView(backButton, buttonParams); // Adds the button to the layout.

        setContentView(scrollView); // Displays the completed Settings screen.
    } // Ends the screen-building method.
} // Ends the SettingsActivity class.
