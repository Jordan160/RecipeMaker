package com.jvetter2.recipemaker.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.jvetter2.recipemaker.R;
import com.jvetter2.recipemaker.data.RecipeList;
import com.jvetter2.recipemaker.data.RecipePreview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchFragment extends Fragment {
    public static final String TAG = "SearchFragment";
    private RecipeArrayAdapter mAdapter;
    private List<RecipePreview> mList = new ArrayList<>();
    private RecyclerView recyclerView;
    public ArrayList<String> recipeNameArray = new ArrayList<>();
    public ArrayList<String> recipeIngredientsArray = new ArrayList<>();
    public ArrayList<String> recipeInstructionsArray = new ArrayList<>();
    Button recipeSearchButton;
    EditText recipeSearchET;
    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recipe_list);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recipeSearchButton = view.findViewById(R.id.search);
        recipeSearchET = view.findViewById(R.id.searchEditText);
        recipeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recipeSearchET.getText().toString().equalsIgnoreCase("")) {
                    getRecipe();
                } else {
                    recipeSearchET.setError(getString(R.string.recipe_error));
                }
            }
        });
        recipeSearchET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (!recipeSearchET.getText().toString().equalsIgnoreCase("")) {
                                getRecipe();
                            } else {
                                recipeSearchET.setError(getString(R.string.recipe_error));
                            }
                            return true;
                        default:
                            break;
                    }
                } return false;
            }
        });
    }

    private void getRecipe() {
        recipeNameArray.clear();
        recipeIngredientsArray.clear();
        recipeInstructionsArray.clear();
        mList.clear();

        hideKeyboard(getActivity());
        RecipeList recipeList = new RecipeList();
        String baseUrl = getString(R.string.base_url);
        String url = baseUrl.replace("PLACEHOLDER", recipeSearchET.getText());
        String recipe = null;
        JSONObject jsonObject = null;

        try {
            recipe = recipeList.execute(url).get();
            jsonObject = new JSONObject(recipe);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("hits");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i).getJSONObject("recipe");
                //String ingredients = c.getString("ingredientLines").replace("[", "").replace("]", "").replace(",", "\n");
                JSONArray ingredients = c.getJSONArray("ingredients");
                String ingredientsFinal = "";
                for (int it = 0; it < ingredients.length(); it++) {
                    ingredientsFinal += "(" + (it + 1) + ") " + ingredients.getJSONObject(it).getString("text") + "\n";
                }

                recipeNameArray.add(c.getString("label"));
                recipeIngredientsArray.add(ingredientsFinal);
                recipeInstructionsArray.add(c.getString("shareAs"));

                RecipePreview recipePreview = new RecipePreview();
                recipePreview.setName(c.getString("label"));
                recipePreview.setRecipeIngredients(ingredientsFinal);
                recipePreview.setRecipeInstructions(c.getString("shareAs"));
                mList.add(recipePreview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new RecipeArrayAdapter(mList, getActivity().getApplicationContext());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
