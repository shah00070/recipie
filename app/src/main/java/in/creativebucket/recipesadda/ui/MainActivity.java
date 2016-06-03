package in.creativebucket.recipesadda.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import in.creativebucket.recipesadda.R;
import in.creativebucket.recipesadda.adapter.FragmentDrawer;
import in.creativebucket.recipesadda.model.AppConstants;
import in.creativebucket.recipesadda.preferences.RecipesAddaStateMachine;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener, AppConstants {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

        boolean isFirstLaunch = RecipesAddaStateMachine.isFirstLaunch(getApplicationContext());
        if (isFirstLaunch) {
            RecipesAddaStateMachine.setIsFirstLaunch(getApplicationContext(), false);
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment containerFragment = getSupportFragmentManager().findFragmentById(R.id.container_body);
        if (containerFragment != null && containerFragment instanceof SettingsFragment) {
            displayView(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        if (id == R.id.action_search) {
//            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(final int position) {

        mDrawerLayout.closeDrawer(GravityCompat.START);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                String title = getString(R.string.app_name);
                String[] titleArray = getResources().getStringArray(R.array.nav_drawer_labels);
                switch (position) {
                    case 0:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", PUNJABI_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[0];
                        break;
                    case 1:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", BENGALI_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[1];
                        break;
                    case 2:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", GUJRATI_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[2];
                        break;
                    case 3:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", RAJASTHANI_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[3];
                        break;

                    case 4:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", BIHARI_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[4];
                        break;

                    case 5:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", HIMACHAL_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[5];
                        break;

                    case 6:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", JAMMU_KASHMIR_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[6];
                        break;

                    case 7:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", HARYANA_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[7];
                        break;

                    case 8:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", GOA_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[8];
                        break;

                    case 9:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", CHATTISHGARH_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[9];
                        break;

                    case 10:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", ANDHRA_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[10];
                        break;

                    case 11:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", ARUNACHAL_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[11];
                        break;

                    case 12:
                        fragment = new RecipesFragment();
                        bundle.putString("recipes_url", ASSAM_RECIPES_URL);
                        fragment.setArguments(bundle);
                        title = titleArray[12];
                        break;

                    case 13:
                        fragment = new UserRecipeFragment();
                        title = titleArray[13];
                        break;

                    case 14:
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(APP_URL));
                        startActivity(intent);
                        break;

                    case 15:
                        fragment = new SettingsFragment();
                        title = titleArray[15];
                        break;
                    default:
                        fragment = new DefaultFragment();
                        title = "Default";
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();
                    // set the toolbar title
                    getSupportActionBar().setTitle(title);
                }
            }
        }, 200);

    }


}
