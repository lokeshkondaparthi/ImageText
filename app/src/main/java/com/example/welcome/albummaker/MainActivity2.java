package com.example.welcome.albummaker;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    public static final String NEW_LINE_TEXT = "hello \n wonder full \n morning \n hello\n" +
            "hello \n" +
            " wonder full \n" +
            " morning \n" +
            " hello\n";
    public static final String CANVAS = "Canvas";
    private ImageView ivImage;
    private EditText eteMaater;
    private int YOUR_SELECT_PICTURE_REQUEST_CODE = 10;
    private Uri outputFileUri;
    private Uri selectedImageUri;
    private static String NEW_LINE_TEXT2="Is there a decent way to calculate the new y position? Adding a seemingly random number doesn't make me feel very comfortable";
    private String TAG="MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ivImage = (ImageView) findViewById(R.id.profile_image);
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageIntent();
            }
        });
        eteMaater = (EditText) findViewById(R.id.etMatter);
    }
    public void createImage(View view) {        // Button Click

    }
    private void openImageIntent() {
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = getUniqueImageFilename();
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);

    }
    private String getUniqueImageFilename() {
        return  "Lokesh"+System.currentTimeMillis();
    }

    public void saveImage(View view) {
        if (eteMaater.getText().toString().length() == 0) {
            Toast.makeText(MainActivity2.this, "Please enter photo descripion.", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity2.this, "Use semicolon for new line", Toast.LENGTH_SHORT).show();
        }
        String mNewLinesString  = mConvertToNewLineString(eteMaater.getText().toString());
        if (selectedImageUri != null) {
            Bitmap mSBitmap  = getBitmapFromUri(selectedImageUri);
            Toast.makeText(MainActivity2.this, mNewLinesString, Toast.LENGTH_SHORT).show();
            mAddTextToImage(mSBitmap,mNewLinesString);

        }
    }

    private void mAddTextToImage(Bitmap srcBitmap, String mNewLinesString) {

        Bitmap mDestBitmap = Bitmap.createBitmap(4000,6000,Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mDestBitmap);
        Paint mPaint = new Paint();
        mPaint.setTextSize(100);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Log.d("ManiActivity2", "mAddTextToImage: " + ivImage.getDrawingCache());
        Bitmap mIcLanucher  = BitmapFactory.decodeResource(getResources(), R.drawable.wed_bag);


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        // resize containerbitmap to canvas
        /*Bitmap mResizedBitmap = Bitmap.createBitmap(mIcLanucher, 0, 0,
                mDestBitmap.getWidth()-1, mDestBitmap.getHeight()-1);*/
       /* // Resize original bitmap
        Bitmap mOrgRszBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
               600 , 1000);*/

        Bitmap mResizedOrgScalBtmp = mGetResizedScalledImages(srcBitmap,"Selected");

        Bitmap mCanvasImage  = mGetResizedScalledImages(mIcLanucher,CANVAS);
        mCanvas.drawBitmap(mCanvasImage,0,0,null);        //for canvas
        mCanvas.drawBitmap(mResizedOrgScalBtmp, 1000, 400, null); // selected bitmap
        int x = 1000, y = 3500;
        for (String line: mNewLinesString.split("\n")) {
            mCanvas.drawText(line, x, y, mPaint);
            y += mPaint.descent() - mPaint.ascent();
        }

//        mCanvas.drawText(NEW_LINE_TEXT2, 300, 1000, mPaint);
        Calendar mCalendar = Calendar.getInstance();
        int mDay  = mCalendar.get(Calendar.DAY_OF_MONTH);
        int mMonth  = mCalendar.get(Calendar.MONTH);
        int mYear  = mCalendar.get(Calendar.YEAR);

        /* Set title*/
        Paint mTitlePaint = new Paint();
        mTitlePaint.setTextSize(200);
        mTitlePaint.setColor(Color.YELLOW);
        mCanvas.drawText("Ravindrachary Marriage bureau",500,150,mTitlePaint);

        /* Set date and time*/
        Paint mDT= new Paint();
        mDT.setTextSize(100);
        mDT.setColor(Color.CYAN);
        mCanvas.drawText("Date:" + mDay + "/ " + (mMonth + 1) + "/ " + mYear + ":" + mCalendar.get(Calendar.HOUR_OF_DAY)
                + ":" + mCalendar.get(Calendar.MINUTE) + ":" + mCalendar.get(Calendar.SECOND), 2700, 250, mDT);


        ivImage.setImageBitmap(mResizedOrgScalBtmp);
        try {
/*
           boolean isSaved=  mDestBitmap.compress(Bitmap.CompressFormat.JPEG,100,
                   new FileOutputStream(new File("Ravi_"+System.currentTimeMillis())));
*/
            /*boolean isSaved=  mDestBitmap.compress(Bitmap.CompressFormat.JPEG,100,
                   new FileOutputStream(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"Lokesh")));*/
            File mPictureFile = getOutputMediaFile();
            if (mPictureFile == null) {
                return;
            }
            FileOutputStream mFos = new FileOutputStream(mPictureFile);
            mDestBitmap.compress(Bitmap.CompressFormat.JPEG,100,mFos);
            try {
                mFos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity2.this, "Saved image", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity2.this, "Not saved image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap mGetResizedScalledImages(Bitmap mOrgRszBitmap,String mType) {
        int IMAGE_MAX_SIZE = 12000000;
        // resize to desired dimensions
        int height = mOrgRszBitmap.getHeight();
        int width = mOrgRszBitmap.getWidth();
        Log.d(TAG, "1th scale operation dimenions - width: " + width + ",height: " + height);

        double y = Math.sqrt(IMAGE_MAX_SIZE
                / (((double) width) / height));
        double x = (y / height) * width;
        Bitmap scaledBitmap = null;
        if (mType.equals(CANVAS)) {
            scaledBitmap = Bitmap.createScaledBitmap(mOrgRszBitmap, 4000,
                    6000, true);
        } else {
            Log.d(TAG, "mGetResizedScalledImages: x Y");
            scaledBitmap = Bitmap.createScaledBitmap(mOrgRszBitmap,mOrgRszBitmap.getWidth(),
                    mOrgRszBitmap.getHeight(), false);
        }
        return scaledBitmap;
    }

    private File getOutputMediaFile() {
        File mMediaStorageDir = new File(Environment.getExternalStorageDirectory()
                +"/Android/data/"
                +getApplicationContext().getPackageName()
                +"/Files");
        if (!mMediaStorageDir.exists()) {
            if (!mMediaStorageDir.mkdirs()) {
                return null;
            }
        } else {
            boolean isDeleted  = mMediaStorageDir.delete();
            if(isDeleted) Toast.makeText(MainActivity2.this, "Deleted!", Toast.LENGTH_SHORT).show();
        }

        File mMediaFile  = new File(mMediaStorageDir.getPath()+File.separator+"Ravi_"+System.currentTimeMillis() + ".jpg");

        return mMediaFile;
    }



    Bitmap getBitmapFromUri(Uri uri)
    {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String mConvertToNewLineString(String s) {
        return s.replace(";","\n");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                    Log.d("MainActivity","from camera"+outputFileUri);
                    ivImage.setImageURI(outputFileUri);
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImageUri,filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    ivImage.setImageURI(selectedImageUri);
                    Log.d("MainActivity2", "from gallery" + selectedImageUri);
                }
            }
        }
    }



}
