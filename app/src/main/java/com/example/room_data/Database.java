package com.example.room_data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.xml.sax.DTDHandler;

@androidx.room.Database(entities = {Note.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instence;

    public abstract NoteDao noteDao();

    public static synchronized Database getInstance(Context context) {
        if(instence == null) {
            instence = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instence;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() { // to load data in initialization of Activity on OnCreate Method

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDBAsyncTask(instence).execute();
        }
    };

    private static class populateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private populateDBAsyncTask(Database db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Nandakumar", "Graduated fresher",1));
            return null;
        }
    }
}
