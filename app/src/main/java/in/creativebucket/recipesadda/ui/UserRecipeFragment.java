package in.creativebucket.recipesadda.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;

import in.creativebucket.recipesadda.R;
import in.creativebucket.recipesadda.model.AppConstants;
import in.creativebucket.recipesadda.utils.Utility;

/**
 * Created by Chandan kumar on 11/1/2015.
 */
public class UserRecipeFragment extends Fragment implements AppConstants {

    private static int RESULT_LOAD_IMG = 1;
    private ProgressDialog prgDialog;
    private String encodedString;
    private RequestParams params = new RequestParams();
    private String imgPath, fileName;
    private Bitmap bitmap;
    private EditText recipeName, recipeVideoUrl, recipeIngredientsEdtTxt, recipeProcedureEdtTxt, recipeStateName, userNameEdtTxt, mobileNumberEdtTxt;
    private RadioGroup recipeType;
    private View rootView;
    private Uri mCapturedImageURI;
    private TextView uploadImageTxtVw;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prgDialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_user_recipe_layout, container, false);

        recipeName = (EditText) rootView.findViewById(R.id.recipeName);
        recipeType = (RadioGroup) rootView.findViewById(R.id.recipeType);
        uploadImageTxtVw = (TextView) rootView.findViewById(R.id.uploadRecipeImage);
        uploadImageTxtVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        recipeVideoUrl = (EditText) rootView.findViewById(R.id.video_url);
        recipeIngredientsEdtTxt = (EditText) rootView.findViewById(R.id.recipe_ingredients);
        recipeProcedureEdtTxt = (EditText) rootView.findViewById(R.id.recipe_procedure);
        userNameEdtTxt = (EditText) rootView.findViewById(R.id.user_name);
        recipeStateName = (EditText) rootView.findViewById(R.id.state_name);
        mobileNumberEdtTxt = (EditText) rootView.findViewById(R.id.mobile_number);

        TextView submitTxtVw = (TextView) rootView.findViewById(R.id.submit_btn);
        submitTxtVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRecipeAndUserDetails();
            }
        });

        return rootView;
    }


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "temp.jpg");
                    mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intentPicture, CAMERA_REQUEST_CODE);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,
                            GALLERY_REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {

                imgPath = getRealPathFromURI(mCapturedImageURI);

            } else if (requestCode == GALLERY_REQUEST_CODE) {

                if (data != null && data.getData() != null) {
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgPath = cursor.getString(columnIndex);
                        cursor.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            String fileNameSegments[] = imgPath.split("/");
            fileName = fileNameSegments[fileNameSegments.length - 1];
            // Put file name in Async Http Post Param which will used in Php web app
            params.put("filename", fileName);

            uploadImage();

        }
    }

    // Convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {
        String result = null;
        Cursor cursor = getActivity().getContentResolver().query(contentUri,
                null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if (idx != -1) {
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }

    // When Upload button is clicked
    public void uploadImage() {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            prgDialog.setMessage("Please wait...");
            prgDialog.setCancelable(false);
            prgDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getActivity(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return encodedString;
            }

            @Override
            protected void onPostExecute(String encodeString) {
                prgDialog.hide();
                // Put converted Image string into Async Http Post param
                showMessage("Image selected successfully");
            }
        }.execute(null, null, null);
    }


    public void submitRecipeAndUserDetails() {

        if (recipeName.getText().toString().equals("")) {
            showMessage("Please enter recipe name!");
            recipeName.requestFocus();
            return;
        }

        if (recipeType.getCheckedRadioButtonId() == -1) {
            showMessage("Please select recipe type!");
            recipeType.requestFocus();
            return;
        }

        if (recipeIngredientsEdtTxt.getText().toString().equals("")) {
            showMessage("Please provide recipe ingredients name!");
            recipeIngredientsEdtTxt.requestFocus();
            return;
        }

        if (recipeProcedureEdtTxt.getText().toString().equals("")) {
            showMessage("Please provide recipe procedure!");
            recipeProcedureEdtTxt.requestFocus();
            return;
        }

        if (userNameEdtTxt.getText().toString().equals("")) {
            showMessage("Please enter your name!");
            userNameEdtTxt.requestFocus();
            return;
        }

        if (recipeStateName.getText().toString().equals("")) {
            showMessage("Please enter state name!");
            recipeStateName.requestFocus();
            return;
        }

        String recipeTypeStr = ((RadioButton) rootView.findViewById(recipeType.getCheckedRadioButtonId())).getText().toString();
        String recipeNameStr = recipeName.getText().toString();

        String videoLinkStr = recipeVideoUrl.getText().toString();
        String recipeIngredientsStr = recipeIngredientsEdtTxt.getText().toString();
        String recipeProcedureStr = recipeProcedureEdtTxt.getText().toString();
        String userNameStr = userNameEdtTxt.getText().toString();
        String recipeStateStr = recipeStateName.getText().toString();
        String userMobileStr = mobileNumberEdtTxt.getText().toString();

        String userEmailId = new Utility(getActivity()).getUserEmailId();

        params.put("recipe_type", recipeTypeStr);
        params.put("recipe_name", recipeNameStr);
        params.put("recipe_video_url", videoLinkStr);
        params.put("recipe_ingredients", recipeIngredientsStr);
        params.put("recipe_procedure", recipeProcedureStr);
        params.put("username", userNameStr);
        params.put("recipeStateStr", recipeStateStr);
        params.put("user_mobile", userMobileStr);
        params.put("user_email", userEmailId);
        params.put("filename", fileName);
        params.put("recipe_image", encodedString);

        makeHTTPCall();
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        prgDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("apps.creativebucket.in/index/upload_user_recipe.php",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        Toast.makeText(getActivity(), response,
                                Toast.LENGTH_LONG).show();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            showMessage("Requested resource not found");
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            showMessage("Something went wrong at server end");
                        }
                        // When Http response code other than 404, 500
                        else {
                            showMessage("Error Occured n Most Common Error: n1. Device not connected to Internet n2. Web App is not deployed in App server n3. App server is not runningn HTTP Status code : "
                                    + statusCode);

                        }
                    }
                });
    }

    private void showMessage(String str) {
        Toast toast = Toast.makeText(getActivity(), str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}

