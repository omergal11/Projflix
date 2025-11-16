package com.example.netflixandroid.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.netflixandroid.entitles.Category;
import com.example.netflixandroid.repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;  //repository to handle the data of the category
    private MutableLiveData<String> statusMessage;  //message to show the status of the operation
    private MutableLiveData<List<Category>> categories; //list of the categories

    public CategoryViewModel(Application application) {
        super(application);
        categories = new MutableLiveData<>();
        statusMessage = new MutableLiveData<>();
        categoryRepository = new CategoryRepository(categories,statusMessage);
    }
    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void createCategory(Category category) {
        categoryRepository.createCategory(category);
    }
    public void updateCategory(Category category,Category updatedCategory) {
        categoryRepository.updateCategory(category,updatedCategory );
    }
    public void deleteCategory(Category category) {
        categoryRepository.deleteCategory(category);
    }
    public void uploadCategories() {
        categoryRepository.loadCategoriesFromAPI();
    }

    public void uploadCategoriesFromLocal() {
        categoryRepository.loadCategoriesFromLocal();
    }

    public void uploadCategory(Category category) {
        categoryRepository.getCategoryDitails(category);
    }
}
