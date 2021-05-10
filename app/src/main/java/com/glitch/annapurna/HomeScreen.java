package com.glitch.annapurna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ImageButton givefood = (ImageButton) findViewById(R.id.givefood);
        givefood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GiveFood.class);
                view.getContext().startActivity(intent);}
        });
        ImageButton needfood = (ImageButton) findViewById(R.id.needfood);
        needfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NeedFood.class);
                view.getContext().startActivity(intent);}
        });
    }

}
