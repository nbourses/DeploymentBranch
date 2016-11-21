package com.nbourses.oyeok.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.nbourses.oyeok.RPOT.ApiSupport.models.UpdateProfile;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import io.realm.Realm;
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
//
//    @Bind(R.id.txtEmail)
//    TextView emailTxt1;
     String name;
    TextView tx;
    String email ;
    String namePattern = "[a-zA-Z]+[a-zA-Z0-9._-]+";
    String emailPattern = "[a-z]+[a-zA-Z0-9._-]+@[a-z]+\\.+(in|com|org|net)";
    String emailPattern1 ="[a-z]+[a-zA-Z0-9._-]+@[a-z]+\\.+[ac]+\\.+[in]";

    private Realm myRealm;

    public Profile() {
//        namePattern = "[a-zA-Z0-9]+";
//
//        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

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

        myRealm = General.realmconfig(getContext());



        dbhelper=new DBHelper(getActivity());
        username_txt=(EditText)layout.findViewById(R.id.txt_user);
        updateProfile= (Button)layout.findViewById(R.id.update_profile);
        Log.i("TAG","fakata name 12 "+dbhelper.getValue(DatabaseConstants.name));
        Log.i("TAG","fakata email 12 "+dbhelper.getValue(DatabaseConstants.email));
        Log.i("TAG","fakata email 13 "+General.getSharedPreferences(getContext(), AppConstants.NAME));
        Log.i("TAG","fakata email 13 "+General.getSharedPreferences(getContext(), AppConstants.EMAIL));
        Log.i("TAG","fakata email 14 "+General.getSharedPreferences(getContext(), AppConstants.MOBILE_NUMBER));
        Log.i("TAG","fakata email 14 "+General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER));


        if(!General.getSharedPreferences(getContext(), AppConstants.NAME).equals("null")) {
            Log.i("Profile","name "+General.getSharedPreferences(getContext(), AppConstants.NAME));
            username_txt.setText(General.getSharedPreferences(getContext(), AppConstants.NAME));
        }

        phoneTxt= (TextView) layout.findViewById(R.id.txt_phone);
        if(!General.getSharedPreferences(getContext(), AppConstants.MOBILE_NUMBER).equals("null")) {
            Log.i("Profile","name "+General.getSharedPreferences(getContext(), AppConstants.MOBILE_NUMBER));
            phoneTxt.setText(General.getSharedPreferences(getContext(), AppConstants.MOBILE_NUMBER));
        }

        emailTxt=(EditText)layout.findViewById(R.id.txt_email);
        if(!General.getSharedPreferences(getContext(), AppConstants.EMAIL).equals("null")) {
            Log.i("Profile","email "+General.getSharedPreferences(getContext(), AppConstants.EMAIL));
            emailTxt.setText(General.getSharedPreferences(getContext(), AppConstants.EMAIL));
        }

        role_txt=(TextView)layout.findViewById(R.id.txt_role);
        if(!General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER).equals("null")) {
            Log.i("Profile","user "+General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER));
            role_txt.setText(General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER));
        }

        profileImage= (ImageView)layout.findViewById(R.id.profile_pic);
//        if(!dbhelper.getValue(DatabaseConstants.imageFilePath).equalsIgnoreCase("null")) {
//            Log.i("Profile","name "+dbhelper.getValue(DatabaseConstants.imageFilePath));
//            filePath = dbhelper.getValue(DatabaseConstants.imageFilePath);
//            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
//            profileImage.setImageBitmap(yourSelectedImage);
//        }

        updateProfile= (Button) layout.findViewById(R.id.update_profile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                name  = username_txt.getText().toString().trim();
                email = emailTxt.getText().toString().trim();
                validationCheck();

                //updateProfile();
            }
        });

//        profileImage.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                Log.i("Profile","Profile image updated");
//                //intent.putExtra("crop","true");
//               // intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 1);
//            }
//        });
        // set cursor at the end
        username_txt.setSelection(username_txt.getText().length());

//        ((MainActivity)getActivity()).changeDrawerToggle(false,"Profile");


//        validationCheck();

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Profile","onActivity result");

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

        if(General.isNetworkAvailable(getContext())) {
            General.slowInternet(getContext());

        String API = AppConstants.SERVER_BASE_URL;
        User user = new User();
        user.setMobileNo(phoneTxt.getText().toString());
        user.setEmail(emailTxt.getText().toString());
        user.setName((username_txt.getText().toString()));
        user.setUserRole((String) role_txt.getText());
        user.setUserId(dbhelper.getValue(DatabaseConstants.userId));
        user.setMyPhoto(filePath);
        user.setPlatform("android");
        user.setSeeWhat("all");
        user.setAdditionalProperty(null, null);

        //{"email":"nvew@xyz", "user_role":"broker", "name": "New","my_photo":"smiles", "user_id":"a03ap69xm641mfoldqjlx15h1a27vy07"}
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        UserApiService userApiService = restAdapter.create(UserApiService.class);
        Log.i("Profile","update profile request call"+user);
        //if (dbhelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") && isNetworkAvailable()) {
        userApiService.userUpdateProfile(user, new Callback<UpdateProfile>() {

            @Override
            public void success(UpdateProfile updateProfile, Response response) {

                General.slowInternetFlag = false;
                General.t.interrupt();
                Log.i("Profile","success"+response);
                SnackbarManager.show(
                    Snackbar.with(getContext())
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("Profile updated successfully")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                        dbhelper.save(DatabaseConstants.email, emailTxt.getText().toString());
                General.setSharedPreferences(getContext(), AppConstants.EMAIL,emailTxt.getText().toString());
                dbhelper.save(DatabaseConstants.name,username_txt.getText().toString());
                General.setSharedPreferences(getContext(),AppConstants.NAME,username_txt.getText().toString());
                dbhelper.save(DatabaseConstants.imageFilePath,filePath);
                //drawerFragment = (FragmentDrawer) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
//                AppConstants.EMAIL_PROFILE=email;
                profileImageMain = (ImageView)getActivity().findViewById(R.id.profile_image_main);

//                if(!dbhelper.getValue(DatabaseConstants.imageFilePath).equalsIgnoreCase("null")) {
//                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(dbhelper.getValue(DatabaseConstants.imageFilePath));
//                    profileImageMain.setImageBitmap(yourSelectedImage);
//                }

               /* if (!General.getSharedPreferences(getContext(),AppConstants.EMAIL).equalsIgnoreCase("null")) {
                    emailTxt1.setVisibility(View.VISIBLE);
                    emailTxt1.setText(General.getSharedPreferences(getContext(),AppConstants.EMAIL));

                }*/
                /*BrokerMainActivity act=new
                         BrokerMainActivity();
                act.profileEmailUpdate(email);*/
//                ((BrokerMainActivity)getContext()).profileEmailUpdate(email);

                Intent in = new Intent(AppConstants.EMAIL_PROFILE);
                //in.putExtra("emailProfile", email);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
//                tx=(TextView) getView().findViewById(R.id.txtEmail);
//                Log.i("kaka","kaka"+tx.getText());
//                tx.setText("sushil");
//                Log.i("kaka","kaka"+tx.getText());

            }

            @Override
            public void failure(RetrofitError error) {

                General.slowInternetFlag = false;
                General.t.interrupt();

                Log.i("update profile", "failed "+error );
            }
        });

    }else{

        General.internetConnectivityMsg(getContext());
    }
    }



    private void validationCheck() {
         name  = username_txt.getText().toString().trim();
         email = emailTxt.getText().toString().trim();
        Log.i("TRACE","inside validCheck : "+name+" email: "+email);
       // && name.length() > 0

        if (name.equalsIgnoreCase("") ) {
            username_txt.setError("Please enter name");
            Log.i("TRACE", "Plz enter name");
            return;

        }else if (email.equalsIgnoreCase("")) {
            emailTxt.setError("Please enter email-id");
            Log.i("TRACE", "Plz enter email");
            return;
        }

         if (name.matches(namePattern) && (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) )
        {
            updateProfile();
                //if (!dbHelper.getValue(DatabaseConstants.email).equalsIgnoreCase("null")) {


        }

        else if (!(email.matches(emailPattern) || email.matches(emailPattern1)) )
        {
            emailTxt.setError("Please enter valid email-id");
            Log.i("TRACE", "Plz enter email");
            return;


        }
        else
        {
            username_txt.setError("Please  enter valid name");
            Log.i("TRACE", "Plz enter name");
            return;
        }


//email.matches(emailPattern) || email.matches(emailPattern1)


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
