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

public class WishlistBooksActivity extends AppCompatActivity {

    public static final String WISHLIST_BOOKS_ACTIVITY_NAME = "wishlistBooks";
    private RecyclerView wishlistBooksRecyclerView;
    private BooksRecyclerViewAdapter adapter;
    private MaterialButton noBooksButton;
    private RelativeLayout noBooksLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_books);

        initViews();

        adapter = new BooksRecyclerViewAdapter(this, WISHLIST_BOOKS_ACTIVITY_NAME);
        adapter.setBooks(Utility.minimize(Utility.getInstance(this).getWishListBooks()));

        checkWishlistBooksStatus();

        wishlistBooksRecyclerView.setAdapter(adapter);
        wishlistBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        wishlistBooksRecyclerView = findViewById(R.id.wishlist_books_recycler_view);
        noBooksButton = findViewById(R.id.no_books_in_wishlist);
        noBooksLayout = findViewById(R.id.no_books_in_wishlist_container);
    }

    public void checkWishlistBooksStatus() {
        if (Utility.getInstance(this).getWishListBooks().isEmpty()) {
            noBooksLayout.setVisibility(View.VISIBLE);
            noBooksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WishlistBooksActivity.this, AllBooksActivity.class);
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
        checkWishlistBooksStatus();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
