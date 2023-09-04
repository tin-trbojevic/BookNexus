package com.example.booknexus.Adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.example.booknexus.AlreadyReadBooksActivity;
import com.example.booknexus.BookActivity;
import com.example.booknexus.CurrentlyReadingBooksActivity;
import com.example.booknexus.FavouriteBooksActivity;
import com.example.booknexus.Models.Book;
import com.example.booknexus.R;
import com.example.booknexus.Utility;
import com.example.booknexus.WishlistBooksActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import static com.example.booknexus.AllBooksActivity.ALL_BOOKS_ACTIVITY_NAME;
import static com.example.booknexus.AlreadyReadBooksActivity.ALREADY_READ_BOOKS_ACTIVITY_NAME;
import static com.example.booknexus.BookActivity.BOOK_ID_KEY;
import static com.example.booknexus.CurrentlyReadingBooksActivity.CURRENTLY_READING_BOOKS_ACTIVITY_NAME;
import static com.example.booknexus.FavouriteBooksActivity.FAVOURITE_BOOKS_ACTIVITY_NAME;
import static com.example.booknexus.WishlistBooksActivity.WISHLIST_BOOKS_ACTIVITY_NAME;

public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<BooksRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Book> books = new ArrayList<>();
    private Context mContext;
    private static final String TAG = "BooksRecyclerViewAda";
    private String parentActivity;

    public BooksRecyclerViewAdapter(Context mContext, String parentActivity) {
        this.mContext = mContext;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.books_recycler_view_layout_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        final Book bookItem = books.get(position);

        holder.bookNameList.setText(bookItem.getName());
        holder.bookAuthorList.setText(bookItem.getAuthor());
        String pages = "Pages: " + bookItem.getNumberOdPages();
        holder.bookPagesList.setText(pages);

        Glide.with(mContext)
                .asBitmap()
                .load(Uri.parse(bookItem.getImageUrl()))
                .into(holder.bookImageList);

        holder.parentCardViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BookActivity.class);
                intent.putExtra(BOOK_ID_KEY, bookItem.getId());
                mContext.startActivity(intent);
            }
        });

        holder.shortBookDescription.setText(bookItem.getShortDescription());

        if (bookItem.isExpended()) {
            TransitionManager.beginDelayedTransition(holder.parentCardViewList);
            holder.downArrow.setVisibility(View.GONE);
            holder.expendedBooksContainer.setVisibility(View.VISIBLE);

            if (parentActivity.equals(ALL_BOOKS_ACTIVITY_NAME)) {
                holder.removeFromList.setVisibility(View.GONE);
            } else if (parentActivity.equals(ALREADY_READ_BOOKS_ACTIVITY_NAME)) {
                holder.removeFromList.setVisibility(View.VISIBLE);
                holder.removeFromList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setMessage("Are you sure you want to remove \"" + bookItem.getName() + "\" from already read books?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Utility.getInstance(mContext).removeFromAlreadyReadBooks(bookItem)) {
                                    Toast.makeText(mContext, "Removed from already read books.", Toast.LENGTH_SHORT).show();
                                    //notifyDataSetChanged();
                                    Intent intent = new Intent(mContext, AlreadyReadBooksActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.create().show();
                    }
                });

            } else if (parentActivity.equals(CURRENTLY_READING_BOOKS_ACTIVITY_NAME)) {

                holder.removeFromList.setVisibility(View.VISIBLE);
                holder.removeFromList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setMessage("Are you sure you want to remove \"" + bookItem.getName() + "\" from currently reading books?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Utility.getInstance(mContext).removeFromCurrentlyReadingBooks(bookItem)) {
                                    Toast.makeText(mContext, "Removed from currently reading books.", Toast.LENGTH_SHORT).show();
                                    //notifyDataSetChanged();
                                    Intent intent = new Intent(mContext, CurrentlyReadingBooksActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.create().show();
                    }
                });

            } else if (parentActivity.equals(FAVOURITE_BOOKS_ACTIVITY_NAME)) {

                holder.removeFromList.setVisibility(View.VISIBLE);
                holder.removeFromList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setMessage("Are you sure you want to remove \"" + bookItem.getName() + "\" from your favourite books?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Utility.getInstance(mContext).removeFromFavouriteBooks(bookItem)) {
                                    Toast.makeText(mContext, "Removed from favourite books.", Toast.LENGTH_SHORT).show();
                                    //notifyDataSetChanged();
                                    Intent intent = new Intent(mContext, FavouriteBooksActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.create().show();
                    }
                });

            } else if (parentActivity.equals(WISHLIST_BOOKS_ACTIVITY_NAME)) {

                holder.removeFromList.setVisibility(View.VISIBLE);
                holder.removeFromList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setMessage("Are you sure you want to remove \"" + bookItem.getName() + "\" from your wishlist?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Utility.getInstance(mContext).removeFromWishListBooks(bookItem)) {
                                    Toast.makeText(mContext, "Removed from wishlist.", Toast.LENGTH_SHORT).show();
                                    //notifyDataSetChanged();
                                    Intent intent = new Intent(mContext, WishlistBooksActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.create().show();
                    }
                });

            }

        } else {
            TransitionManager.beginDelayedTransition(holder.parentCardViewList);
            holder.downArrow.setVisibility(View.VISIBLE);
            holder.expendedBooksContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView parentCardViewList;
        private ImageView bookImageList;
        private TextView bookNameList, bookPagesList, bookAuthorList, shortBookDescription;
        private ImageView downArrow, upArrow;
        private LinearLayout collapsedBooksContainer;
        private RelativeLayout expendedBooksContainer;
        private TextView removeFromList;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            parentCardViewList = itemView.findViewById(R.id.parent_card_view_all_books_layout_list);
            bookAuthorList = itemView.findViewById(R.id.book_author_all_books_layout_list);
            bookImageList = itemView.findViewById(R.id.book_image_all_books_layout_list);
            bookNameList = itemView.findViewById(R.id.book_name_all_books_layout_list);
            bookPagesList = itemView.findViewById(R.id.book_pages_all_books_layout_list);

            downArrow = itemView.findViewById(R.id.down_arrow);
            upArrow = itemView.findViewById(R.id.up_arrow);

            collapsedBooksContainer = itemView.findViewById(R.id.collapsed_books_container);
            expendedBooksContainer = itemView.findViewById(R.id.expanded_books_container);

            shortBookDescription = itemView.findViewById(R.id.short_book_description);

            removeFromList = itemView.findViewById(R.id.remove_from_list);

            downArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Book book = books.get(getAdapterPosition());
                    book.setExpended(!book.isExpended());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            upArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Book book = books.get(getAdapterPosition());
                    book.setExpended(!book.isExpended());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}

