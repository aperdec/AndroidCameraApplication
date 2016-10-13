package com.example.perds.cameraapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int TAKE_AVATAR_CAMERA_REQUEST = 1;
    private static final String SETTINGS_PREFS_AVATAR = "settingsPrefsAvatar";
    private static final String CAMERA_PREFS = "cameraPrefs";
    private ImageButton avatarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avatarButton = (ImageButton) findViewById(R.id.imageButton_avatar);
        initAvatar();

        SharedPreferences settings = getSharedPreferences(CAMERA_PREFS, MODE_PRIVATE);
        if (settings.contains(SETTINGS_PREFS_AVATAR)) {
            String avatarUri = settings.getString(SETTINGS_PREFS_AVATAR, "");

            if (avatarUri != null) {
                Uri imageUri = Uri.parse(avatarUri);
                avatarButton.setImageURI(imageUri);
            } else {
                avatarButton.setImageResource(R.drawable.avatar);
            }
        } else {
            avatarButton.setImageResource(R.drawable.avatar);
        }
    }

    private void initAvatar() {
        avatarButton.setOnClickListener(new ChooseCameraListener());
    }

    private class ChooseCameraListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(Intent.createChooser(pictureIntent, "Take a photo"), TAKE_AVATAR_CAMERA_REQUEST);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_AVATAR_CAMERA_REQUEST:
                if (requestCode == Activity.RESULT_CANCELED) {

                } else if (resultCode == Activity.RESULT_OK) {
                    Bitmap cameraPic = (Bitmap) data.getExtras().get("data");
                    if (cameraPic != null) {
                        try {
                            saveAvatar(cameraPic);
                        } catch (Exception e) {

                        }
                    }
                }
        }
    }

    private void saveAvatar(Bitmap avatar) {
        String avatarFilename = "avatar.jpg";

        try {
            avatar.compress(Bitmap.CompressFormat.JPEG, 100, openFileOutput(avatarFilename, MODE_PRIVATE));
        } catch (Exception e) {

        }

        Uri avatarUri = Uri.fromFile(new File(this.getFilesDir(), avatarFilename));

        avatarButton.setImageURI(avatarUri);

        SharedPreferences settings = getSharedPreferences(CAMERA_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SETTINGS_PREFS_AVATAR, avatarUri.getPath());
        editor.commit();
    }
}
