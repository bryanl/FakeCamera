package com.osesm.apps.fakecamera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.*;

public class MainActivity extends Activity {

  final static int[] photos = {R.drawable.android_1, R.drawable.android_2, R.drawable.android_3};
  static int lastPhotoIndex = -1;

  private final static String TAG = "FakeCamera";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    prepareToSnapPicture();
    finish();
  }

  private void prepareToSnapPicture() {
    checkSdCard();
    Intent intent = getIntent();

    if (intent.getExtras() != null) {
      snapPicture(intent);
      setResult(RESULT_OK);
    } else {
      Log.i(TAG, "Unable to capture photo. Missing Intent Extras.");
      setResult(RESULT_CANCELED);
    }
  }

  private void checkSdCard() {
    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      Toast.makeText(this, "External SD card not mounted", Toast.LENGTH_LONG).show();
      Log.i(TAG, "External SD card not mounted");
    }
  }

  private void snapPicture(Intent intent) {
    try {
      this.copyFile(getPicturePath(intent));
      Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
      Log.i(TAG, "Can't copy photo");
      e.printStackTrace();
    }
  }

  private File getPicturePath(Intent intent) {
    Uri uri = (Uri) intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
    return new File(uri.getPath());
  }

  private void copyFile(File destination) throws IOException {
    InputStream in = getResources().openRawResource(getNextPhoto());
    OutputStream out = new FileOutputStream(destination);
    byte[] buffer = new byte[1024];
    int length;

    if (in != null) {
      Log.i(TAG, "Input photo stream is null");

      while ((length = in.read(buffer)) > 0) {
        out.write(buffer, 0, length);
      }

      in.close();
    }

    out.close();
  }

  private int getNextPhoto() {
    if (lastPhotoIndex == photos.length - 1) {
      lastPhotoIndex = -1;
    }

    return photos[++lastPhotoIndex];
  }
}


