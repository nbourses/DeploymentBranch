package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.nbourses.oyeok.activities.BrokerListingActivity;
import com.nbourses.oyeok.activities.MyPortfolioActivity;
import com.nbourses.oyeok.adapters.ListingTitleAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.CreateCatalogListing;

import com.nbourses.oyeok.models.portListingModel;
import com.nbourses.oyeok.realmModels.ListingCatalogRealm;
import com.nbourses.oyeok.realmModels.Listingidsrealm;
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

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListingTitle extends Fragment {

    View v;
    ImageView list_catalog_dp,list_done,list_editProfile_pic,list_btn_delete;
    EditText list_catalog_name;
    ListView list_selected_info;
    TextView list_back;
    private static ArrayList<portListingModel> selectedList=new ArrayList<>();

    private static ArrayList<portListingModel> copyselectedlist = new ArrayList<>();
    private static ArrayList<portListingModel> templist = new ArrayList<>();


    ListingTitleAdapter adapter;
    ProgressBar pg_create_watch;



//image
    private static Bitmap Image = null;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static Bitmap rotateImage = null;
    private static Uri mImageUri;

 //s3
    private File fileToUpload = new File("/storage/emulated/0/DCIM/Facebook/FB_IMG_1467990952511.jpg");
    private String imageName = null;
    public TransferUtility transferUtility;
    public AmazonS3 s3;
    private static String Imagepath;
    private String channel_name = "";

//realm data
    Realm realm;
    private static RealmList<Listingidsrealm> realmList = new RealmList<>();
    
    
    
    String catalog_id;
    ArrayList<String> ids=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v =inflater.inflate(R.layout.fragment_listing_title, container, false);
        list_catalog_dp=(ImageView)v.findViewById(R.id.list_watchlist_dp);
        list_done=(ImageView)v.findViewById(R.id.list_done);
        list_editProfile_pic=(ImageView)v.findViewById(R.id.list_editProfile_pic);
        list_btn_delete=(ImageView)v.findViewById(R.id.list_btn_delete);
        list_catalog_name=(EditText)v.findViewById(R.id.list_catalog_name);
        list_back=(TextView)v.findViewById(R.id.list_back1);
        list_selected_info=(ListView)v.findViewById(R.id.list_selected_info);
        pg_create_watch=(ProgressBar)v.findViewById(R.id.pg_create_watch);

//        if(portListing1 != null)
//            portListing1.clear();
        /*if(selectedList != null)
        selectedList.clear();*/

        selectedList= ((BrokerListingActivity)getActivity()).PortlistingData1();
        adapter=new ListingTitleAdapter(getContext(),selectedList);
        list_selected_info.setAdapter(adapter);

        list_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_catalog_name.getText().toString().length()>3){
                    pg_create_watch.setVisibility(View.VISIBLE);
                    CreateCatalog();
                }else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Invalid Title")
                            .setMessage("Please Type Catalog title (length should be greater then 3 character)!")
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

        list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((BrokerListingActivity)getActivity()).Back();
            }
        });

        list_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                templist.clear();
                // selectedlist.addAll(adapter.getAllData());
                for(portListingModel hold: adapter.getAllData()){
                    if(hold.isCheckbox()){

                        // selectedlist.remove(hold);

                        //portListingModel loadBuildingDataModel1=new portListingModel(hold.getName(),hold.getLat(),hold.getLng(),hold.getId(),hold.getLocality(),hold.getCity(),hold.getLl_pm(),hold.getOr_psf());
                        templist.add(hold);
                        Log.i("selected1","selected building hold : "+hold.getName());
                    }

                }

                copyselectedlist.clear();
                copyselectedlist.addAll(selectedList);

                for ( portListingModel d : templist) {
                    Log.i("selected1","selected building : templist "+selectedList.size()+" contains  "+selectedList.contains(d));

                    for(portListingModel c : copyselectedlist)
                        if(d.getId()==c.getId()){
                            Log.i("selected1","selected building : c "+c.getName()+"d  "+d.getName());
                            selectedList.remove(c);
                            break;
                        }

                }
                copyselectedlist.clear();
                Log.i("selected1","selected building : "+selectedList.size());
                adapter.notifyDataSetChanged();


            }
        });

        list_editProfile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        list_selected_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setCheckBox(position);
                Log.i("multimode","inside onItemClick  123 "+position);
            }
        });

        list_catalog_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_catalog_dp.setImageBitmap(null);
                if (Image != null)
                    Image.recycle();
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        RESULT_LOAD_IMAGE);
            }
        });


        init();
        return v;
    }







    @Override
    public void onDetach() {
        super.onDetach();
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("google signin", "onActivityResult");
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode != 0) {
            mImageUri = data.getData();
            /*fileToUpload = new File(getRealPathFromURI(this,mImageUri));
           // Log.i(TAG, "lolwa imagewa uri fileToUpload "+fileToUpload);
            setFileToUpload(saveBitmapToFile(fileToUpload));*/
            fileToUpload = new File(getRealPathFromURI(getContext(),mImageUri));
            Log.i("google12", "onActivityResult   "+fileToUpload);
            setFileToUpload(saveBitmapToFile(fileToUpload));
            try {
                Image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mImageUri);
                if (getOrientation(getContext(), mImageUri) != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(getOrientation(getContext(), mImageUri));
                    if (rotateImage != null)
                        rotateImage.recycle();
                    rotateImage = Bitmap.createBitmap(Image, 0, 0, Image.getWidth(), Image.getHeight(), matrix, true);
                    list_catalog_dp.setImageBitmap(rotateImage);
                } else
                    list_catalog_dp.setImageBitmap(Image);
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



    public void setFileToUpload(File fileToU){

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

/*
    private void displayImgMessage(String bucketName, String imgName){

        Log.i("image1", "calipso inside displayimage msg"+bucketName +" "+imgName );
        Log.i("image1", "displayImgMessage called ");

        String user_id = null;


        if(General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("yes"))
            user_id = General.getSharedPreferences(getContext(),AppConstants.USER_ID);
        else
            user_id = General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI);



        try {
            JSONObject jsonMsg = new JSONObject();

            jsonMsg.put("timestamp",String.valueOf(System.currentTimeMillis()));


            jsonMsg.put("_from", user_id);

            jsonMsg.put("to", channel_name);
            if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
                jsonMsg.put("status","OKI");
            else if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                jsonMsg.put("status","OYEI");

            jsonMsg.put("name","");




            jsonMsg.put("message", "https://s3.ap-south-1.amazonaws.com/"+bucketName+"/"+imgName);
            Log.i("yoyoyo","urlo "+jsonMsg);
            Log.i("image1", "calipso inside calling displayMsg "+jsonMsg );

            *//*displayMessage(jsonMsg);
            sendNotification(jsonMsg);*//*
//            pubnub.publish(channel_name, jsonMsg, true, new Callback() {});


        }
        catch(Exception e){}


    }*/


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
                getApplicationContext(),
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

        transferUtility = new TransferUtility(s3, getApplicationContext());
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



    private void CreateCatalog(){
        Log.i("magical","CreateCatalog =========== ");
        CreateCatalogListing createCatalogListing=new CreateCatalogListing();
        createCatalogListing.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
        createCatalogListing.setAction("create");
        createCatalogListing.setCatalog_id("");
        createCatalogListing.setCity("Mumbai");
        createCatalogListing.setTt((AppConstants.TT_TYPE).toLowerCase());
        ids.clear();
        for(portListingModel c:selectedList){
            //loadBuildingdataModelRealm  loadBuildingdataModelRealm1=new loadBuildingdataModelRealm(c.getId(),c.getName(),c.getLat(),c.getLng(),c.getLocality(),c.getCity(),c.getLl_pm(),c.getOr_psf(),c.isCheckbox());
            ids.add(c.getId());
            Log.i("magical","inside ===========  : "+c.getId());
        }
        createCatalogListing.setTitle(list_catalog_name.getText().toString());
        createCatalogListing.setListing_ids(ids);
        Log.i("magical","inside =========== ");
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        oyeokApiService.CreateCataloglist(createCatalogListing, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    Log.i("magical","addBuildingRealm success response "+response+"\n"+jsonResponse);
                    JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                    catalog_id=building.getString("catalog_id");
                    Log.i("magical","addBuildingRealm success response "+building+"  id :: "+building.getString("catalog_id"));

                    int size = building.length();
                       /* if (realmsids != null)
                            realmsids.clear();*/

                    AddDataToRealm();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("magical","failure =========== "+error);
            }
        });


    }


    public void AddDataToRealm(){
        Realm myRealm = General.realmconfig(getContext());
        Log.i("datafromraelm1", "realm data 12 : " +catalog_id+" "+list_catalog_name.getText().toString()+" "+AppConstants.TT_TYPE);

        ListingCatalogRealm listingCatalogRealm=new ListingCatalogRealm();
        listingCatalogRealm.setCatalog_id(catalog_id);
        listingCatalogRealm.setCatalog_name(list_catalog_name.getText().toString());
        listingCatalogRealm.setImageuri(imageName);
        listingCatalogRealm.setCity("Mumbai");
        listingCatalogRealm.setTt(AppConstants.TT_TYPE);
        listingCatalogRealm.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
        listingCatalogRealm.setUser_name(General.getSharedPreferences(getContext(),AppConstants.NAME));
//        listingCatalogRealm.setDisplayListings(null);
        //listingCatalogRealm.setUser_role(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER));
        int size=ids.size();
        realmList.clear();
        for(int i=0;i<size;i++){
        /*loadBuildingdataModelRealm  loadBuildingdataModelRealm1=new loadBuildingdataModelRealm(c.getId(),c.getName(),c.getLat(),c.getLng(),c.getLocality(),c.getCity(),c.getLl_pm(),c.getOr_psf(),c.isCheckbox());
        realmList.add(loadBuildingdataModelRealm1);*/
            Log.i("datafromraelm1", "realm data 12 : "+i+" :  " + ids.get(i).toString());
            Listingidsrealm  listingidsrealm=new Listingidsrealm(ids.get(i).toString());
            realmList.add(listingidsrealm);
        }
        listingCatalogRealm.setListingids(realmList);
        myRealm.beginTransaction();
        myRealm.copyToRealmOrUpdate(listingCatalogRealm);
        myRealm.commitTransaction();

        //RealmResults<listingCatalogRealm> results2 = myRealm.where(listingCatalogRealm.class).equalTo("watchlist_id", watchlist_id).findAll();
        RealmResults<ListingCatalogRealm> results2 = myRealm.where(ListingCatalogRealm.class).equalTo("catalog_id",catalog_id).findAll();

        for (ListingCatalogRealm c : results2) {

           for(int i=0;i<c.getListingids().size();i++) {
            Log.i("datafromraelm", "realm data  : "+i+" :  " + c.getListingids().get(i).getListing_id());
           }

        }
        myRealm.close();
        pg_create_watch.setVisibility(View.GONE);
        ((BrokerListingActivity)getActivity()).Refresh();
    }





}
