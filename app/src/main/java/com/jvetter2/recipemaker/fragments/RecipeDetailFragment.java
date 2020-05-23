package com.jvetter2.recipemaker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jvetter2.recipemaker.MainActivity;
import com.jvetter2.recipemaker.R;

public class RecipeDetailFragment extends Fragment {
  int position = 0;
  TextView recipeNameTV;
  TextView recipeCategoryTV;
  TextView recipeIngredientsTV;
  TextView recipeInstructionsTV;
  public static final String TAG = "RecipeDetailFragment";

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if(savedInstanceState == null){
      if(getArguments() != null) {
        position = getArguments().getInt("position", 0);
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_detail, parent, false);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_edit:
        AddEditFragment addEditFragment = new AddEditFragment();
        Bundle args = new Bundle();
        args.putString("action", getString(R.string.menu_edit));
        args.putString("name" ,recipeNameTV.getText().toString());
        args.putString("category" ,recipeCategoryTV.getText().toString());
        args.putString("ingredients" ,recipeIngredientsTV.getText().toString());
        args.putString("instructions" ,recipeInstructionsTV.getText().toString());
        addEditFragment.setArguments(args);

        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft2 = mFragmentManager.beginTransaction();
        ft2.addToBackStack(AddEditFragment.TAG);
        ft2.replace(((ViewGroup)(getView().getParent())).getId(), addEditFragment, AddEditFragment.TAG);
        ft2.commit();
        return true;
      case R.id.action_delete:
        Bundle deleteArgs = new Bundle();
        deleteArgs.putString("name" ,recipeNameTV.getText().toString());
        final ConfirmationDialogFragment confirmationFragment = new ConfirmationDialogFragment();
        confirmationFragment.setArguments(deleteArgs);
        confirmationFragment.show(getActivity().getSupportFragmentManager().beginTransaction(), "tag");
        default:
            getActivity().onBackPressed();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    recipeNameTV = (TextView) view.findViewById(R.id.recipeNameTV);
    recipeCategoryTV = (TextView) view.findViewById(R.id.recipeCategoryTV);
    recipeIngredientsTV = (TextView) view.findViewById(R.id.recipeIngredientsTV);
    recipeInstructionsTV = (TextView) view.findViewById(R.id.recipeInstructionsTV);

    recipeNameTV.setText(MainActivity.recipeNames.get(position).toString());
    recipeCategoryTV.setText(MainActivity.recipeCategory.get(position).toString());
    recipeIngredientsTV.setText(MainActivity.recipeIngredients.get(position).toString());
    recipeInstructionsTV.setText(MainActivity.recipeInstructions.get(position).toString());

    ((AppCompatActivity)getActivity()).getSupportActionBar();
    ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}
