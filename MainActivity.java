package com.example.lin01;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;
import android.widget.*;
public class MainActivity extends AppCompatActivity {
    Button button = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.ibike);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        button = (Button)findViewById(R.id.bus);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,list.class);
                startActivity(intent);
            }
        });
    }

}

//public class MainActivity extends AppCompatActivity {
//    private Button button;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        button = (Button) findViewById(R.id.map);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openmap();
//            }
//        });
//
//    }
//
//    public void openmap() {
//        Intent intent = new Intent(this, map.class);
//
//        startActivity(intent);
//    }
//}

