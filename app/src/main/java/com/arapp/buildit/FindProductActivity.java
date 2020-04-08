package com.arapp.buildit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FindProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_product);


        //Search button
        Button searchButton = (Button) findViewById(R.id.bSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        Button scanButton = (Button) findViewById(R.id.bScan);
        scanButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent scanIntent = new Intent(getApplicationContext(), ImageTrackingActivity.class);
                startActivity(scanIntent);
            }
        });

    }
}
