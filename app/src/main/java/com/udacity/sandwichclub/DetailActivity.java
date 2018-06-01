package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    TextView mAlsoKnownAs;
    TextView mOrigin;
    TextView mDescription;
    TextView mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mAlsoKnownAs = findViewById(R.id.also_known_tv);
        mOrigin = findViewById(R.id.origin_tv);
        mDescription = findViewById(R.id.description_tv);
        mIngredients = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.placeholder)
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        List<String> alsoKnownList = sandwich.getAlsoKnownAs();
        String listAlsoKnown = "";
        for (String a : alsoKnownList) {
            listAlsoKnown += a + ", ";
        }
        //remove the last comma
        if (!listAlsoKnown.isEmpty()) {
            listAlsoKnown = listAlsoKnown.substring(0, listAlsoKnown.length() - 2);
        }
        mAlsoKnownAs.setText(dataMissing(listAlsoKnown));

        mOrigin.setText(dataMissing(sandwich.getPlaceOfOrigin()));

        mDescription.setText(dataMissing(sandwich.getDescription()));

        List<String> ingredientsList = sandwich.getIngredients();
        String listIngredients = "";
        for (String i : ingredientsList) {
            listIngredients += i + ", ";
        }
        //remove the last comma
        if (!listIngredients.isEmpty()) {
            listIngredients = listIngredients.substring(0, listIngredients.length() - 2);
        }
        mIngredients.setText(dataMissing(listIngredients));
    }

    //This method checks the missing data
    private String dataMissing(String s) {
        if (s.isEmpty()) {
            return getString(R.string.data_missing);
        } else {
            return s;
        }
    }
}
