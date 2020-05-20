package com.jvetter2.recipemaker.fragments;


import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jvetter2.recipemaker.MainActivity;
import com.jvetter2.recipemaker.R;

import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditFragment extends Fragment {
    public static final String TAG = "AddEditFragment";
    EditText recipeNameET;
    Spinner categorySpinner;
    EditText recipeIngredientsET;
    EditText recipeInstructionsET;
    FloatingActionButton save;
    String name;
    String category;
    String ingredients;
    String instructions;
    String[] recipeCategories;

    public AddEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_add_edit, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Resources res = getResources();
        recipeCategories = res.getStringArray(R.array.category_items);
        categorySpinner = view.findViewById(R.id.recipeCategorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, recipeCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setPadding(0, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        categorySpinner.setAdapter(adapter);

        final SQLiteDatabase recipeDatabase = getActivity().openOrCreateDatabase("Recipes", MODE_PRIVATE, null);
        recipeNameET = view.findViewById(R.id.recipeNameET);
        recipeIngredientsET = view.findViewById(R.id.recipeIngredientsET);
        recipeInstructionsET = view.findViewById(R.id.recipeInstructionsET);
        save = view.findViewById(R.id.saveFloatingActionButton);

        Bundle bundle = this.getArguments();
        String action = bundle.getString("action", "add");
        name = bundle.getString("name", "");
        category = bundle.getString("category", "");
        ingredients = bundle.getString("ingredients", "");
        instructions = bundle.getString("instructions", "");

        if (action.equalsIgnoreCase(getString(R.string.menu_edit))) {
            updateExistingRecipe(recipeDatabase);
        } else {
            saveNewRecipe(recipeDatabase);
        }

    }

    private void updateExistingRecipe(final SQLiteDatabase recipeDatabase) {
        recipeNameET.setText(name);
        categorySpinner.setSelection(Arrays.asList(recipeCategories).indexOf(category));
        recipeIngredientsET.setText(ingredients);
        recipeInstructionsET.setText(instructions);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    if (!recipeNameET.getText().toString().equalsIgnoreCase(name) ||
                            !category.equalsIgnoreCase(categorySpinner.getSelectedItem().toString()) ||
                            !recipeIngredientsET.getText().toString().equalsIgnoreCase(ingredients) ||
                            !recipeInstructionsET.getText().toString().equalsIgnoreCase(instructions)) {
                        ContentValues cv = new ContentValues();
                        cv.put("name", recipeNameET.getText().toString());
                        cv.put("category", categorySpinner.getSelectedItem().toString());
                        cv.put("ingredients", recipeIngredientsET.getText().toString());
                        cv.put("instructions", recipeInstructionsET.getText().toString());

                        String selection = "name" + " LIKE ?";
                        String[] selectionArgs = {name};

                        try {
                            int count = recipeDatabase.update(
                                    "recipes",
                                    cv,
                                    selection,
                                    selectionArgs);
                            Log.i("Count: ", String.valueOf(count));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Data is the same, no changes were made", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                }
            }
        });
    }

    private void saveNewRecipe(final SQLiteDatabase recipeDatabase) {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    ContentValues cv = new ContentValues();
                    cv.put("name", recipeNameET.getText().toString());
                    cv.put("category", categorySpinner.getSelectedItem().toString());
                    cv.put("ingredients", recipeIngredientsET.getText().toString());
                    cv.put("instructions", recipeInstructionsET.getText().toString());

                    try {
                        long count = recipeDatabase.insert(
                                "recipes",
                                null,
                                cv);
                        Log.i("Count: ", String.valueOf(count));
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Boolean validateFields() {
        if(recipeNameET.getText().toString().isEmpty()) {
            recipeNameET.setError("Please provide a recipe name");
            return false;
        }

        if(categorySpinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.recipe_category))) {
            TextView errorText = (TextView)categorySpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Please select a Recipe Category");
            return false;
        }

        if(recipeInstructionsET.getText().toString().isEmpty()) {
            recipeInstructionsET.setError("Please provide recipe instructions");
            return false;
        }

        if(recipeIngredientsET.getText().toString().isEmpty()) {
            recipeIngredientsET.setError("Please provide recipe ingredients");
            return false;
        }
        return true;
    }
}