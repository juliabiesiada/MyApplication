package com.arapp.buildit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private ListView Categories;
    private String[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);


        categories = getIntent().getExtras().getStringArray("CATEGORIES_LIST");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.single_category, categories);
        ListView listView = (ListView) findViewById(R.id.categories_list);
        listView.setAdapter(adapter);
    }
}
