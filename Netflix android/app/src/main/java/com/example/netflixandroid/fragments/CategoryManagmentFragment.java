package com.example.netflixandroid.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.R;
import com.example.netflixandroid.adapters.CategoryAdapter;
import com.example.netflixandroid.entitles.Category;
import com.example.netflixandroid.viewmodels.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagmentFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapterList;
    private List<Category> categoryList = new ArrayList<>();
    private FloatingActionButton fabAddCategory;

    public CategoryManagmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View rootView = inflater.inflate(R.layout.fragment_category_managment, container, false);

        // Initialize the RecyclerView
        categoryRecyclerView = rootView.findViewById(R.id.recyclerViewCategories);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapterList = new CategoryAdapter(categoryList, category -> {
            // Edit category
            showCategoryDialog(category);
        });
        categoryRecyclerView.setAdapter(categoryAdapterList);

        // Initialize the FAB
        fabAddCategory = rootView.findViewById(R.id.fabAddCategory);
        fabAddCategory.setOnClickListener(v -> showCategoryDialog(null));

        // ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            categoryAdapterList.notifyDataSetChanged();
        });
        categoryViewModel.getStatusMessage().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                // Show Toast for any status change
                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    // Dialog for adding/editing a category
    private void showCategoryDialog(Category category) {
        // Create a dialog with EditText and Switch
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(category == null ? getString(R.string.add_category) : getString(R.string.edit_category));

        View view = getLayoutInflater().inflate(R.layout.dialog_category, null);
        EditText nameEditText = view.findViewById(R.id.editTextCategoryName);
        Switch promotedSwitch = view.findViewById(R.id.switchPromoted);

        if (category != null) {
            nameEditText.setText(category.getName());
            promotedSwitch.setChecked(category.isPromoted());
        }

        builder.setView(view);

        builder.setPositiveButton(category == null ? getString(R.string.add) : getString(R.string.update), (dialog, which) -> {
            String name = nameEditText.getText().toString();
            boolean promoted = promotedSwitch.isChecked();
            Category newCategory = new Category(name, promoted, new ArrayList<>());

            if (category == null) {
                categoryViewModel.createCategory(newCategory);
            } else {
                categoryViewModel.updateCategory(category, newCategory);
            }
        });

        // Delete button if the category exists
        if (category != null) {
            builder.setNegativeButton(getString(R.string.delete), (dialog, which) -> {
                categoryViewModel.deleteCategory(category);
            });
        } else {
            builder.setNegativeButton(getString(R.string.cancel), null);
        }

        builder.show();
    }
}
