package com.example.noteflow;
import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
public class NoteViewModel extends ViewModel {
    private static NoteRepository noteRepository;
    private static LiveData<List<Note>> allNotes;
   /* public NoteViewModel(@NonNull Application application) {
       // super(application);
       // noteRepository=new NoteRepository(application);
       //allNotes= noteRepository.getAllNotes();
    }*/
    public static void init(Application application){
        noteRepository=new NoteRepository(application);
        allNotes= noteRepository.getAllNotes();

    }

   
    public void insertNote(Note note){
        noteRepository.insert(note);
    }

    public void updateNote(Note note){
        noteRepository.update(note);
    }
    public void deleteNote(Note note){
        noteRepository.delete(note);
    }
    public void deleteAllNotes(){
        noteRepository.deleteAllNotes();
    }
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

}
