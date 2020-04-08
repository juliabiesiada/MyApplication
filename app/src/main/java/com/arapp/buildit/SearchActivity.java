package com.arapp.buildit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private ListView Rooms;
    private String[] rooms;
    private Map<String, String[]> room_to_cat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        HashMap<String, String[]> tmp = new HashMap<>();
        tmp.put("Sypialnia", getResources().getStringArray(R.array.bedroom_items));
        tmp.put("Kuchnia", getResources().getStringArray(R.array.kitchen_items));
        tmp.put("Jadalnia", getResources().getStringArray(R.array.dining_room_items));
        tmp.put("Pokój dzienny", getResources().getStringArray(R.array.living_room_items));
        tmp.put("Łazienka", getResources().getStringArray(R.array.bathroom_items));
        tmp.put("Balkon", getResources().getStringArray(R.array.balcony_items));
        room_to_cat = Collections.unmodifiableMap(tmp);

        rooms = getResources().getStringArray(R.array.menu);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.single_category, rooms);
        final ListView listView = (ListView) findViewById(R.id.rooms_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String room = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
                intent.putExtra("CATEGORIES_LIST", room_to_cat.get(room));
                startActivity(intent);
            }

        });

    }
}
