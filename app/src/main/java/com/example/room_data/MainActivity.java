package com.example.room_data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.addButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteAddEditActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // update RecyclerView
                adapter.setNotes(notes);

               // Toast.makeText(MainActivity.this, "OnChange", Toast.LENGTH_SHORT).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,  // To create swipe effect and swipe operation
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));

                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListner(new NoteAdapter.OnItemClickListner() {
            @Override
            public void onitemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, NoteAddEditActivity.class);
                intent.putExtra(NoteAddEditActivity.EXTRA_ID, note.getId());
                intent.putExtra(NoteAddEditActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(NoteAddEditActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(NoteAddEditActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(NoteAddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(NoteAddEditActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(NoteAddEditActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(NoteAddEditActivity.EXTRA_ID, -1);

            if(id == -1) {
                Toast.makeText(this, "Data can't Update!", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(NoteAddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(NoteAddEditActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(NoteAddEditActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                noteViewModel.deleteAll();
                Toast.makeText(this, "All Notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

