package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String NAME = "name";
    private static final String MAIN_NAME = "mainName";
    private static final String ALSOKNOWNAS = "alsoKnownAs";
    private static final String PLACE_ORIGIN = "placeOfOrigin";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String INGREDIENTS = "ingredients";

    public static Sandwich parseSandwichJson(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject name = jsonObject.getJSONObject(NAME);

            String mainName = name.optString(MAIN_NAME);

            JSONArray alsoKnownAs = name.getJSONArray(ALSOKNOWNAS);
            ArrayList<String> alsoKnownAsList = new ArrayList<>();
            for (int i = 0; i < alsoKnownAs.length(); i++) {
                alsoKnownAsList.add(alsoKnownAs.getString(i));
            }

            String placeOfOrigin = jsonObject.optString(PLACE_ORIGIN);
            String description = jsonObject.optString(DESCRIPTION);
            String image = jsonObject.optString(IMAGE);

            JSONArray ingredients = jsonObject.getJSONArray(INGREDIENTS);
            ArrayList<String> ingredientsList = new ArrayList<>();
            for (int i = 0; i < ingredients.length(); i++) {
                ingredientsList.add(ingredients.getString(i));
            }

            return new Sandwich(mainName, alsoKnownAsList, placeOfOrigin, description, image, ingredientsList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
