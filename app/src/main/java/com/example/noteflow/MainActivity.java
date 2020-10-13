package com.example.noteflow;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
private NoteViewModel noteViewModel;
RecyclerView recyclerView;
NoteAdapter noteAdapter;
ImageButton insert_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("ltr"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        recyclerView=findViewById(R.id.recycler_view);
        insert_note=findViewById(R.id.btn_add_note);
        insert_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddNoteActivity.class);
                startActivityForResult(intent,1);

            }
        });
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        noteAdapter=new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //updateRecyclerView
                noteAdapter.changeData(notes);
            }
        });
       new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
           @Override
           public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
               return false;
           }

           @Override
           public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                 noteViewModel.deleteNote(noteAdapter.getNoteAtPos(viewHolder.getAdapterPosition()));
                 Toast.makeText(getApplicationContext(),"Note deleted",Toast.LENGTH_LONG).show();
           }
       }).attachToRecyclerView(recyclerView);
       noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(Note note) {
               Intent intent=new Intent(MainActivity.this,AddNoteActivity.class);
               intent.putExtra(AddNoteActivity.ID,note.getId());
               intent.putExtra(AddNoteActivity.TITLE,note.getName());
               intent.putExtra(AddNoteActivity.DESC,note.getDescription());
               intent.putExtra(AddNoteActivity.BEST,note.getBest());
               intent.putExtra(AddNoteActivity.TIME,note.getTime());
               intent.putExtra(AddNoteActivity.DATE,note.getDate());
               startActivityForResult(intent,2);
           }
       });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent outIntent) {
        super.onActivityResult(requestCode, resultCode, outIntent);
        if(requestCode==1&&resultCode==RESULT_OK){
            String title=outIntent.getStringExtra(AddNoteActivity.TITLE);
            String desc=outIntent.getStringExtra(AddNoteActivity.DESC);
            int best=outIntent.getIntExtra(AddNoteActivity.BEST,1);
            String time=outIntent.getStringExtra(AddNoteActivity.TIME);
            String date=outIntent.getStringExtra(AddNoteActivity.DATE);
            Note note=new Note(title,desc,best,time,date);
            noteViewModel.insertNote(note);
            Toast.makeText(getApplicationContext(),"Note Saves",Toast.LENGTH_LONG).show();
        }
        else if (requestCode==2&&resultCode==RESULT_OK){
           int id=outIntent.getIntExtra(AddNoteActivity.ID,-1);
           if(id==-1)
           {
               Toast.makeText(getApplicationContext(),"Note DonT Updated",Toast.LENGTH_LONG).show();
               return;
           }
            String title=outIntent.getStringExtra(AddNoteActivity.TITLE);
            String desc=outIntent.getStringExtra(AddNoteActivity.DESC);
            int best=outIntent.getIntExtra(AddNoteActivity.BEST,1);
            String time=outIntent.getStringExtra(AddNoteActivity.TIME);
            String date=outIntent.getStringExtra(AddNoteActivity.DATE);

            Note note=new Note(title,desc,best,time,date);
            note.setId(id);
            noteViewModel.updateNote(note);

        }
        else
            {
        Toast.makeText(getApplicationContext(),"Note not Saved",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.delete:
                noteViewModel.deleteAllNotes();
                Toast.makeText(getApplicationContext(),"All Notes Deleted",Toast.LENGTH_LONG).show();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
