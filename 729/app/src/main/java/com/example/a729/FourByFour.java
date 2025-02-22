package com.example.a729;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalTime;
import java.util.Vector;

public class FourByFour extends AppCompatActivity implements GestureDetector.OnGestureListener {

    enum WhatToDo {
        RIGHT,
        DOWN,
        LEFT,
        UP,
        FINISH
    }

    private GestureDetector gestureDetector;
    private float width;
    private final long SPEED = 30;
    private final float FACT = 0.225f;
    private final int SWIPE_THRESHOLD = 100;
    private final int SWIPE_VELOCITY_THRESHOLD = 100;
    private final int TILES = 4;
    private final int TILE_COUNT = TILES * TILES;
    private final int DECR1 = 1;
    private final int DECR2 = 2;
    private final int DECR3 = 3;
    private final int TILES_FEWER = TILES - DECR1;
    private final int TILES_EVEN_FEWER = TILES - DECR2;
    private final int TILES_FEWER_MERGE = TILES - DECR3;
    boolean add = false;
    boolean canSwipe = true;
    Button[][] tiles;
    Button[][] tilesOnBoard = new Button[][]{
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_four_by_four);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            gestureDetector = new GestureDetector(this, this);
            return insets;
        });

        width = getResources().getDisplayMetrics().widthPixels * FACT;

        tiles = new Button[][]{
                {findViewById(R.id.tile00), findViewById(R.id.tile01), findViewById(R.id.tile02), findViewById(R.id.tile03)},
                {findViewById(R.id.tile10), findViewById(R.id.tile11), findViewById(R.id.tile12), findViewById(R.id.tile13)},
                {findViewById(R.id.tile20), findViewById(R.id.tile21), findViewById(R.id.tile22), findViewById(R.id.tile23)},
                {findViewById(R.id.tile30), findViewById(R.id.tile31), findViewById(R.id.tile32), findViewById(R.id.tile33)}
        };
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
    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) {
            return false;
        }
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                canSwipe = false;
                if (diffX > 0) {
                    moveRight(WhatToDo.RIGHT);
                } else {
                    moveLeft(WhatToDo.LEFT);
                }
            }
        } else {
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                canSwipe = false;
                if (diffY > 0) {
                    moveDown(WhatToDo.DOWN);
                } else {
                    moveUp(WhatToDo.UP);
                }
            }
        }
        return true;
    }

    void addTile() {
        Vector<Integer> empty = new Vector<>();
        for (int i = 0; i < TILE_COUNT; i++) {
            empty.addElement(i);
        }
        Button t = null;
        for (int i = 0; i < TILES; i++) {
            for (int j = 0; j < TILES; j++) {
                if (tiles[i][j].getVisibility() == View.VISIBLE) {
                    empty.removeElement((int) (tiles[i][j].getTranslationY() / width) * TILES + (int) (tiles[i][j].getTranslationX() / width));
                } else {
                    t = tiles[i][j];
                }
            }
        }
        int index = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            index = LocalTime.now().getSecond() % empty.size();
        }
        int idx = empty.get(index), i = idx / TILES, j = idx % TILES;
        t.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c3)));
        t.setText("3");
        t.setTranslationX(j * width);
        t.setTranslationY(i * width);
        t.setVisibility(View.VISIBLE);
        add = false;
    }

    void moveRight(WhatToDo w) {
        reassign();
        for (int i = 0; i < TILES; i++) {
            for (int j = TILES_EVEN_FEWER; j >= 0; j--) {
                if (tilesOnBoard[i][j] != null) {
                    int h = j + 1;
                    while (h < TILES && tilesOnBoard[i][h] == null) {
                        h++;
                    }
                    h--;
                    if (tilesOnBoard[i][h] == null) {
                        add = true;
                        tilesOnBoard[i][h] = tilesOnBoard[i][j];
                        tilesOnBoard[i][j] = null;
                    }
                }
            }
        }
        moveTiles(w);
    }

    void moveDown(WhatToDo w) {
        reassign();
        for (int j = 0; j < TILES; j++) {
            for (int i = TILES_EVEN_FEWER; i >= 0; i--) {
                if (tilesOnBoard[i][j] != null) {
                    int h = i + 1;
                    while (h < TILES && tilesOnBoard[h][j] == null) {
                        h++;
                    }
                    h--;
                    if (tilesOnBoard[h][j] == null) {
                        add = true;
                        tilesOnBoard[h][j] = tilesOnBoard[i][j];
                        tilesOnBoard[i][j] = null;
                    }
                }
            }
        }
        moveTiles(w);
    }

    void moveLeft(WhatToDo w) {
        reassign();
        for (int i = 0; i < TILES; i++) {
            for (int j = 1; j < TILES; j++) {
                if (tilesOnBoard[i][j] != null) {
                    int h = j - 1;
                    while (h >= 0 && tilesOnBoard[i][h] == null) {
                        h--;
                    }
                    h++;
                    if (tilesOnBoard[i][h] == null) {
                        add = true;
                        tilesOnBoard[i][h] = tilesOnBoard[i][j];
                        tilesOnBoard[i][j] = null;
                    }
                }
            }
        }
        moveTiles(w);
    }

    void moveUp(WhatToDo w) {
        reassign();
        for (int j = 0; j < TILES; j++) {
            for (int i = 1; i < TILES; i++) {
                if (tilesOnBoard[i][j] != null) {
                    int h = i - 1;
                    while (h >= 0 && tilesOnBoard[h][j] == null) {
                        h--;
                    }
                    h++;
                    if (tilesOnBoard[h][j] == null) {
                        add = true;
                        tilesOnBoard[h][j] = tilesOnBoard[i][j];
                        tilesOnBoard[i][j] = null;
                    }
                }
            }
        }
        moveTiles(w);
    }

    void reassign() {
        makeNull();
        for (int i = 0; i < TILES; i++) {
            for (int j = 0; j < TILES; j++) {
                if (tiles[i][j].getVisibility() == View.VISIBLE) {
                    tilesOnBoard[(int) (tiles[i][j].getTranslationY() / width)][(int) (tiles[i][j].getTranslationX() / width)] = tiles[i][j];
                }
            }
        }
    }

    void makeNull() {
        for (int i = 0; i < TILES; i++) {
            for (int j = 0; j < TILES; j++) {
                tilesOnBoard[i][j] = null;
            }
        }
    }

    void handleW(WhatToDo w) {
        if (w == WhatToDo.RIGHT) {
            mergeRight();
        } else if (w == WhatToDo.DOWN) {
            mergeDown();
        } else if (w == WhatToDo.LEFT) {
            mergeLeft();
        } else if (w == WhatToDo.UP) {
            mergeUp();
        } else {
            if (add) {
                addTile();
            }
            canSwipe = true;
        }
    }

    void moveTiles(WhatToDo w) {
        AnimatorSet animatorSet = new AnimatorSet();
        Vector<Animator> anim = new Vector<>();
        for (int i = 0; i < TILES; i++) {
            for (int j = 0; j < TILES; j++) {
                if (tilesOnBoard[i][j] != null) {
                    int xd = (int) (Math.abs(tilesOnBoard[i][j].getTranslationX() - width * j) / width);
                    int yd = (int) (Math.abs(tilesOnBoard[i][j].getTranslationY() - width * i) / width);
                    if (xd > 0) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(tilesOnBoard[i][j], "translationX", width * j);
                        animator.setDuration(SPEED * xd);
                        anim.add(animator);
                    } else if (yd > 0) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(tilesOnBoard[i][j], "translationY", width * i);
                        animator.setDuration(SPEED * yd);
                        anim.add(animator);
                    }
                }
            }
        }
        if (!anim.isEmpty()) {
            animatorSet.playTogether(anim);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    for (int i = 0; i < TILES; i++) {
                        for (int j = 0; j < TILES; j++) {
                            if (tilesOnBoard[i][j] != null) {
                                tilesOnBoard[i][j].setTranslationX(width * j);
                                tilesOnBoard[i][j].setTranslationY(width * i);
                            }
                        }
                    }
                    handleW(w);
                }
            });
            animatorSet.start();
        } else {
            handleW(w);
        }
    }

    void updateColor(Button tile, CharSequence s) {
        add = true;
        if (s.equals("3")) {
            tile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c9)));
            tile.setText("9");
        } else if (s.equals("9")) {
            tile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c27)));
            tile.setText("27");
        } else if (s.equals("27")) {
            tile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c81)));
            tile.setText("81");
        } else if (s.equals("81")) {
            tile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c243)));
            tile.setText("243");
        } else if (s.equals("243")) {
            tile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c729)));
            tile.setText("729");
        } else if (s.equals("729")) {
            tile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c2187)));
            tile.setText("2187");
        } else if (s.equals("2187")) {
            tile.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c6561)));
            tile.setText("6561");
        }
    }

    void mergeRight() {
        reassign();
        for (int i = 0; i < TILES; i++) {
            for (int j = TILES_FEWER; j >= DECR2; j--) {
                if (tilesOnBoard[i][j] != null) {
                    Button t1 = tilesOnBoard[i][j - DECR1], t2 = tilesOnBoard[i][j - DECR2];
                    if (t1 != null && t2 != null) {
                        CharSequence s = tilesOnBoard[i][j].getText();
                        if (s.equals(t1.getText()) && s.equals(t2.getText())) {
                            t1.setVisibility(View.GONE);
                            t2.setVisibility(View.GONE);
                            updateColor(tilesOnBoard[i][j], s);
                            j -= DECR2;
                        }
                    }
                }
            }
        }
        moveRight(WhatToDo.FINISH);
    }

    void mergeDown() {
        reassign();
        for (int j = 0; j < TILES; j++) {
            for (int i = TILES_FEWER; i >= DECR2; i--) {
                if (tilesOnBoard[i][j] != null) {
                    Button t1 = tilesOnBoard[i - DECR1][j], t2 = tilesOnBoard[i - DECR2][j];
                    if (t1 != null && t2 != null) {
                        CharSequence s = tilesOnBoard[i][j].getText();
                        if (s.equals(t1.getText()) && s.equals(t2.getText())) {
                            t1.setVisibility(View.GONE);
                            t2.setVisibility(View.GONE);
                            updateColor(tilesOnBoard[i][j], s);
                            i -= DECR2;
                        }
                    }
                }
            }
        }
        moveDown(WhatToDo.FINISH);
    }

    void mergeLeft() {
        reassign();
        for (int i = 0; i < TILES; i++) {
            for (int j = 0; j < TILES_FEWER_MERGE; j++) {
                if (tilesOnBoard[i][j] != null) {
                    Button t1 = tilesOnBoard[i][j + DECR1], t2 = tilesOnBoard[i][j + DECR2];
                    if (t1 != null && t2 != null) {
                        CharSequence s = tilesOnBoard[i][j].getText();
                        if (s.equals(t1.getText()) && s.equals(t2.getText())) {
                            t1.setVisibility(View.GONE);
                            t2.setVisibility(View.GONE);
                            updateColor(tilesOnBoard[i][j], s);
                            j += DECR2;
                        }
                    }
                }
            }
        }
        moveLeft(WhatToDo.FINISH);
    }

    void mergeUp() {
        reassign();
        for (int j = 0; j < TILES; j++) {
            for (int i = 0; i < TILES_FEWER_MERGE; i++) {
                if (tilesOnBoard[i][j] != null) {
                    Button t1 = tilesOnBoard[i + DECR1][j], t2 = tilesOnBoard[i + DECR2][j];
                    if (t1 != null && t2 != null) {
                        CharSequence s = tilesOnBoard[i][j].getText();
                        if (s.equals(t1.getText()) && s.equals(t2.getText())) {
                            t1.setVisibility(View.GONE);
                            t2.setVisibility(View.GONE);
                            updateColor(tilesOnBoard[i][j], s);
                            i += DECR2;
                        }
                    }
                }
            }
        }
        moveUp(WhatToDo.FINISH);
    }
}
