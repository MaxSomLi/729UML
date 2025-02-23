package com.example.a729;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalTime;

public class FiveByFive extends NumberByNumber {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FACT = 0.187f;
        TILES = 5;
        TILE_COUNT = TILES * TILES;
        TILES_FEWER = TILES - DECR1;
        TILES_EVEN_FEWER = TILES - DECR2;
        TILES_FEWER_MERGE = TILES - DECR3;
        tilesOnBoard = new Button[TILES][TILES];
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_five_by_five);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            gestureDetector = new GestureDetector(this, this);
            return insets;
        });
        tiles = new Button[][] {
                {findViewById(R.id.tile00), findViewById(R.id.tile01), findViewById(R.id.tile02), findViewById(R.id.tile03), findViewById(R.id.tile04)},
                {findViewById(R.id.tile10), findViewById(R.id.tile11), findViewById(R.id.tile12), findViewById(R.id.tile13), findViewById(R.id.tile14)},
                {findViewById(R.id.tile20), findViewById(R.id.tile21), findViewById(R.id.tile22), findViewById(R.id.tile23), findViewById(R.id.tile24)},
                {findViewById(R.id.tile30), findViewById(R.id.tile31), findViewById(R.id.tile32), findViewById(R.id.tile33), findViewById(R.id.tile34)},
                {findViewById(R.id.tile40), findViewById(R.id.tile41), findViewById(R.id.tile42), findViewById(R.id.tile43), findViewById(R.id.tile44)}
        };
        width = getResources().getDisplayMetrics().widthPixels * FACT;
        int index = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            index = LocalTime.now().getSecond() % TILE_COUNT;
        }
        int i = index / TILES, j = index % TILES;
        for (int t1 = 0; t1 < TILES; t1++) {
            for (int t2 = 0; t2 < TILES; t2++) {
                tiles[t1][t2].setX(width * t2);
                tiles[t1][t2].setY(width * t1);
                tiles[t1][t2].setVisibility(View.GONE);
            }
        }
        tiles[i][j].setVisibility(View.VISIBLE);
        tiles[i][j].setText("3");
        Button unchoose = findViewById(R.id.unchoose);
        unchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });
    }
}
