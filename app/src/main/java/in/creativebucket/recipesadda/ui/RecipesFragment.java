package in.creativebucket.recipesadda.ui;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import in.creativebucket.recipesadda.R;
import in.creativebucket.recipesadda.service.DetachableResultReceiver;
import in.creativebucket.recipesadda.service.FeedDataHandler;
import in.creativebucket.recipesadda.adapter.CustomViewPagerAdapter;
import in.creativebucket.recipesadda.model.AppConstants;
import in.creativebucket.recipesadda.model.RecipeData;

public class RecipesFragment extends Fragment implements DetachableResultReceiver.Receiver, AppConstants {

    private ArrayList<RecipeData> recipeArrayList;
    private DetachableResultReceiver mReceiver;
    private FeedDataHandler feedDataHandler;
    private ViewPager viewPager;
    private ProgressBar progressBar;
    private String recipeUrl;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getArguments() != null)
            recipeUrl = getArguments().getString("recipes_url");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new DetachableResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        feedDataHandler = new FeedDataHandler(mReceiver, getActivity(), recipeUrl);
        if (isNetworkAvailable())
            feedDataHandler.downloadFeedData();
        else {
            if (getActivity() != null)
                Toast.makeText(getActivity(), getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dish_layout, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.myViewpager);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        initUI();
        return rootView;
    }

    public void initUI() {

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition / 2 + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);
                // page.setRotationY(position * -30);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onReceiveResult(int i, Bundle bundle) {
        if (i == 1) {
            recipeArrayList = new ArrayList<>();
            progressBar.setVisibility(View.GONE);
            recipeArrayList.addAll(feedDataHandler.recipeDataArrayList);

            if (recipeArrayList.size() > 0) {
                CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getFragmentManager(), recipeArrayList);
                viewPager.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                if (getActivity() != null)
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet_slow_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}