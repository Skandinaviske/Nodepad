package com.nodepad.pc.nodepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity  implements View.OnClickListener {

    private Button textbtn,imgbtn,videobtn;
    private ListView lv;
    private Intent i;
    private Myadapter adapter;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //****Choose landscape layout(activity_main2.xml) or portrait layout(activity_main.xml)****//

        int mCO=getResources().getConfiguration().orientation;
        if(mCO== Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_main);
        if(mCO== Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_main2);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        lv =(ListView) findViewById(R.id.list);
        textbtn = (Button)findViewById(R.id.text);
        imgbtn = (Button)findViewById(R.id.img);
        textbtn.setOnClickListener(this);
        imgbtn.setOnClickListener(this);
        notesDB = new NotesDB(this);
        System.out.println("WIN CREATE");
        dbReader = notesDB.getReadableDatabase();

        //****Pass the values from database to SelectActivity.****//
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("Smmol",position+"");
                Cursor cursor = dbReader.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
                cursor.moveToPosition(position);
                Intent i = new Intent(MainActivity.this,SelectActivity.class);
                i.putExtra(NotesDB.ID,cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(NotesDB.CONTENT,cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(NotesDB.TIME,cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                i.putExtra(NotesDB.PATH,cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                i.putExtra(NotesDB.COLOR,cursor.getString(cursor.getColumnIndex(NotesDB.COLOR)));
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        i = new Intent(this,AddContent.class);

        //****Create text or image button to create new text note or new image note.****//

        switch(v.getId()){
            case R.id.text:
                i.putExtra("flag","1");
                startActivity(i);
                break;
            case R.id.img:
                ButtonDialogFragment buttonDialogFragment = new ButtonDialogFragment();
                buttonDialogFragment.show("Please Choose", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Choose camera and start activity.
                        Toast.makeText(MainActivity.this, "Click Camera!" + which, Toast.LENGTH_SHORT).show();
                        i.putExtra("flag","2");
                        startActivity(i);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Choose text and start activity.
                        Toast.makeText(MainActivity.this, "Click Album!" + which, Toast.LENGTH_SHORT).show();
                        i.putExtra("flag","3");
                        startActivity(i);
                    }
                }, getFragmentManager());

                break;

        }
    }

    //****Choose database.****//
    public void selectDB(){
        Cursor cursor = dbReader.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
        adapter = new Myadapter(this,cursor);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }




}
