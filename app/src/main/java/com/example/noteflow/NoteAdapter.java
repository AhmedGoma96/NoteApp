package com.example.noteflow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
  List<Note> list=new ArrayList<>();
  OnItemClickListener onItemClickListener;

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card,parent,false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
final Note note=list.get(position);
holder.best.setText(note.getBest()+"");
holder.desc.setText(note.getDescription());
holder.title.setText(note.getName());
holder.time.setText(note.getTime());
holder.date.setText(note.getDate());
if(onItemClickListener!=null&&position!=-1){
  holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      onItemClickListener.onItemClick(note);
    }
  });
}

  }

  @Override
  public int getItemCount() {
    if(list==null)
    return 0;
    return list.size();
  }
  public void changeData(List<Note> list){
    this.list=list;
    notifyDataSetChanged();
  }
  public Note getNoteAtPos(int pos){
    return list.get(pos);
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView title,desc,best,time,date;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      title=itemView.findViewById(R.id.text_title);
      desc=itemView.findViewById(R.id.text_desc);
      best=itemView.findViewById(R.id.text_best);
      time=itemView.findViewById(R.id.time_card);
      date=itemView.findViewById(R.id.date_card);
    }
  }
  public interface OnItemClickListener{
    void onItemClick(Note note);
  }

}
