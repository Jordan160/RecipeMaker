package com.jvetter2.recipemaker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.jvetter2.recipemaker.fragments.RecipeDetailFragment;
import com.jvetter2.recipemaker.fragments.RecipeMenuFragment;
import com.jvetter2.recipemaker.fragments.SearchFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeMenuFragment.OnItemSelectedListener {
    FragmentManager mFragmentManager;
    public static ArrayList recipeNames = new ArrayList();
    public static ArrayList recipeCategory = new ArrayList();
    public static ArrayList recipeIngredients = new ArrayList();
    public static ArrayList recipeInstructions = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = this.getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.search:
                        SearchFragment searchFragment = new SearchFragment();
                        FragmentTransaction ft = mFragmentManager.beginTransaction();
                        ft.addToBackStack(SearchFragment.TAG);
                        ft.replace(R.id.flContainer, searchFragment, SearchFragment.TAG);
                        ft.commit();
                        return true;
                }
                return false;
            }
        });

        recipeNames.clear();
        recipeCategory.clear();
        recipeIngredients.clear();
        recipeInstructions.clear();

        SQLiteDatabase myDatabase = this.openOrCreateDatabase("Recipes", MODE_PRIVATE, null);

        try {
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS recipes (name VARCHAR, category VARCHAR, " +
                    "ingredients VARCHAR, instructions VARCHAR)");

            Cursor c = myDatabase.rawQuery("SELECT * FROM recipes ORDER BY name COLLATE NOCASE ASC", null);

            int nameIndex = c.getColumnIndex("name");
            int categoryIndex = c.getColumnIndex("category");
            int ingredientsIndex = c.getColumnIndex("ingredients");
            int instructionsIndex = c.getColumnIndex("instructions");

            c.moveToFirst();

            while (!c.isAfterLast()) {
                Log.i("name: ", c.getString(nameIndex));
                Log.i("category: ", c.getString(categoryIndex));
                Log.i("ingredients: ", c.getString(ingredientsIndex));
                Log.i("instructions: ", c.getString(instructionsIndex));

                recipeNames.add(c.getString(nameIndex));
                recipeCategory.add(c.getString(categoryIndex));
                recipeIngredients.add(c.getString(ingredientsIndex));
                recipeInstructions.add(c.getString(instructionsIndex));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("DEBUG", getResources().getConfiguration().orientation + "");

        if (savedInstanceState == null) {
            RecipeMenuFragment firstFragment = new RecipeMenuFragment();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(R.id.flContainer, firstFragment, RecipeMenuFragment.TAG);
            ft.commit();
        }
    }

    @Override
    public void onMenuItemSelected(int position) {
        RecipeDetailFragment secondFragment = new RecipeDetailFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        secondFragment.setArguments(args);

        getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, secondFragment, RecipeDetailFragment.TAG) // replace flContainer
                    .addToBackStack(null)
                    .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}