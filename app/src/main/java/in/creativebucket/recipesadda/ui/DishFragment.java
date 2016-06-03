package in.creativebucket.recipesadda.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.ArrayList;

import in.creativebucket.recipesadda.R;
import in.creativebucket.recipesadda.model.AppConstants;
import in.creativebucket.recipesadda.model.Config;
import in.creativebucket.recipesadda.model.RecipeData;
import in.creativebucket.recipesadda.preferences.RecipesAddaStateMachine;

/**
 * Created by Chandan kumar on 11/1/2015.
 */
public class DishFragment extends Fragment implements AppConstants {

    public static ArrayList<RecipeData> dishDataArr;
    public int position;

    public static DishFragment newInstance(ArrayList<RecipeData> dishDataArr, int position) {
        DishFragment dishFragment = new DishFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        bundle.putParcelableArrayList(RECIPE_LIST, dishDataArr);
        dishFragment.setArguments(bundle);
        return dishFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(POSITION);
        dishDataArr = getArguments().getParcelableArrayList(RECIPE_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.viewpager_item, container, false);
        TextView mDishName = (TextView) rootView.findViewById(R.id.recipeName);
        ImageView mRecipeImage = (ImageView) rootView.findViewById(R.id.recipeImage);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ImageView fb_image = (ImageView) rootView.findViewById(R.id.fb_image);
        TextView mDishIngredientsTxt = (TextView) rootView.findViewById(R.id.ingredient_item);
        TextView mDishDescription = (TextView) rootView.findViewById(R.id.dish_description);

        TextView playVideoTxtVw = (TextView) rootView.findViewById(R.id.watch_video);

        playVideoTxtVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int videoMode = RecipesAddaStateMachine.getCurrentVideoMode(getActivity());
                if (videoMode == AppConstants.LANDSCAPE_MODE)
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                            Config.YOUTUBE_API_KEY, dishDataArr.get(position).getVideoId(), 0, true, false));
                else
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                            Config.YOUTUBE_API_KEY, dishDataArr.get(position).getVideoId(), 0, true, true));
            }
        });

        mDishName.setText(dishDataArr.get(position).getRecipeName());

        Glide.with(getActivity())
                .load(dishDataArr.get(position).getImageUrl())
                .placeholder(R.drawable.recipe_placeholder)
                .into(new GlideDrawableImageViewTarget(mRecipeImage) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        progressBar.setVisibility(View.GONE);
                    }
                });

        fb_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(getOpenFacebookIntent(getActivity()));
            }
        });

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "atlantis_medium.ttf");

        String ingredientsData = getFormattedData(dishDataArr.get(position).getIngredients());
        mDishIngredientsTxt.setText(Html.fromHtml(ingredientsData));
       // mDishIngredientsTxt.setTypeface(tf);


        String preparationData = getFormattedData(dishDataArr.get(position).getPreparation());
        mDishDescription.setText(Html.fromHtml(preparationData));
        //mDishDescription.setTypeface(tf);


        return rootView;
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/102364983458877"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/recipesadda"));
        }
    }

    public String getFormattedData(String strData) {
        String responseString = "";
        String[] strArr = strData.split("&&");
        for (int i = 0; i < strArr.length; i++) {
            if (i == 0)
                responseString = responseString + "<b>" + (i + 1) + ".</b>&#160;&#160;&#160;" + strArr[i] + "<br/><br/>";
            else
                responseString = responseString + "<b>" + (i + 1) + ".</b>&#160;&#160;" + strArr[i] + "<br/><br/>";

        }
        return responseString;
    }

}

