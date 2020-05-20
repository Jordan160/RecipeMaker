package com.jvetter2.recipemaker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.jvetter2.recipemaker.MainActivity;
import com.jvetter2.recipemaker.R;

import static android.content.Context.MODE_PRIVATE;

public class ConfirmationDialogFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "name";
    private String name;

    public ConfirmationDialogFragment(){};

    public static ConfirmationDialogFragment newInstance(String param1) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

                builder.setTitle(R.string.confirm_title);
                builder.setMessage(R.string.confirm_delete);
                builder.setNegativeButton(R.string.confirm_cancel, null);
                builder.setPositiveButton(android.R.string.yes,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //RecipeDetailFragment.deleteRecipe();
                        SQLiteDatabase recipeDatabase = getActivity().openOrCreateDatabase("Recipes", MODE_PRIVATE, null);
                          try {
                            deleteRecipe(name, recipeDatabase);
                          } catch (Exception e) {
                            e.printStackTrace();
                          }
                          Intent intent = new Intent(getActivity(), MainActivity.class);
                          startActivity(intent);
                    }
                });
        return builder.create();
    }

    public static boolean deleteRecipe(String name, SQLiteDatabase recipeDatabase) {
        return recipeDatabase.delete("recipes", "name" + " = ?", new String[] { name }) > 0;
    }
}

