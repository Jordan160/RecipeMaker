package com.jvetter2.recipemaker.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jvetter2.recipemaker.MainActivity;
import com.jvetter2.recipemaker.R;
import com.jvetter2.recipemaker.data.RecipePreview;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecipeArrayAdapter extends RecyclerView.Adapter<RecipeArrayAdapter.ViewHolder>{
    private List<RecipePreview> mList;
    private Context mContext;

    public RecipeArrayAdapter(List<RecipePreview> list, Context context){
        super();
        mList = list;
        mContext = context;
    }

    //     get the size of the list
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    // specify the row layout file and click for each row
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        RecipePreview recipe = mList.get(listPosition);
        ((ViewHolder) holder).recipeName.setText(recipe.getName());
        ((ViewHolder) holder).recipeIngredients.setText(recipe.getRecipeIngredients());
        ((ViewHolder) holder).recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("position", String.valueOf(holder.getAdapterPosition()));
                ArrayList<String> recipeToAdd = new ArrayList<String>();
                Log.i("Recipe Name: ", mList.get(holder.getAdapterPosition()).getName());

                final SQLiteDatabase recipeDatabase = v.getContext().openOrCreateDatabase("Recipes", MODE_PRIVATE, null);

                ContentValues cv = new ContentValues();
                cv.put("name", mList.get(holder.getAdapterPosition()).getName());
                cv.put("category", "Other");
                cv.put("ingredients", mList.get(holder.getAdapterPosition()).getRecipeIngredients());
                cv.put("instructions", mList.get(holder.getAdapterPosition()).getRecipeInstructions());

                try {
                    long count = recipeDatabase.insert(
                            "recipes",
                            null,
                            cv);
//                    Log.i("Count: ", String.valueOf(count));
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(v.getContext(), "Recipe Saved!", Toast.LENGTH_SHORT).show();
                holder.recipeButton.setVisibility(View.INVISIBLE);
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeName;
        public TextView recipeIngredients;
        public Button recipeButton;


        public ViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipeName);
            recipeIngredients = (TextView) itemView.findViewById(R.id.recipeIngredients);
            recipeButton = (Button) itemView.findViewById(R.id.recipeSaveButton);
        }
    }
}

