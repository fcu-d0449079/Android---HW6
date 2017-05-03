package com.example.zhuang.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button button2;
    ListView note;
    SQLiteDatabase db;
    ArrayList<String> titlelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(add);

        note = (ListView)findViewById(R.id.note);
        note.setOnItemClickListener(click);
        note.setOnItemLongClickListener(click1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DBOpenHelper openhelper = new DBOpenHelper(this);
        db = openhelper.getWritableDatabase();

        titlelist = NoteDB.getTitleList(db);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, titlelist);
        note.setAdapter(adapter);
    }

    OnClickListener add = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,
                    NoteEditor.class);
            intent.putExtra("NOTEPOS", -1);
            startActivity(intent);
        }
    };

    OnItemClickListener click = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View v,
                                int position, long id) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,
                    NoteEditor.class);
            intent.putExtra("NOTEPOS", position);
            startActivity(intent);
        }
    };
    OnItemLongClickListener click1 = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> av, View v,
                                       int position, long id) {
            String title = titlelist.get(position);
            NoteDB.delNote(db, title);
            titlelist = NoteDB.getTitleList(db);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (MainActivity.this,
                            android.R.layout.simple_list_item_1, titlelist);
            note.setAdapter(adapter);
            return false;
        }

    };
}
