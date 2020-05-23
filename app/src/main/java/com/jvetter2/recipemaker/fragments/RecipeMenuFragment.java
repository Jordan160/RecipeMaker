package com.jvetter2.recipemaker.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jvetter2.recipemaker.MainActivity;
import com.jvetter2.recipemaker.R;

public class RecipeMenuFragment extends Fragment {

  public static final String TAG = "RecipeMenuFragment";
  ArrayAdapter<String> itemsAdapter;
  FragmentManager mFragmentManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, MainActivity.recipeNames);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_menu, parent, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    ListView lvItems = (ListView) view.findViewById(R.id.lvItems);
    lvItems.setAdapter(itemsAdapter);

    ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.onMenuItemSelected(position);
      }
    });
  }

  private OnItemSelectedListener listener;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if(context instanceof OnItemSelectedListener){
      this.listener = (OnItemSelectedListener) context;
    } else {
      throw new ClassCastException(context.toString()
        + " must implement RecipeMenuFragment.OnItemSelectedListener");
    }
  }

  public interface OnItemSelectedListener {
    void onMenuItemSelected(int position);
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_add_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.addButton) {
      AddEditFragment addEditFragment = new AddEditFragment();
      Bundle args = new Bundle();
      args.putString("action", getString(R.string.menu_add));
      addEditFragment.setArguments(args);
      FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
      FragmentTransaction ft2 = mFragmentManager.beginTransaction();
      ft2.addToBackStack(AddEditFragment.TAG);
      ft2.replace(((ViewGroup) (getView().getParent())).getId(), addEditFragment, AddEditFragment.TAG);
      ft2.commit();
    }
    return true;
  }
}