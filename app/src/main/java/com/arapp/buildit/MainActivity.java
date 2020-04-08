package com.arapp.buildit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wikitude.WikitudeSDK;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WikitudeSDK.deleteRootCacheDirectory(this);

        //Start button
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), FindProductActivity.class);
                startActivity(startIntent);
                //do zmiany - nie można wyłączyć apki
                finish();
            }
        });



    }
}
