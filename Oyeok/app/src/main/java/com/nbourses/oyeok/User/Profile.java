package com.nbourses.oyeok.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.SignUp;
import com.nbourses.oyeok.RPOT.ApiSupport.models.UpdateProfile;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.NavDrawer.FragmentDrawer;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Profile extends Fragment {






    private TextView role_txt,phoneTxt;
    private EditText emailTxt,username_txt;
    private Button updateProfile;
    private ImageView profileImage,profileImageMain;
    DBHelper dbhelper;
    String filePath="";



    public Profile() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout= inflater.inflate(R.layout.fragment_profile, container, false);
        dbhelper=new DBHelper(getActivity());
        username_txt=(EditText)layout.findViewById(R.id.txt_user);
        updateProfile= (Button)layout.findViewById(R.id.update_profile);
        if(!dbhelper.getValue(DatabaseConstants.name).equals("null"))
            username_txt.setText(dbhelper.getValue(DatabaseConstants.name));

        phoneTxt= (TextView) layout.findViewById(R.id.txt_phone);
        if(!dbhelper.getValue(DatabaseConstants.mobileNumber).equals("null"))
            phoneTxt.setText(dbhelper.getValue(DatabaseConstants.mobileNumber));

        emailTxt=(EditText)layout.findViewById(R.id.txt_email);
        if(!dbhelper.getValue(DatabaseConstants.email).equals("null"))
            emailTxt.setText(dbhelper.getValue(DatabaseConstants.email));

        role_txt=(TextView)layout.findViewById(R.id.txt_role);
        if(!dbhelper.getValue(DatabaseConstants.user).equals("null"))
            role_txt.setText(dbhelper.getValue(DatabaseConstants.user));

        profileImage= (ImageView)layout.findViewById(R.id.profile_image);
        if(!dbhelper.getValue(DatabaseConstants.imageFilePath).equalsIgnoreCase("null")) {
            filePath = dbhelper.getValue(DatabaseConstants.imageFilePath);
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
            profileImage.setImageBitmap(yourSelectedImage);
        }

        updateProfile= (Button) layout.findViewById(R.id.update_profile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                updateProfile();
            }
        });

                profileImage.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        //intent.putExtra("crop","true");
                        //intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);
                    }
                });



        return layout;
    }

                        // TODO: Rename method, update argument and hook method into UI event

                public void onActivityResult(int requestCode, int resultCode, Intent data)
                {
                    super.onActivityResult(requestCode, resultCode, data);

                    //switch(requestCode) {
                        /*case 1234:*/
                            if(resultCode == Activity.RESULT_OK){
                                Uri selectedImage = data.getData();
                                //String WholeId
                                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                filePath = cursor.getString(columnIndex);
                                cursor.close();


                                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                                Log.i("selected image",filePath);
                                profileImage.setImageBitmap(yourSelectedImage);
                                    /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                            //}
                    }

                };

                public void updateProfile() {
                    String API = DatabaseConstants.serverUrl;
                    User user = new User();
                    user.setMobileNo(phoneTxt.getText().toString());
                    user.setEmail(emailTxt.getText().toString());
                    user.setName((username_txt.getText().toString()));
                    user.setUserRole((String) role_txt.getText());
                    user.setUserId(dbhelper.getValue(DatabaseConstants.userId));
                    user.setMyPhoto(filePath);
                    user.setPlatform("android");
                    user.setSeeWhat("all");


                    //{"email":"nvew@xyz", "user_role":"broker", "name": "New","my_photo":"smiles", "user_id":"a03ap69xm641mfoldqjlx15h1a27vy07"}
                    RestAdapter restAdapter1 = new RestAdapter.Builder().setEndpoint(API).build();
                    restAdapter1.setLogLevel(RestAdapter.LogLevel.FULL);
                    UserApiService user1 = restAdapter1.create(UserApiService.class);
                    //if (dbhelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") && isNetworkAvailable()) {
                        user1.userUpdateProfile(user, new Callback<UpdateProfile>() {

                            @Override
                            public void success(UpdateProfile updateProfile, Response response) {
                                Log.i("update profile", "success");
                                dbhelper.save(DatabaseConstants.email, emailTxt.getText().toString());
                                dbhelper.save(DatabaseConstants.name,username_txt.getText().toString());
                                dbhelper.save(DatabaseConstants.imageFilePath,filePath);
                                //drawerFragment = (FragmentDrawer) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

                                profileImageMain = (ImageView)getActivity().findViewById(R.id.profile_image_main);
                                if(!dbhelper.getValue(DatabaseConstants.imageFilePath).equalsIgnoreCase("null")) {
                                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(dbhelper.getValue(DatabaseConstants.imageFilePath));
                                    profileImageMain.setImageBitmap(yourSelectedImage);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("update profile", "failed");
                            }
                        });
                    }
                //}

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
