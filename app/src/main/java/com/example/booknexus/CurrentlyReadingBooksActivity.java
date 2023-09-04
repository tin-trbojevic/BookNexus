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

public class CurrentlyReadingBooksActivity extends AppCompatActivity {

    public static final String CURRENTLY_READING_BOOKS_ACTIVITY_NAME = "currentlyReadingBooks";
    private RecyclerView currentlyReadingBooksRecyclerView;
    private BooksRecyclerViewAdapter adapter;
    private MaterialButton noBooksButton;
    private RelativeLayout noBooksLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currently_reading_books);

        initViews();

        adapter = new BooksRecyclerViewAdapter(CurrentlyReadingBooksActivity.this, CURRENTLY_READING_BOOKS_ACTIVITY_NAME);
        adapter.setBooks(Utility.minimize(Utility.getInstance(this).getCurrentlyReadingBooks()));

        checkCurrentlyReadingStatus();

        currentlyReadingBooksRecyclerView.setAdapter(adapter);
        currentlyReadingBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        currentlyReadingBooksRecyclerView = findViewById(R.id.currently_reading_books_recycler_view);
        noBooksButton = findViewById(R.id.no_books_in_currently_reading);
        noBooksLayout = findViewById(R.id.no_books_in_currently_reading_container);
    }

    public void checkCurrentlyReadingStatus() {
        if (Utility.getInstance(this).getCurrentlyReadingBooks().isEmpty()) {
            noBooksLayout.setVisibility(View.VISIBLE);
            noBooksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CurrentlyReadingBooksActivity.this, AllBooksActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            noBooksLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCurrentlyReadingStatus();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
