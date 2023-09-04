package com.example.booknexus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.booknexus.Adapters.BooksRecyclerViewAdapter;
import com.google.android.material.button.MaterialButton;

public class FavouriteBooksActivity extends AppCompatActivity {

    public static final String FAVOURITE_BOOKS_ACTIVITY_NAME = "favouriteBooks";
    private RecyclerView favouriteBooksRecyclerView;
    private BooksRecyclerViewAdapter adapter;
    private MaterialButton noBooksButton;
    private RelativeLayout noBooksLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_books);

        initViews();

        adapter = new BooksRecyclerViewAdapter(this, FAVOURITE_BOOKS_ACTIVITY_NAME);
        adapter.setBooks(Utility.minimize(Utility.getInstance(this).getFavouriteBooks()));

        checkFavouriteBooksStatus();

        favouriteBooksRecyclerView.setAdapter(adapter);
        favouriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void checkFavouriteBooksStatus() {
        if (Utility.getInstance(this).getFavouriteBooks().isEmpty()) {
            noBooksLayout.setVisibility(View.VISIBLE);
            noBooksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FavouriteBooksActivity.this, AllBooksActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            noBooksLayout.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        favouriteBooksRecyclerView = findViewById(R.id.favourite_books_recycler_view);
        noBooksButton = findViewById(R.id.no_books_in_favourites);
        noBooksLayout = findViewById(R.id.no_books_in_favourites_container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFavouriteBooksStatus();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
