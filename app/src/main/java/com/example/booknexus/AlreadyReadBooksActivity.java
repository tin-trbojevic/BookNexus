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

public class AlreadyReadBooksActivity extends AppCompatActivity {

    public static final String ALREADY_READ_BOOKS_ACTIVITY_NAME = "alreadyReadBooks";
    private RecyclerView alreadyReadBooksRecyclerView;
    private BooksRecyclerViewAdapter adapter;
    private MaterialButton noBooksButton;
    private RelativeLayout noBooksLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_read_books);

        initViews();

        adapter = new BooksRecyclerViewAdapter(AlreadyReadBooksActivity.this, ALREADY_READ_BOOKS_ACTIVITY_NAME);
        adapter.setBooks(Utility.minimize(Utility.getInstance(this).getAlreadyReadBooks()));

        checkAlreadyBooksStatus();

        alreadyReadBooksRecyclerView.setAdapter(adapter);
        alreadyReadBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void checkAlreadyBooksStatus() {
        if (Utility.getInstance(this).getAlreadyReadBooks().isEmpty()) {
            noBooksLayout.setVisibility(View.VISIBLE);
            noBooksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AlreadyReadBooksActivity.this, AllBooksActivity.class);
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
        checkAlreadyBooksStatus();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initViews() {

        alreadyReadBooksRecyclerView = findViewById(R.id.already_read_books_recycler_view);
        noBooksButton = findViewById(R.id.no_books_in_already_read);
        noBooksLayout = findViewById(R.id.no_books_in_already_read_container);
    }
}
