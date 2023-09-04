package com.example.booknexus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.booknexus.Models.Book;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import static com.example.booknexus.WebsiteActivity.URL_NAME_TAG;

public class BookActivity extends AppCompatActivity {

    private static final String TAG = "BookActivity";
    public static final String BOOK_ID_KEY = "bookId";
    public static final String BOOK_ID_NUMBER_KEY = "bookIdNumber";
    private ImageView bookImage;
    private TextView bookName, authorName, numberOfPages, longDescription, learnMore;
    private MaterialButton currentlyReadingBooksButton, addToFavouritesBooksButton, alreadyReadBooksButton, addToWishListBooksButton;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        initViews();

        Intent intent = getIntent();

        if (null != intent) {
            int bookId = intent.getIntExtra(BOOK_ID_KEY, -1);
            if (bookId != -1) {
                final Book selectedBook = Utility.getInstance(this).getBookById(bookId);
                if (null != selectedBook) {
                    book = selectedBook;

                    setBookViewData(selectedBook);

                    handleCurrentlyReadingBooksButton(selectedBook);

                    handleAddToFavouritesButton(selectedBook);

                    handleAlreadyReadBooksButton(selectedBook);

                    handleAddToWishListBooksButton(selectedBook);

                    learnMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent learnMoreIntent = new Intent(BookActivity.this, WebsiteActivity.class);
                            learnMoreIntent.putExtra(URL_NAME_TAG, selectedBook.getBookWebsiteUrl());
                            startActivity(learnMoreIntent);
                        }
                    });
                }
            }
        }
    }

    private void handleAddToWishListBooksButton(final Book currentBook) {
        ArrayList<Book> wishListBooks = Utility.getInstance(this).getWishListBooks();
        ArrayList<Book> alreadyReadBooks = Utility.getInstance(this).getAlreadyReadBooks();
        ArrayList<Book> currentlyReadingBooks = Utility.getInstance(this).getCurrentlyReadingBooks();

        boolean existsInWishListBooks = false;
        boolean existsInAlreadyReadBooks = false;
        boolean existsInCurrentlyReadingBooks = false;

        if (null != wishListBooks) {
            for (Book book : wishListBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInWishListBooks = true;
                }
            }
        }

        if (null != alreadyReadBooks) {
            for (Book book : alreadyReadBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInAlreadyReadBooks = true;
                }
            }
        }

        if (null != currentlyReadingBooks) {
            for (Book book : currentlyReadingBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInCurrentlyReadingBooks = true;
                }
            }
        }

        if (existsInWishListBooks) {
            addToWishListBooksButton.setIconResource(R.drawable.ic_check);
            addToWishListBooksButton.setClickable(false);
        } else {
            final boolean finalExistsInAlreadyReadBooks = existsInAlreadyReadBooks;
            final boolean finalExistsInCurrentlyReadingBooks = existsInCurrentlyReadingBooks;
            addToWishListBooksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utility.getInstance(BookActivity.this).addWishListBook(currentBook)) {
                        if (finalExistsInAlreadyReadBooks) {
                            alreadyReadBooksButton.setIcon(null);
                            alreadyReadBooksButton.setClickable(true);
                            if (Utility.getInstance(BookActivity.this).removeFromAlreadyReadBooks(currentBook)) {
                                Log.d(TAG, "onClick: removeFromAlreadyReadBooks successful");
                            } else {
                                Log.d(TAG, "onClick: removeFromAlreadyReadBooks failed");
                            }
                        }
                        if (finalExistsInCurrentlyReadingBooks) {
                            currentlyReadingBooksButton.setIcon(null);
                            currentlyReadingBooksButton.setClickable(true);
                            if (Utility.getInstance(BookActivity.this).removeFromCurrentlyReadingBooks(currentBook)) {
                                Log.d(TAG, "onClick: removeFromCurrentlyReadingBooks successful");
                            } else {
                                Log.d(TAG, "onClick: removeFromCurrentlyReadingBooks failed");
                            }
                        }
                        Toast.makeText(BookActivity.this, "Book added to wishlist. Hope you get it soon.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(BookActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleAlreadyReadBooksButton(final Book currentBook) {
        ArrayList<Book> alreadyReadBooks = Utility.getInstance(this).getAlreadyReadBooks();
        ArrayList<Book> currentlyReadingBooks = Utility.getInstance(this).getCurrentlyReadingBooks();
        ArrayList<Book> wishListBooks = Utility.getInstance(this).getWishListBooks();

        boolean existsInAlreadyReadBooks = false;
        boolean existsInCurrentlyReadingBooks = false;
        boolean existsInWishListBooks = false;

        if (null != alreadyReadBooks) {
            for (Book book : alreadyReadBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInAlreadyReadBooks = true;
                }
            }
        }

        if (null != currentlyReadingBooks) {
            for (Book book : currentlyReadingBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInCurrentlyReadingBooks = true;
                }
            }
        }

        if (null != wishListBooks) {
            for (Book book : wishListBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInWishListBooks = true;
                }
            }
        }

        if (existsInAlreadyReadBooks) {
            alreadyReadBooksButton.setIconResource(R.drawable.ic_check);
            alreadyReadBooksButton.setClickable(false);
        } else {
            final boolean finalExistsInCurrentlyReadingBooks = existsInCurrentlyReadingBooks;
            final boolean finalExistsInWishListBooks = existsInWishListBooks;
            alreadyReadBooksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utility.getInstance(BookActivity.this).addAlreadyReadBook(currentBook)) {
                        if (finalExistsInCurrentlyReadingBooks) {
                            currentlyReadingBooksButton.setIcon(null);
                            currentlyReadingBooksButton.setCheckable(true);
                            if (Utility.getInstance(BookActivity.this).removeFromCurrentlyReadingBooks(currentBook)) {
                                Log.d(TAG, "onClick: removeFromCurrentlyReadingBooks successful");
                            } else {
                                Log.d(TAG, "onClick: removeFromCurrentlyReadingBooks failed");
                            }
                        }
                        if (finalExistsInWishListBooks) {
                            addToWishListBooksButton.setIcon(null);
                            addToWishListBooksButton.setClickable(true);
                            if (Utility.getInstance(BookActivity.this).removeFromWishListBooks(currentBook)) {
                                Log.d(TAG, "onClick: removeFromWishListBooks successful");
                            } else {
                                Log.d(TAG, "onClick: removeFromWishListBooks failed");
                            }
                        }
                        Toast.makeText(BookActivity.this, "Congrats on reading this book.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(BookActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleAddToFavouritesButton(final Book currentBook) {
        ArrayList<Book> favouriteBooks = Utility.getInstance(this).getFavouriteBooks();

        boolean existsInFavouriteBooks = false;

        if (null != favouriteBooks) {
            for (Book book : favouriteBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInFavouriteBooks = true;
                }
            }
        }

        if (existsInFavouriteBooks) {
            addToFavouritesBooksButton.setIconResource(R.drawable.ic_check);
            addToFavouritesBooksButton.setClickable(false);
        } else {
            addToFavouritesBooksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utility.getInstance(BookActivity.this).addFavouriteBook(currentBook)) {
                        Toast.makeText(BookActivity.this, "Added to favourite books.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(BookActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleCurrentlyReadingBooksButton(final Book currentBook) {
        ArrayList<Book> currentlyReadingBooks = Utility.getInstance(this).getCurrentlyReadingBooks();
        ArrayList<Book> alreadyReadBooks = Utility.getInstance(this).getAlreadyReadBooks();
        ArrayList<Book> wishListBooks = Utility.getInstance(this).getWishListBooks();

        boolean existsInCurrentlyReadingBooks = false;
        boolean existsInAlreadyReadBooks = false;
        boolean existsInWishListBooks = false;

        if (null != currentlyReadingBooks) {
            for (Book book : currentlyReadingBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInCurrentlyReadingBooks = true;
                }
            }
        }

        if (null != alreadyReadBooks) {
            for (Book book : alreadyReadBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInAlreadyReadBooks = true;
                }
            }
        }

        if (null != wishListBooks) {
            for (Book book : wishListBooks) {
                if (book.getId() == currentBook.getId()) {
                    existsInWishListBooks = true;
                }
            }
        }

        if (existsInCurrentlyReadingBooks) {
            currentlyReadingBooksButton.setIconResource(R.drawable.ic_check);
            currentlyReadingBooksButton.setClickable(false);
        } else {
            final boolean finalExistsInAlreadyReadBooks = existsInAlreadyReadBooks;
            final boolean finalExistsInWishListBooks = existsInWishListBooks;
            currentlyReadingBooksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utility.getInstance(BookActivity.this).addCurrentlyReadingBook(currentBook)) {
                        if (finalExistsInAlreadyReadBooks) {
                            alreadyReadBooksButton.setIcon(null);
                            alreadyReadBooksButton.setCheckable(true);
                            if (Utility.getInstance(BookActivity.this).removeFromAlreadyReadBooks(currentBook)) {
                                Log.d(TAG, "onClick: removeFromAlreadyReadBooks successful");
                            } else {
                                Log.d(TAG, "onClick: removeFromAlreadyReadBooks failed");
                            }
                        }

                        if (finalExistsInWishListBooks) {
                            addToWishListBooksButton.setIcon(null);
                            addToWishListBooksButton.setClickable(true);
                            if (Utility.getInstance(BookActivity.this).removeFromWishListBooks(currentBook)) {
                                Log.d(TAG, "onClick: removeFromWishListBooks successful");
                            } else {
                                Log.d(TAG, "onClick: removeFromWishListBooks failed");
                            }
                        }

                        Toast.makeText(BookActivity.this, "You are currently reading this book.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(BookActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void initViews() {
        bookImage = findViewById(R.id.book_image_book_layout);

        bookName = findViewById(R.id.book_name_book_layout);
        authorName = findViewById(R.id.author_book_layout);
        numberOfPages = findViewById(R.id.number_of_pages_book_layout);
        longDescription = findViewById(R.id.long_description_book_layout);

        currentlyReadingBooksButton = findViewById(R.id.currently_reading_books_button_book_layout);
        addToFavouritesBooksButton = findViewById(R.id.favourites_books_button_book_layout);
        alreadyReadBooksButton = findViewById(R.id.already_read_books_button_book_layout);
        addToWishListBooksButton = findViewById(R.id.wish_list_books_button_book_layout);

        learnMore = findViewById(R.id.learn_more_book_layout);
    }

    private void setBookViewData(Book book) {
        bookName.setText(book.getName());
        authorName.setText(book.getAuthor());
        numberOfPages.setText(String.valueOf(book.getNumberOdPages()));
        longDescription.setText(book.getLongDescription());

        Glide.with(this)
                .asBitmap()
                .load(Uri.parse(book.getImageUrl()))
                .into(bookImage);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
        builder.setTitle("Remove book");
        builder.setMessage("Do you really want to delete this book?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Utility.getInstance(BookActivity.this).removeFromAllBooks(book)) {
                    Toast.makeText(BookActivity.this, "Book deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookActivity.this, AllBooksActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(BookActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_warning);
        builder.create().show();
    }

    private void editBook() {
        Intent intent = new Intent(this, EditBookActivity.class);
        intent.putExtra(BOOK_ID_KEY, book.getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_remove, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_remove_book) {
            showAlertDialog();
            return true;
        }
        if (item.getItemId() == R.id.action_edit_book) {
            editBook();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, AllBooksActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backIntent);
    }
}
