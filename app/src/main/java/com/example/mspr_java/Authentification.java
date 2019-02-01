package com.example.mspr_java;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Authentification extends AppCompatActivity {


    static final int REQUEST_TAKE_PHOTO = 1;
    File mCurrentPhotoPath;
    EditText editTextAuth;
    AlertDialog alertDialog;
    String token = "";
    RelativeLayout mainContainer;
    RelativeLayout loadingScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        mainContainer=(RelativeLayout)findViewById(R.id.main_container);
        editTextAuth = (EditText) findViewById(R.id.editTextAuth);
        RelativeLayout bgLayout = (RelativeLayout) findViewById(R.id.main_container);
        AnimationDrawable animationDrawable = (AnimationDrawable) bgLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
        annimationButton();
        createAlertDialog();
        try {
            createImageFile();
        } catch (IOException e) {
            Log.e("ERREUR","Impossible de creer le fichier");
            e.printStackTrace();
        }
        //A DEGAGER
        //getToMainActivity();
        //dispatchTakePictureIntent();
    }

    private void createAlertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("L'authentification a échouée veuillez recommencer");
        dialog.setTitle("Echec de l'Authentification");
        dialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
        alertDialog=dialog.create();
    }


    private void createImageFile() throws IOException {
        String imageFileName = "ImageTempo";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        try {
            image.delete();
        }catch(Exception e){
            Log.e("Sheh","impossible de delete l'image");
        }
        mCurrentPhotoPath = image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = mCurrentPhotoPath;

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //DISPLAY LOADING SCREEN TOO
           String image = encode();
           Log.e("Image ",""+image);
           String id = editTextAuth.getText().toString();
           Log.e("IDENTIFIANT","de connexion : "+id);
           auth(image,id);

        }
    }

    private String encode(){
        File file = new File(mCurrentPhotoPath.getAbsolutePath());
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String returnStr = "";
        try {
            returnStr = new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("ERREUR","Unsupported encoding exception");
        }
        return returnStr;
    }
    public void didTapButton(View view) {
        annimationButton();

        ////////////////////////
        if(editTextAuth.getText().toString().isEmpty()||editTextAuth.getText().toString().equals("")){
            Snackbar.make(view, getString(R.string.toastNoId), Snackbar.LENGTH_LONG)
                    .show();
                //Toast.makeText(this, getString(R.string.toastNoId),Toast.LENGTH_LONG).show();

        }else {
            dispatchTakePictureIntent();
        }
        ////////////////////////


    }

    public void annimationButton(){
        ImageButton button = (ImageButton) findViewById(R.id.ConnectButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        button.startAnimation(myAnim);
    }

    public void auth(String image,String id){
        ComServerAuth auth = new ComServerAuth(this);
        try {
            //auth.post(getString(R.string.lienAuth),image,id);
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            auth.request("auth", headers,auth.generateJson(image,id));
            startLoadingScreen();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur d'authentification",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void startLoadingScreen() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout loadingScreenContainer = (RelativeLayout) inflater.inflate(R.layout.loading_screen, mainContainer,false);
        ImageView loadingImage = (ImageView)loadingScreenContainer.getChildAt(1);
        mainContainer.addView(loadingScreenContainer);
        RotateAnimation rotate = new RotateAnimation(
                0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        loadingImage.startAnimation(rotate);
        loadingScreen=loadingScreenContainer;

    }

    private void stopLoadingScreen(){
        if(loadingScreen!=null){
            mainContainer.removeView(loadingScreen);
        }

    }

    public void retourAuth(int code, String token){
        Log.e("TOKEN RETOUR",""+token);
        stopLoadingScreen();
        if(code!=200){
            alertDialogError();
        }else{
            this.token = token;
            getToMainActivity();
        }

        return;
    }
    private void alertDialogError() {
        runOnUiThread(new Runnable() {
            public void run() {
                alertDialog.show();
            }
        });
    }
    public void getToMainActivity(){
        if(!token.equals("")){
        Intent intent = new Intent(this,Main_Activity.class);
        intent.putExtra("token", token);
        startActivity(intent);
        }
    }
}
