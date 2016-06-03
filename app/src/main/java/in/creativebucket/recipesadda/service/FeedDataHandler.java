package in.creativebucket.recipesadda.service;

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.creativebucket.recipesadda.AppController;
import in.creativebucket.recipesadda.model.AppConstants;
import in.creativebucket.recipesadda.model.RecipeData;
import in.creativebucket.recipesadda.utils.LogUtils;

/**
 * Created by Chandan Kumar on 9/13/2015.
 */
public class FeedDataHandler implements AppConstants {
    private String url;
    private ResultReceiver mResultreceiver;
    private Context mContext;
    // Tag used to cancel the request
    private String tag_json_obj = "json_obj_req";
    public String dishCategoryName = "";
    public ArrayList<RecipeData> recipeDataArrayList = new ArrayList<RecipeData>();


    private String TAG = LogUtils.makeLogTag(FeedDataHandler.class);

    public FeedDataHandler(ResultReceiver resultreceiver, Context ctx, String url) {
        this.mContext = ctx;
        this.url = url;
        this.mResultreceiver = resultreceiver;
    }

    public void downloadFeedData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    dishCategoryName = jsonObject.getString(DISH_CATEGORY_NAME);
                    JSONArray jsonArray = jsonObject.getJSONArray(DISH_LIST);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        // For coming version of app
                        // RecipeData recipeData = new RecipeData(object.getString(RECIPE_ID), object.getString(RECIPE_NAME), object.getString(RECIPE_IMAGE_URL), object.getString(RECIPE_VIDEO_URL), object.getString(RECIPE_INGREDIENTS), object.getString(RECIPE_PREPARATION));

                        RecipeData recipeData = new RecipeData(object.getString(RECIPE_NAME), object.getString(RECIPE_IMAGE_URL), object.getString(RECIPE_INGREDIENTS), object.getString(RECIPE_PREPARATION));
                        recipeDataArrayList.add(recipeData);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                } finally {
                    mResultreceiver.send(1, Bundle.EMPTY);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        mResultreceiver.send(1, Bundle.EMPTY);
                    }
                }
        );

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
