package com.example.room_data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class NoteAddEditActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE =
            "com.example.room_data.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.room_data.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.example.room_data.EXTRA_PRIORITY";
    public static final String EXTRA_ID =
            "com.example.room_data.EXTRA_ID";

    private EditText tileEditText;
    private EditText descriptionEditText;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        tileEditText = findViewById(R.id.titleEdittext);
        descriptionEditText = findViewById(R.id.descriptionEdittext);
        numberPickerPriority = findViewById(R.id.NumberPicker_prority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            tileEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            descriptionEditText.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add Note");
        }
    }

    private void saveNote() {
        String title = tileEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Fields cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        if(id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();

    }

    @Override // to inflate and create the menu in activity
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
