
package com.example.smartpantrymanager;

// Imports the Android layout and view classes.
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

// Imports RecyclerView.
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Imports the list collection.
import java.util.List;

// Custom adapter that connects pantry data to the RecyclerView.
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    // Stores the screen context.
    private final Context context;

    // Stores the pantry items to display.
    private final List<PantryItem> pantryItems;

    // Stores the database helper used for deletion.
    private final DatabaseHelper database;

    // Receives the context, pantry list, and database helper.
    public PantryAdapter(Context context, List<PantryItem> pantryItems, DatabaseHelper database) {
        // Saves the supplied context.
        this.context = context;

        // Saves the supplied pantry list.
        this.pantryItems = pantryItems;

        // Saves the supplied database helper.
        this.database = database;
    }

    // Creates a new row for a pantry item.
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates a vertical layout for the row.
        LinearLayout row = new LinearLayout(context);

        // Arranges row contents vertically.
        row.setOrientation(LinearLayout.VERTICAL);

        // Adds space inside the row.
        row.setPadding(24, 20, 24, 20);

        // Returns the row wrapped in a ViewHolder.
        return new PantryViewHolder(row);
    }

    // Fills a row with the pantry item's details.
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        // Gets the pantry item for this row.
        PantryItem item = pantryItems.get(position);

        // Displays the name, quantity, and unit.
        holder.itemText.setText(item.name + " — " + item.quantity + " " + item.unit);

        // Creates an Edit button.
        holder.editButton.setText("Edit");

        // Creates a Delete button.
        holder.deleteButton.setText("Delete");

        // Opens AddEditActivity with this item's ID when Edit is tapped.
        holder.editButton.setOnClickListener(view -> {
            // Creates an intent to open the editing screen.
            android.content.Intent intent =
                    new android.content.Intent(context, AddEditActivity.class);

            // Passes the ID of the selected pantry item.
            intent.putExtra("item_id", item.id);

            // Opens the editing screen.
            context.startActivity(intent);
        });

        // Deletes this item after confirmation.
        holder.deleteButton.setOnClickListener(view -> {
            // Asks the user to confirm deletion.
            new AlertDialog.Builder(context)
                    .setTitle("Delete pantry item")
                    .setMessage("Delete " + item.name + "?")
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Deletes the item from the database.
                        database.deletePantryItem(item.id);

                        // Refreshes the list using MainActivity.
                        if (context instanceof MainActivity) {
                            // Reloads the pantry list after deletion.
                            ((MainActivity) context).refreshPantryList();
                        }
                    })
                    .show();
        });

        // Creates a horizontal row for the two buttons.
        LinearLayout buttons = new LinearLayout(context);

        // Arranges the buttons side by side.
        buttons.setOrientation(LinearLayout.HORIZONTAL);

        // Adds the Edit button to the row.
        buttons.addView(holder.editButton);

        // Adds the Delete button to the row.
        buttons.addView(holder.deleteButton);

        // Adds the buttons to the item layout.
        holder.row.addView(buttons);
    }

    // Returns the number of pantry items.
    @Override
    public int getItemCount() {
        // Reports how many rows RecyclerView should display.
        return pantryItems.size();
    }

    // Holds references to the views in one pantry row.
    static class PantryViewHolder extends RecyclerView.ViewHolder {

        // Displays the pantry item details.
        TextView itemText;

        // Opens the editing screen.
        Button editButton;

        // Deletes the pantry item.
        Button deleteButton;

        // Holds all the row's views.
        LinearLayout row;

        // Finds or creates the views for this row.
        PantryViewHolder(@NonNull View itemView) {
            // Passes the row view to the parent ViewHolder.
            super(itemView);

            // Saves the row layout.
            row = (LinearLayout) itemView;

            // Creates the item description.
            itemText = new TextView(itemView.getContext());

            // Sets a readable text size.
            itemText.setTextSize(18);

            // Creates the Edit button.
            editButton = new Button(itemView.getContext());

            // Creates the Delete button.
            deleteButton = new Button(itemView.getContext());

            // Adds the description to the row.
            row.addView(itemText);
        }
    }
}
