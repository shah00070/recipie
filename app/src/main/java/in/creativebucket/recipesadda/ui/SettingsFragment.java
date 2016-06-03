package in.creativebucket.recipesadda.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import in.creativebucket.recipesadda.R;
import in.creativebucket.recipesadda.model.AppConstants;
import in.creativebucket.recipesadda.preferences.RecipesAddaStateMachine;
import in.creativebucket.recipesadda.service.HttpRequest;
import in.creativebucket.recipesadda.utils.Utility;

public class SettingsFragment extends Fragment implements AppConstants {


    private ProgressBar progressBar;
    private EditText feedbackMsgTxt;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_layout, container, false);
        feedbackMsgTxt = (EditText) rootView.findViewById(R.id.feedback_msg);
        TextView sendMsgTxtVw = (TextView) rootView.findViewById(R.id.submit_feedback);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ImageView fb_imageView = (ImageView) rootView.findViewById(R.id.fb_image);
        Switch videoModeSwitch = (Switch) rootView.findViewById(R.id.video_mode);

        sendMsgTxtVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserFeedback();
            }
        });
        int currentVideoMode = RecipesAddaStateMachine.getCurrentVideoMode(getActivity());
        if (currentVideoMode == AppConstants.LANDSCAPE_MODE) {
            videoModeSwitch.setChecked(false);
        } else {
            videoModeSwitch.setChecked(true);
        }
        videoModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isEnable) {
                if (isEnable) {
                    RecipesAddaStateMachine.setCurrentVideoMode(getActivity(), AppConstants.PORTRAIT_MODE);
                } else {
                    RecipesAddaStateMachine.setCurrentVideoMode(getActivity(), AppConstants.LANDSCAPE_MODE);
                }
            }
        });

        sendMsgTxtVw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendUserFeedback();
                }
                return false;
            }
        });

        fb_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    startActivity(getOpenFacebookIntent(getActivity()));
            }
        });

        return rootView;
    }

    public void sendUserFeedback() {
        String userEmail = new Utility(getActivity()).getUserEmailId();
        boolean isValidEmailId = isValidEmail(userEmail);

        if (feedbackMsgTxt.getText().toString().length() > 0 && isValidEmailId)
            new SendUserMsgAsyncTask().execute(feedbackMsgTxt.getText().toString(), userEmail);
        else
            Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_LONG).show();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/102364983458877"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/recipesadda"));
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    class SendUserMsgAsyncTask extends AsyncTask<String, Void, String> {

        public String feedbackMsg = "";
        public String userEmail = "";

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            feedbackMsg = args[0];
            userEmail = args[1];

            HashMap<String, String> params = new HashMap<>();
            params.put("user_feedback", feedbackMsg);
            params.put("user_email", userEmail);

            String response = "";
            try {
                response = new HttpRequest(SEND_FEEDBACK_URL).preparePost().withData(params).sendAndReadString();
            } catch (Exception e) {
                response = e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String responseCode) {
            super.onPostExecute(responseCode);
            progressBar.setVisibility(View.GONE);
            if (responseCode.equals("0")) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.success_msg), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.failure_msg), Toast.LENGTH_LONG).show();
            }
            feedbackMsgTxt.setText("");
        }
    }


}