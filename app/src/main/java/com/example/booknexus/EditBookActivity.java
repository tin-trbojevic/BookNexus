package com.example.booknexus;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.booknexus.Models.Book;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static com.example.booknexus.BookActivity.BOOK_ID_KEY;

public class EditBookActivity extends AppCompatActivity {

    private static final int PICK_PHOTO_FROM_GALLERY = 101;
    private static final int REQUEST_STORAGE_PERMISSION_SETTINGS = 102;
    private int STORAGE_PERMISSION_CODE = 103;
    private TextInputEditText bookName, authorName, numberOfPages, bookImageUrl, bookUrl, shortDesc, longDesc;
    private MaterialButton saveBookButton, cancelAddButton;
    private RelativeLayout parentLayout;
    private ImageView pickImageFromGallery;
    private boolean areEntriesCorrect;
    private Uri profileImageUri = Uri.EMPTY;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        initViews();

        Intent intent = getIntent();
        if (null != intent) {
            bookId = intent.getIntExtra(BOOK_ID_KEY, -1);

            if (bookId == -1) {
                Toast.makeText(this, "Something go wrong. Please try again.", Toast.LENGTH_SHORT).show();
                //Navigate user back if bookId is invalid
                super.onBackPressed();
            }
        }

        setViewsInitData();

        cancelAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelAlertDialog();
            }
        });

        pickImageFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStoragePermissionRequest();
            }
        });

        saveBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areEntriesCorrect = true;
                if (bookName.getText().toString().isEmpty()) {
                    bookName.setError("Enter book name");
                    areEntriesCorrect = false;
                }
                if (authorName.getText().toString().isEmpty()) {
                    authorName.setError("Enter author name");
                    areEntriesCorrect = false;
                }
                if (numberOfPages.getText().toString().isEmpty()) {
                    numberOfPages.setError("Enter number of pages");
                    areEntriesCorrect = false;
                }
                if (bookImageUrl.getText().toString().isEmpty()) {
                    bookImageUrl.setError("Enter image URL");
                    areEntriesCorrect = false;
                }
                if (bookUrl.getText().toString().isEmpty()) {
                    bookUrl.setError("Enter book URL");
                    areEntriesCorrect = false;
                }

                if (areEntriesCorrect) {
                    String bookNameInput = bookName.getText().toString();
                    String authorNameInput = authorName.getText().toString();
                    int numberOfPagesInput = Integer.parseInt(numberOfPages.getText().toString());
                    String bookImageUrlInput = bookImageUrl.getText().toString();
                    String bookUrlInput = bookUrl.getText().toString();
                    String shortDescInput = shortDesc.getText().toString();
                    String longDescInput = longDesc.getText().toString();

                    Book book = new Book(bookId, bookNameInput, authorNameInput, numberOfPagesInput, bookImageUrlInput, shortDescInput, longDescInput, bookUrlInput);

                    editBook(book);
                }
            }
        });

        removeWarnings();
    }

    private void initViews() {
        bookName = findViewById(R.id.edit_book_name);
        authorName = findViewById(R.id.edit_book_author_name);
        numberOfPages = findViewById(R.id.edit_book_pages);
        bookImageUrl = findViewById(R.id.edit_book_image_url);
        bookUrl = findViewById(R.id.edit_book_url);
        shortDesc = findViewById(R.id.edit_book_short_desc);
        longDesc = findViewById(R.id.edit_book_long_desc);

        saveBookButton = findViewById(R.id.button_save_edit_book);
        cancelAddButton = findViewById(R.id.button_cancel_edit_book);

        parentLayout = findViewById(R.id.edit_book_parent_relative_layout);

        pickImageFromGallery = findViewById(R.id.edit_book_pick_image_from_gallery_button);
    }


    private void handleStoragePermissionRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showStorageRationale();
            } else {
                askStoragePermission();
            }
        }
    }

    private void handleStoragePermissionRequestFromSettings() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted. You can pick book image now.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Permission not granted in settings", Toast.LENGTH_LONG).show();
        }
    }

    private void showStorageRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Storage permission needed");
        builder.setMessage("Storage permission is needed to choose book cover image from phone gallery");
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                askStoragePermission();
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void showStorageRationaleNeverAskAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("Permission denied");
        builder.setMessage("You denied storage permission and checked option to never ask again. If you wan't to grant permission go to application settings and grant permission manually.");
        builder.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent settingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                settingIntent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivityForResult(settingIntent, REQUEST_STORAGE_PERMISSION_SETTINGS);
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void askStoragePermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE
        );
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_PHOTO_FROM_GALLERY);
    }


    private void setViewsInitData() {
        Book book = Utility.getInstance(this).getBookById(bookId);
        bookName.setText(book.getName());
        authorName.setText(book.getAuthor());
        numberOfPages.setText(String.valueOf(book.getNumberOdPages()));
        bookImageUrl.setText(book.getImageUrl());
        bookUrl.setText(book.getBookWebsiteUrl());
        shortDesc.setText(book.getShortDescription());
        longDesc.setText(book.getLongDescription());
    }

    private void editBook(Book book) {
        if (Utility.getInstance(EditBookActivity.this).editBook(book)) {
            Toast.makeText(EditBookActivity.this, "Book edited successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditBookActivity.this, BookActivity.class);
            intent.putExtra(BOOK_ID_KEY, bookId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            showSnackbar(book);
        }
    }

    private void showSnackbar(final Book book) {
        Snackbar snackbar = Snackbar.make(parentLayout, "Something went wrong. Please try again.", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editBook(book);
                    }
                })
                .setAnchorView(R.id.button_add_new_book);
        snackbar.show();
    }

    private void removeWarnings() {

        bookName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                bookName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        authorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                authorName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        numberOfPages.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                numberOfPages.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bookImageUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                bookImageUrl.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bookUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                bookUrl.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void showCancelAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditBookActivity.this);
        builder.setTitle("Quit editor");
        builder.setMessage("Do you want to cancel editing this book?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditBookActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        showCancelAlertDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == AddNewBookActivity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(EditBookActivity.this, "Image not selected", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = EditBookActivity.this.getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                if (inputStream == null) {
                    Toast.makeText(EditBookActivity.this, "Error selecting image", Toast.LENGTH_SHORT).show();
                    return;
                }

                profileImageUri = data.getData();

                bookImageUrl.setText(profileImageUri.toString());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_STORAGE_PERMISSION_SETTINGS) {
            handleStoragePermissionRequestFromSettings();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0) {
            String permission = permissions[0];
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                if (!showRationale) {
                    showStorageRationaleNeverAskAgain();
                } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                openGallery();
            }
        }
    }
}