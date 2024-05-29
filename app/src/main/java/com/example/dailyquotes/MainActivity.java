package com.example.dailyquotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView quoteTextView;
    private Button randomQuoteButton;
    private ImageButton likeQuotes, shareQuotes, quotesSetting;
    private ProgressBar progressCircular;
    private String quoteWithAuthor, id, quote, author = null;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = findViewById(R.id.quotesTextView);
        randomQuoteButton = findViewById(R.id.randomQuoteButton);
        progressCircular = findViewById(R.id.progressCircular);
        likeQuotes = findViewById(R.id.likeQuotes);
        shareQuotes = findViewById(R.id.shareQuotes);
        quotesSetting = findViewById(R.id.quotesSetting);

        databaseHelper = new DatabaseHelper(this);

        randomQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressCircular.setVisibility(View.VISIBLE);
                new FetchQuoteTask().execute("https://random-quotes-freeapi.vercel.app/api/random");
            }
        });

        likeQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id != null && quote != null && author != null) {
                    if (databaseHelper.is_favorite_quote(id)) {
                        databaseHelper.remove_favorite_quotes(id);
                        likeQuotes.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.baseline_favorite_border_24));
                    } else {
                        databaseHelper.add_favorite_quotes(id, quote, author);
                        likeQuotes.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.baseline_favorite_24));
                    }
                }
            }
        });

        shareQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quoteWithAuthor == null) {
                    Log.d("GetRandom", "Get random quote");
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, quoteWithAuthor);
                    startActivity(Intent.createChooser(intent, "Share via"));
                }
            }
        });

        quotesSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
            }
        });
    }

    private class FetchQuoteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                response = stringBuilder.toString();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                id = jsonObject.getString("id");
                quote = jsonObject.getString("quote");
                author = jsonObject.getString("author");
                quoteWithAuthor = quote + "\n - " + author;

                if (id != null && quote != null) {
                    int drawableId = databaseHelper.is_favorite_quote(id) ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24;
                    likeQuotes.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, drawableId));
                }
                quoteTextView.setText(quoteWithAuthor);
                progressCircular.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
                progressCircular.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load quote", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
