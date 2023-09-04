package com.example.booknexus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import androidx.annotation.NonNull;

import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

import com.example.booknexus.Adapters.BooksRecyclerViewAdapter;

public class AllBooksActivity extends AppCompatActivity {

    private RecyclerView allBooksRecyclerView;
    private BooksRecyclerViewAdapter adapter;
    public static final String ALL_BOOKS_ACTIVITY_NAME = "allBooks";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);


        initViews();


        adapter = new BooksRecyclerViewAdapter(AllBooksActivity.this, ALL_BOOKS_ACTIVITY_NAME);
        adapter.setBooks(Utility.minimize(Utility.getInstance(this).getAllBooks()));

        allBooksRecyclerView.setAdapter(adapter);
        allBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void initViews() {
        allBooksRecyclerView = this.findViewById(R.id.all_books_recycler_view);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_new_book) {
            Intent intent = new Intent(AllBooksActivity.this, AddNewBookActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}