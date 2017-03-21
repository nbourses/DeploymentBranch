package com.nbourses.oyeok.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.JsonElement;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.DealConversationActivity;
import com.nbourses.oyeok.activities.MyPortfolioActivity;
import com.nbourses.oyeok.adapters.WatchlistExplorerAdapter;
import com.nbourses.oyeok.adapters.watchlistAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.CreateWatchlistAPI;
import com.nbourses.oyeok.models.UpdateBuildingRateModel;
import com.nbourses.oyeok.models.loadBuildingDataModel;
import com.nbourses.oyeok.models.portListingModel;
import com.nbourses.oyeok.realmModels.Localities;
import com.nbourses.oyeok.realmModels.WatchListRealmModel;
import com.nbourses.oyeok.realmModels.loadBuildingdataModelRealm;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

//import static com.facebook.FacebookSdk.getApplicationContext;


public class WatchlistTitle extends Fragment {



    ImageView watchlist_dp,done,editProfile_pic,btn_delete;
    EditText watchlist_name;
    ListView selected_info;
    TextView back;
    watchlistAdapter adapter;
    View v;
    ProgressBar pg_create_watch;
    String watchlist_id;
    private static Bitmap Image = null;
    private static final int RESULT_LOAD_IMAGE = 1;
    //private CallbackManager callbackManager;
    private static Bitmap rotateImage = null;
   private static ArrayList<loadBuildingDataModel> selectedlist = new ArrayList<>();
    private static ArrayList<loadBuildingDataModel> copyselectedlist = new ArrayList<>();
    private static ArrayList<loadBuildingDataModel> templist = new ArrayList<>();
    private static RealmList<loadBuildingdataModelRealm> realmList = new RealmList<>();
    ArrayList<String> ids=new ArrayList<>();

    private static Uri mImageUri;



    //s3
    private File fileToUpload = new File("/storage/emulated/0/DCIM/Facebook/FB_IMG_1467990952511.jpg");
    private String imageName = null;
    public TransferUtility transferUtility;
    public AmazonS3 s3;
    private static String Imagepath;
    private String channel_name = "";

//constants
    String user_id,user_name,user_role;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         v=inflater.inflate(R.layout.fragment_watchlist_title, container, false);

        watchlist_dp=(ImageView)v.findViewById(R.id.watchlist_dp);
        done =(ImageView)v.findViewById(R.id.done);
        watchlist_name=(EditText)v.findViewById(R.id.watchlist_name);
        selected_info=(ListView) v.findViewById(R.id.selected_info);
        back=(TextView)v.findViewById(R.id.back);
        editProfile_pic=(ImageView)v.findViewById(R.id.editProfile_pic);
        btn_delete=(ImageView)v.findViewById(R.id.btn_delete);
        pg_create_watch=(ProgressBar)v.findViewById(R.id.pg_create_watch);
        if (selectedlist != null)
            selectedlist.clear();
        if (copyselectedlist != null)
            copyselectedlist.clear();

        if(ids!=null)
            ids.clear();

        selectedlist.addAll(((MyPortfolioActivity)getActivity()).passingListActivity());
        user_id=General.getSharedPreferences(getContext(),AppConstants.USER_ID);
        user_name=General.getSharedPreferences(getContext(),AppConstants.NAME);
        user_role=General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER);
        /*copyselectedlist.addAll(selectedlist);
        for(int i=0;i<copyselectedlist.size();i++){
            Log.i("selected11","selected building init : "+selectedlist.size()+"   === "+selectedlist.get(i).getName() );
            if(selectedlist.get(i).isCheckbox()) {
                selectedlist.remove(i);
            }

        }*/

        verifyStoragePermissions(getActivity());
        try {
            adapter= new watchlistAdapter(selectedlist,getContext());
            selected_info.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for(loadBuildingDataModel c: selectedlist){


                        *//*loadBuildingDataModel loadBuildingDataModel1=new loadBuildingDataModel(c.getName(),hold.getLat(),hold.getLng(),hold.getId(),hold.getLocality(),hold.getCity(),hold.getLl_pm(),hold.getOr_psf());
                        templist.add(loadBuildingDataModel1);*//*
                        Log.i("selected1","selected building : "+c.getName());


                }*/
                if(watchlist_name.getText().toString().length()>3){
                    pg_create_watch.setVisibility(View.VISIBLE);
                    CreateWatchlist();
                }else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Invalid Title")
                            .setMessage("Please Type watchlist title (length should be greater then 3 character eg: Properties for bachelor)!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            }
        });



        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("selected1","selected building : "+selectedlist.size()+"   === "+templist.size() );
                //selectedlist.clear();
                templist.clear();
               // selectedlist.addAll(adapter.getAllData());
                for(loadBuildingDataModel hold: adapter.getAllData()){
                    if(hold.isCheckbox()){

                       // selectedlist.remove(hold);

                        loadBuildingDataModel loadBuildingDataModel1=new loadBuildingDataModel(hold.getName(),hold.getLat(),hold.getLng(),hold.getId(),hold.getLocality(),hold.getCity(),hold.getLl_pm(),hold.getOr_psf());
                        templist.add(loadBuildingDataModel1);
                        Log.i("selected1","selected building hold : "+hold.getName());
                    }

                }

                copyselectedlist.clear();
                copyselectedlist.addAll(selectedlist);

                for ( loadBuildingDataModel d : templist) {
                    Log.i("selected1","selected building : templist "+selectedlist.size()+" contains  "+selectedlist.contains(d));

                    for(loadBuildingDataModel c : copyselectedlist)
                    if(d.getId()==c.getId()){
                        Log.i("selected1","selected building : c "+c.getName()+"d  "+d.getName());
                        selectedlist.remove(c);
                        break;
                    }

                }
                copyselectedlist.clear();
                Log.i("selected1","selected building : "+selectedlist.size());
                adapter.notifyDataSetChanged();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MyPortfolioActivity)getActivity()).Back("");
            }
        });
        editProfile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchlist_dp.performClick();
            }
        });

        watchlist_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchlist_dp.setImageBitmap(null);
                if (Image != null)
                    Image.recycle();
                if(verifyStoragePermissions(getActivity())) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            RESULT_LOAD_IMAGE);
                }



            }
        });

        selected_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.setCheckBox(position);
                Log.i("multimode","inside onItemClick  123 "+position+" "+selectedlist.size());


            }
        });

        init();

        return v;
    }

    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else
        {
            return true;
        }
    }

    public  void init(){




        if (Environment.getExternalStorageState() == null) {
            //create new file directory object
            File directory = new File(Environment.getDataDirectory()
                    + "/oyeok/watchlist/");

            // if no directory exists, create new directory
            if (!directory.exists()) {
                directory.mkdir();
            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            File directory = new File(Environment.getExternalStorageDirectory()
                    + "/oyeok/watchlist/");

            // if no directory exists, create new directory to store test
            // results
            if (!directory.exists()) {
                directory.mkdir();
            }
        }

        credentialsProvider();

        setTransferUtility();



    }





    private void CreateWatchlist(){
        CreateWatchlistAPI createWatchlistAPI=new CreateWatchlistAPI();
        createWatchlistAPI.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
        createWatchlistAPI.setAction("create");
        createWatchlistAPI.setCity("mumbai");
        createWatchlistAPI.setTt(AppConstants.TT_TYPE);
        for(loadBuildingDataModel c:selectedlist){
            //loadBuildingdataModelRealm  loadBuildingdataModelRealm1=new loadBuildingdataModelRealm(c.getId(),c.getName(),c.getLat(),c.getLng(),c.getLocality(),c.getCity(),c.getLl_pm(),c.getOr_psf(),c.isCheckbox());
            ids.add(c.getId());
        }
        createWatchlistAPI.setTitle(watchlist_name.getText().toString());
        createWatchlistAPI.setBuild_list(ids);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        oyeokApiService.CreateWatchlist(createWatchlistAPI, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    Log.i("magic11","addBuildingRealm success response "+response+"\n"+jsonResponse);
                    JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                    Log.i("magic11","addBuildingRealm success response "+building);
                    watchlist_id=building.getString("watchlist_id");
                    AddDataToRealm(watchlist_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("magic11c"," failure response   "+error);
                try {
                    if(error.getMessage().equalsIgnoreCase("500 Internal Server Error")){
                        SnackbarManager.show(
                                Snackbar.with(getContext())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text(R.string.server_error)
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    }else
                    SnackbarManager.show(
                            Snackbar.with(getContext())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .text("Server Error: " + error.getMessage())
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                }
                catch(Exception e){}
            }
        });


    }


public void AddDataToRealm(String watchlist_id){
    Realm myRealm = General.realmconfig(getContext());
    WatchListRealmModel watchListRealmModel=new WatchListRealmModel();
    watchListRealmModel.setWatchlist_id(watchlist_id);
    watchListRealmModel.setWatchlist_name(watchlist_name.getText().toString());
    watchListRealmModel.setImageuri(imageName);
    watchListRealmModel.setCity("Mumbai");
    watchListRealmModel.setTt(AppConstants.TT_TYPE);
    watchListRealmModel.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
    watchListRealmModel.setUser_name(General.getSharedPreferences(getContext(),AppConstants.NAME));
    watchListRealmModel.setUser_role(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER));
    if(realmList!=null)
        realmList.clear();
    for(int i=0;i<ids.size();i++){
        loadBuildingdataModelRealm  loadBuildingdataModelRealm1=new loadBuildingdataModelRealm(ids.get(i));
        realmList.add(loadBuildingdataModelRealm1);
    }
    watchListRealmModel.setBuildingids(realmList);
    if(myRealm.isInTransaction())
        myRealm.cancelTransaction();
    myRealm.beginTransaction();
    myRealm.copyToRealmOrUpdate(watchListRealmModel);
    myRealm.commitTransaction();
    //RealmResults<WatchListRealmModel> results2 = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findAll();
    RealmResults<WatchListRealmModel> results2 = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findAll();

    /*for (WatchListRealmModel c : results2) {

        for(int i=0;i<c.getBuildingids().size();i++) {
            Log.i("datafromraelm", "realm data  : "+i+" :  " + c.getBuildingids().get(i).getId());
        }


    }*/
    myRealm.close();
    pg_create_watch.setVisibility(View.GONE);
    ((MyPortfolioActivity)getActivity()).Close();
}




    /*public static WatchlistTitle newInstance() {
        WatchlistTitle fragment = new WatchlistTitle();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("google signin", "onActivityResult ");
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode != 0) {
            mImageUri = data.getData();
            /*fileToUpload = new File(getRealPathFromURI(this,mImageUri));
            Log.i(TAG, "lolwa imagewa uri fileToUpload "+fileToUpload);
            setFileToUpload(saveBitmapToFile(fileToUpload));*/
            fileToUpload = new File(getRealPathFromURI(getContext(),mImageUri));

            Log.i("JPEGIMAGE", "onActivityResult "+fileToUpload);
            File file= null;
                file = saveBitmapToFile(fileToUpload);

            Log.i("JPEGIMAGE", "onActivityResult file "+file);

            setFileToUpload(file);
            try {
                Image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mImageUri);
                if (getOrientation(getContext(), mImageUri) != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(getOrientation(getContext(), mImageUri));
                    if (rotateImage != null)
                        rotateImage.recycle();
                    rotateImage = Bitmap.createBitmap(Image, 0, 0, Image.getWidth(), Image.getHeight(), matrix, true);
                    watchlist_dp.setImageBitmap(rotateImage);
                } else
                    watchlist_dp.setImageBitmap(Image);
                Log.i("imageuri", "onActivityResult imageuri : "+Image+" mImageUri : "+mImageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    private File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);


            Log.i("JPEG IMAGE","===================");
            return file;
        } catch (Exception e) {
            return null;
        }
    }





    /*private File saveBitmapToFile(File file) throws IOException {
       // try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
           // inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100 , outputStream);


            Log.i("JPEGIMAGE","=================== :: "+file);
            return file;
       *//* } catch (Exception e) {
            return null;
        }*//*
    }*/



    public void setFileToUpload(File fileToU){
        Log.i("JPEGIMAGE","==================="+fileToU+"   "+fileToUpload);
        fileToUpload = fileToU;

        imageName = "andro"+String.valueOf(System.currentTimeMillis())+".png";

        try {
            if (Environment.getExternalStorageState() == null) {
                //create new file directory object
                File directory = new File(Environment.getDataDirectory()
                        + "/oyeok/watchlist/");


                // if no directory exists, create new directory
                if (!directory.exists()) {
                    directory.mkdir();
                }

                File destdir = new File(Environment.getExternalStorageDirectory()
                        + "/oyeok/watchlist/" + imageName);
                Log.i("image1", "dest diro " + destdir);
                copyFileUsingStream(fileToUpload, destdir);
                Log.i("image1", "file after save ");

                // if phone DOES have sd card
            } else if (Environment.getExternalStorageState() != null) {
                // search for directory on SD card
                File directory = new File(Environment.getExternalStorageDirectory()
                        + "/oyeok/watchlist/");

                // if no directory exists, create new directory to store test
                // results
                if (!directory.exists()) {
                    directory.mkdir();
                }
                File destdir = new File(Environment.getExternalStorageDirectory()
                        + "/oyeok/watchlist/" + imageName);
                Log.i("image1", "dest diro " + destdir);
                copyFileUsingStream(fileToUpload, destdir);
                Log.i("image1", "file after save ");
            }
        }
        catch (Exception e)
        {

            Log.i("image1", "Caught in exFception saving image /watch " + e);

        }


       // displayImgMessage("oyeok-watchlist-images",imageName); //oyes-watchlist-images//oyeok-chat-images

        TransferObserver transferObserver = transferUtility.upload(
                "oyeok-watchlist-images",      //The bucket to upload to
                imageName,    // The key for the uploaded object
                fileToUpload       // The file where the data to upload exists
        );

        transferObserverListener(transferObserver);
        //

    }



    public static void copyFileUsingStream(File source, File dest) throws IOException {

        Log.i("image1","fila source "+source);
        Log.i("image1","fila dest "+dest);

        InputStream is = null;
        OutputStream os = null;

        try {

            is = new FileInputStream(source);
            os = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

        } finally {

            is.close();
            os.close();
        }
    }



//////      jsonMsg.put("message", "https://s3.ap-south-1.amazonaws.com/"+bucketName+"/"+imgName);   ///////





    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.i("sushilimage statechange", state+"");
                if(state.toString().equalsIgnoreCase("COMPLETED")){

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.i("imageu percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.i("imageu error","error"+ex);
            }

        });
    }




    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),
                "us-east-1:d166d43a-32b3-4690-9975-aed1c61f2b28", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }


    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));

    }



    public void setTransferUtility(){

        transferUtility = new TransferUtility(s3, getContext());
    }



    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //myRealm.close();
    }



}
