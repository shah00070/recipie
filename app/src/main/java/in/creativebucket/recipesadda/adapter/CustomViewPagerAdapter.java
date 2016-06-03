package in.creativebucket.recipesadda.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import in.creativebucket.recipesadda.model.RecipeData;
import in.creativebucket.recipesadda.ui.DishFragment;
import in.creativebucket.recipesadda.ui.UserRecipeFragment;

public class CustomViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<RecipeData> dishDataArr;

    public CustomViewPagerAdapter(FragmentManager fm, ArrayList<RecipeData> dishArray) {
        super(fm);
        this.dishDataArr = dishArray;
    }

    @Override
    public Fragment getItem(int i) {
        return DishFragment.newInstance(dishDataArr, i);
    }

    public int getCount() {
        return dishDataArr.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
