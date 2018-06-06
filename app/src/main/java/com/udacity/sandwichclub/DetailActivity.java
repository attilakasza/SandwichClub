package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.also_known_tv) TextView mAlsoKnownAs;
    @BindView(R.id.origin_tv) TextView mOrigin;
    @BindView(R.id.description_tv) TextView mDescription;
    @BindView(R.id.ingredients_tv) TextView mIngredients;
    @BindView(R.id.image_iv) ImageView mImageIv;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
                .into(mImageIv);

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(
                0,
                R.animator.fade_out
        );
    }
}
