package com.nodepad.pc.nodepad;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.SQLData;

public class SelectActivity extends Activity implements View.OnClickListener{

    private Button s_delete,s_back,s_save;
    private ImageButton s_share,s_facebook;
    private ImageView s_img;
    private TextView s_tv;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private RelativeLayout ll;
    private Button r, g, b;
    int colornum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //Choose landscape layout(select2.xml) or portrait layout(select.xml)
        int mCO=getResources().getConfiguration().orientation;
        super.onCreate(savedInstanceState);
        if(mCO== Configuration.ORIENTATION_PORTRAIT)
        setContentView(R.layout.select);
        if(mCO== Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.select2);
        s_delete=(Button)findViewById(R.id.s_delete);
        s_back=(Button)findViewById(R.id.s_back);
        s_img=(ImageView) findViewById(R.id.s_img);
        s_tv=(EditText) findViewById(R.id.s_tv);
        s_save=(Button)findViewById(R.id.s_save);
        s_share=(ImageButton)findViewById(R.id.s_share);
        s_facebook=(ImageButton)findViewById(R.id.s_facebook);

        ll = (RelativeLayout) findViewById(R.id.ll);
        r = (Button) findViewById(R.id.b1);
        g = (Button) findViewById(R.id.b2);
        b = (Button) findViewById(R.id.b3);
        r.setOnClickListener(this);
        g.setOnClickListener(this);
        b.setOnClickListener(this);
        ll.setOnClickListener(this);


        //****Set the background color according to the database.****//
        if(getIntent().getStringExtra(NotesDB.COLOR).toString().contentEquals("red")) {
            ll.setBackgroundColor(Color.RED);
        }
        if(getIntent().getStringExtra(NotesDB.COLOR).toString().contentEquals("blue")) {
            ll.setBackgroundColor(Color.BLUE);
        }
        if(getIntent().getStringExtra(NotesDB.COLOR).toString().contentEquals("green")) {
            ll.setBackgroundColor(Color.GREEN);
        }

        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();
        s_back.setOnClickListener(this);
        s_delete.setOnClickListener(this);
        s_save.setOnClickListener(this);
        s_share.setOnClickListener(this);
        s_facebook.setOnClickListener(this);

        //if(getIntent().getStringExtra(NotesDB.PATH).equals("null")){
         //   s_img.setVisibility(View.GONE);
        //}else{
           s_img.setVisibility(View.VISIBLE);
        //}

        //****Set the text and image to the layout from the database.****//
        s_tv.setText(getIntent().getStringExtra(NotesDB.CONTENT));
        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(NotesDB.PATH));
        s_img.setImageBitmap(bitmap);

    }

    //****delete the line in the database.****//
    public void delete(){
        dbWriter.delete(NotesDB.TABLE_NAME,"_id="+getIntent().getIntExtra(NotesDB.ID,0),null);
    }

    //****modify the line in the database.****//
    public void modify(){
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT,s_tv.getText().toString());
        dbWriter.update(NotesDB.TABLE_NAME, cv, "_id="+getIntent().getIntExtra(NotesDB.ID,0), null);
    }

    //****share the content by email****//
    public void share(){
        Log.d("You",getIntent().getStringExtra(NotesDB.COLOR));
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",".com", null));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "address");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(NotesDB.CONTENT));
        startActivity(Intent.createChooser(emailIntent, "Send Email..."));;
            //startActivity(Intent.createChooser(intent));
            //startActivity(intent);
    }

    //****share the content by other app.****//
    public void share2(){

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share this app");
        String shareMessage = "https://play.google.com/store";
        shareIntent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(NotesDB.CONTENT));
        startActivity(Intent.createChooser(shareIntent, "Choose the messenger to share this App"));
    }

    //****change the color in the database.****//
public void changecolor() {
    ContentValues cv = new ContentValues();
    if(colornum==1) {
        cv.put(NotesDB.COLOR, "red");
        dbWriter.update(NotesDB.TABLE_NAME, cv, "_id=" + getIntent().getIntExtra(NotesDB.ID, 0), null);
    }
    if(colornum==2) {
        cv.put(NotesDB.COLOR, "green");
        dbWriter.update(NotesDB.TABLE_NAME, cv, "_id=" + getIntent().getIntExtra(NotesDB.ID, 0), null);
    }
        if(colornum==3) {
            cv.put(NotesDB.COLOR, "blue");
            dbWriter.update(NotesDB.TABLE_NAME, cv, "_id=" + getIntent().getIntExtra(NotesDB.ID, 0), null);
        }//if(colornum==0)
    //cv.put(NotesDB.COLOR, "blue");
}


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.s_delete:
                delete();
                finish();
                break;


            case R.id.s_back:
                finish();
                break;

            case R.id.s_save:
                modify();
                finish();
                break;

            case R.id.s_share:
                share();
                break;

            case R.id.s_facebook:
                share2();
                break;

            case R.id.b1:
                ll.setBackgroundColor(Color.RED);
                colornum=1;
                changecolor();
                break;

            case R.id.b2:
                ll.setBackgroundColor(Color.GREEN);
                colornum=2;
                changecolor();
                break;
            case R.id.b3:
                ll.setBackgroundColor(Color.BLUE);
                colornum=3;
                changecolor();
                break;

        }
    }
}
