package com.example.dailyquotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteQuotesAdapter extends RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder> {

    private List<Quote> favoriteQuotes;
    private Context context;
    private DatabaseHelper databaseHelper;

    public FavoriteQuotesAdapter(Context context, List<Quote> favoriteQuotes) {
        this.context = context;
        this.favoriteQuotes = favoriteQuotes;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_quotes_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quote quote = favoriteQuotes.get(position);
        holder.quoteTextView.setText(quote.getQuote());
        holder.authorTextView.setText(quote.getAuthor());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Quote currentQuote = favoriteQuotes.get(currentPosition);
                    databaseHelper.remove_favorite_quotes(currentQuote.getQuoteId());
                    favoriteQuotes.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, favoriteQuotes.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteQuotes.size();
    }

    public void updateFavoriteQuotes(List<Quote> newFavoriteQuotes) {
        this.favoriteQuotes = newFavoriteQuotes;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView quoteTextView, authorTextView;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quoteTextView = itemView.findViewById(R.id.quoteTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
