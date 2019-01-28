package com.example.mspr_java;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Response;

public class Authentification extends AppCompatActivity {


    static final int REQUEST_TAKE_PHOTO = 1;
    File mCurrentPhotoPath;
    EditText editTextAuth;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        editTextAuth = findViewById(R.id.editTextAuth);
        RelativeLayout bgLayout = findViewById(R.id.main_container);
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
        getToMainActivity();
        //dispatchTakePictureIntent();
    }

    private void createAlertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("L'authentification a échouée veuillez recommencer");
        dialog.setTitle("Echec de l'Authentification");
        dialog.setPositiveButton("YES",
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
           String bit64 = encode();
           Log.e("Image Bit64",""+bit64);
           String id = editTextAuth.getText().toString();
           Log.e("IDENTIFIANT","de connexion : "+id);
           auth(bit64,id); // AUTHENTIFICATION
           /*byte[] imageBytes = Base64.decode(bit64, Base64.DEFAULT);
           Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
           mImageView.setImageBitmap(decodedImage);
           */
        }
    }

    private String encode(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath.getAbsolutePath(), options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);
        return imgString;
    }


    public void didTapButton(View view) {
        annimationButton();
        if(editTextAuth.getText().toString().equals("test"))
            getToMainActivity();
        else{
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

    }

    public void annimationButton(){
        ImageButton button = findViewById(R.id.ConnectButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        button.startAnimation(myAnim);
    }

    public void auth(String image,String id){
        ComServerAuth auth = new ComServerAuth(this);
        try {
            auth.post(getString(R.string.lienAuth),image,id);
        } catch (IOException e) {
            Log.e("Requete post", "sheh");
        }
    }

    public void retourAuth(Response response){
        Log.e("TOKEN RETOUR",""+response.toString());
        if(response.code()!=200){
            alertDialogError();
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
        Intent intent = new Intent(this,Main_Activity.class);
        startActivity(intent);
    }
}
