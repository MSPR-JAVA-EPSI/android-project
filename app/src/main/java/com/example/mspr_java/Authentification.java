package com.example.mspr_java;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
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
import java.io.FileOutputStream;
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
           String image = encode64();
           Log.e("Image ",""+image);
           String id = editTextAuth.getText().toString();
           Log.e("IDENTIFIANT","de connexion : "+id);
           auth(image,id);

        }
    }

    private String encodeByte(){
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
    private String encode64(){
        BitmapFactory.Options options = new BitmapFactory.Options();////////////////////////////////////
        options.inPreferredConfig = Bitmap.Config.ARGB_8888; ////////////////////////////////Useless rn
                                                                    ////////////////////////////////////
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath.getAbsolutePath());
        Log.e("Absolute path",""+mCurrentPhotoPath.getAbsolutePath());
        Log.e("Size",mCurrentPhotoPath.getTotalSpace()+"");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);

        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        try {
            stream.flush();

            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imgString = Base64.encodeToString(byteFormat,Base64.DEFAULT);
        imgString = imgString.replace("\n","");

        imgString ="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";
        return imgString;
    }
    public void didTapButton(View view) {
        annimationButton();

        ////////////////////////
        if(editTextAuth.getText().toString().isEmpty()||editTextAuth.getText().toString().equals("")){
            Snackbar.make(mainContainer, getString(R.string.toastNoId), Snackbar.LENGTH_LONG)
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
        loadingScreenContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
