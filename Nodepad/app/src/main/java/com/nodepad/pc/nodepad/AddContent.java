package com.nodepad.pc.nodepad;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//****AddContent.java is the class to add content to the note list.****//

public class AddContent extends Activity implements View.OnClickListener{

    private String val;
    private Button savebtn,deletebtn;
    private EditText editText;
    private ImageView c_img;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private File notefile;
    private String imagePath;
    private int identify;

    private Button r, g, b;
    private RelativeLayout ll;
    private int colornum=0;
    private int flager=0;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        //****Choose landscape layout(addcontent2.xml) or portrait layout(addcontent.xml)****//

        int mCO=getResources().getConfiguration().orientation;

        super.onCreate(savedInstanceState);
        if(mCO== Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.addcontent);
            flager=1;
        }
        if(mCO== Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.addcontent2);
            flager=2;
        }
        val = getIntent().getStringExtra("flag");
        savebtn=(Button)findViewById(R.id.save);
        deletebtn=(Button)findViewById(R.id.delete);
        editText =(EditText)findViewById(R.id.etext);
        c_img=(ImageView)findViewById(R.id.c_img);

        ll = (RelativeLayout) findViewById(R.id.ll);
        r = (Button) findViewById(R.id.b1);
        g = (Button) findViewById(R.id.b2);
        b = (Button) findViewById(R.id.b3);
        r.setOnClickListener(this);
        g.setOnClickListener(this);
        b.setOnClickListener(this);
        ll.setOnClickListener(this);

        savebtn.setOnClickListener(this);
        deletebtn.setOnClickListener(this);
        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

        initView();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    //****Choose create text note(for 1) or image and text note by album(for2) or by camera(for 3)****//
    public void initView(){
        if(val.equals("1")){
            c_img.setVisibility(View.GONE);
        }
        if(val.equals("2")){
                c_img.setVisibility(View.VISIBLE);
                Intent img = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                notefile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getTime() + ".jpg");
                img.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(notefile));
                startActivityForResult(img, 1);
             }
        if(val.equals("3")) {
                c_img.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);//打开相册
        }
    }
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.save:
                    addDB();
                    finish();
                    break;

                case R.id.delete:
                    finish();
                    break;


                //****Change background by click different buttons(R,G,B).****//
                case R.id.b1:
                    ll.setBackgroundColor(Color.RED);
                    colornum=1;
                    break;

                case R.id.b2:
                    ll.setBackgroundColor(Color.GREEN);
                    colornum=2;
                    break;
                case R.id.b3:
                    ll.setBackgroundColor(Color.BLUE);
                    colornum=3;
                    break;

            }
        }

        //****Add text, picture and its path, background color and create time to the database.****//
        public void addDB(){
            ContentValues cv = new ContentValues();
            cv.put(NotesDB.CONTENT, editText.getText().toString());
            cv.put(NotesDB.TIME,getTime());
            if(colornum==1)cv.put(NotesDB.COLOR,"red");
            if(colornum==2)cv.put(NotesDB.COLOR,"green");
            if(colornum==3)cv.put(NotesDB.COLOR,"blue");
            if(colornum==0)cv.put(NotesDB.COLOR,"white");
            if(identify==1)
            cv.put(NotesDB.PATH,notefile+"");
            else
            cv.put(NotesDB.PATH,imagePath);
            dbWriter.insert(NotesDB.TABLE_NAME,null,cv);
        }

        //****Get real time for creating different picture names.****//
        private String getTime(){
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date date = new Date();
            String str = format.format(date);
            return  str;
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            //set image to the layout from album
            Bitmap bitmap = BitmapFactory.decodeFile(notefile.getAbsolutePath());
            c_img.setImageBitmap(bitmap);
            identify=1;
        }
        if(requestCode == 2){
                //set image to the layout from camera
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                imagePath = c.getString(columnIndex);
                Log.d("Sister",imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            c_img.setImageBitmap(bitmap);
            identify=2;
        }
        }

}
