package com.example.netflixandroid.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.R;
import com.example.netflixandroid.entitles.Category;

import java.util.List;

// Adapter for the category list
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }
    // Constructor
    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    // Create the view holder
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_edit, parent, false);
        return new CategoryViewHolder(itemView);
    }
    // Bind the view holder
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        Category category = categories.get(position);

        holder.nameTextView.setText(category.getName());
        holder.promotedSwitch.setChecked(category.isPromoted());
        holder.promotedSwitch.setEnabled(false);

        holder.itemView.setOnClickListener(v -> {
            listener.onCategoryClick(category);
        });
    }
    // Get the item count
    @Override
    public int getItemCount() {
        if (categories != null) {
            return categories.size();
        } else {
            return 0;
        }
    }
    // View holder class
    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Switch promotedSwitch;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.categoryNameTextView);
            promotedSwitch = itemView.findViewById(R.id.promotedSwitch);
        }
    }
}
