package com.example.noteflow;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {
    public static final String ID="ID";
    public static final String TITLE="TITLE";
    public static final String  DESC="DESC";
    public static final String BEST="BEST";
    public static final String TIME="TIME";
    public static final String DATE="DATE";
  private EditText title,desc;
  private TextView best;
  private NumberPicker numberPicker;
  private TextView time,date;
    int hours=1;
    int minutes=1;
    int months=1;
    int years=1;
    int days=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("ltr"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        title=findViewById(R.id.edit_text_title);
        desc=findViewById(R.id.edit_text_description);
        numberPicker=findViewById(R.id.number_picker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        time=findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(AddNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        final String stime = hourOfDay+":"+minute;
                        hours=hourOfDay;
                        minutes=minute;

                        try {
                            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                            final Date dateObj = sdf.parse(stime);
                           time.setText(new SimpleDateFormat("K:mm").format(dateObj));

                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }

                             /* hours=hourOfDay;
                              minutes=minute;
                              time.setText(hourOfDay+":"+minute);*/
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });
        date=findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(AddNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    Calendar calendar=Calendar.getInstance();
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        years=year;
                        months=month;
                        days=dayOfMonth;
                        month+=1;
                              date.setText(year+":"+month+":"+dayOfMonth);
                    }
                },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });




        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent outIntent =getIntent();
        if(outIntent.hasExtra(ID)){
            setTitle("Edit Note");
            title.setText(outIntent.getStringExtra(TITLE));
            desc.setText(outIntent.getStringExtra(DESC));
            numberPicker.setValue(outIntent.getIntExtra(BEST,1));
            time.setText(outIntent.getStringExtra(TIME));
            date.setText(outIntent.getStringExtra(DATE));
            Log.e("edit","information");
        }
        else {
            setTitle("Add Note");
            Log.e("add","information");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    
    }

    private void saveNote() {
        String stitle=title.getText().toString();
        String sdesc=desc.getText().toString();
        int best=numberPicker.getValue();
        String stime=time.getText().toString();
        String sdate=date.getText().toString();



        if(stitle.trim().isEmpty()||sdesc.trim().isEmpty()||stime.trim().isEmpty()||sdate.trim().isEmpty()){

            Toast.makeText(this,"please Enter title,desc,time and date",Toast.LENGTH_LONG).show();
            return;
        }
        Intent data=new Intent();
        data.putExtra(TITLE,stitle);
        data.putExtra(DESC,sdesc);
        data.putExtra(BEST,best);
        data.putExtra(TIME,stime);
        data.putExtra(DATE,sdate);

        int id=getIntent().getIntExtra(ID,-1);
        if(id!=-1){
            data.putExtra(ID,id);
        }
        makeAlarm(stitle,sdesc,best,stime,sdate);
        setResult(RESULT_OK,data);


        finish();


    }


   public void makeAlarm(String title,String desc,int best, String time,String date){
       AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
       Calendar calendar=Calendar.getInstance();
       calendar.set(Calendar.MONTH,months);
       calendar.set(Calendar.YEAR,years);
       calendar.set(Calendar.DAY_OF_MONTH, days);
       calendar.set(Calendar.HOUR_OF_DAY,hours);
       calendar.set(Calendar.MINUTE,minutes);
       Intent intent=new Intent(AddNoteActivity.this,MyReceiver.class);
      intent.putExtra(TITLE,title);
      intent.putExtra(DESC,desc);
      intent.putExtra(BEST,best);
      intent.putExtra(TIME,time);
      intent.putExtra(DATE,date);
       //toReachBroadCastReceiver
       PendingIntent pendingIntent =PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
       alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
   }


}
