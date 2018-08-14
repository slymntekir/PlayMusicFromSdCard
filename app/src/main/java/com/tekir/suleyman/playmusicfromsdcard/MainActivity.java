package com.tekir.suleyman.playmusicfromsdcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvPlayList;
    Spinner sp_hafiza;
    String[] items;
    String[] sanatcilar = {"orhan","ferdi","ibrahim","müslüm"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvPlayList = findViewById(R.id.lvPlayList);
        sp_hafiza = findViewById(R.id.sp_hafiza);

    }

    private ArrayList<File> findSongs(File root)
    {
        ArrayList<File> a1 = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File f : files)
        {
            if(f.isDirectory() && !f.isHidden())
            {
                a1.addAll(findSongs(f));
            }
            else
            {
                for(String sanatci : sanatcilar)
                {
                    if( f.getName().endsWith(".mp3") && f.getName().toLowerCase().contains(sanatci))
                    {
                        a1.add(f);
                    }
                }
            }
        }
        return a1;
    }

    public void toast(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    public void cek(View view) {
        String hafiza = sp_hafiza.getSelectedItem().toString();
        TextView tx_path = findViewById(R.id.tx_path);

        try {
            ArrayList<File> songs = new ArrayList<>();

            if(hafiza.equals("telefon")) {
                songs = findSongs(Environment.getExternalStorageDirectory());
                tx_path.setText(Environment.getExternalStorageDirectory().toString());
            }
            else if(hafiza.equals("sd card"))
            {
                File f = new File("/storage/2D10-C310");
                tx_path.setText(f.toString());
                songs = findSongs(f);
            }
            else
            {
                songs = findSongs(Environment.getExternalStorageDirectory());
                songs.addAll(findSongs(new File("/storage/2D10-C310/")));
                tx_path.setText(Environment.getExternalStorageDirectory().toString()+" , /storage/2D10-C310/");
            }

            items = new String[songs.size()];

            for (int i=0;i<songs.size();i++) {
                //toast(songs.get(i).getName().toString());
                items[i] = songs.get(i).getName().toString();
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(
                    getApplicationContext(),R.layout.song_layout,R.id.textView,items);
            lvPlayList.setAdapter(adp);
            final ArrayList<File> finalSongs = songs;
            lvPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).
                            putExtra("songlist", finalSongs));
                }
            });
        }catch (Exception e)
        {
            toast(e.getMessage());
        }
    }
}