package com.example.bathroommanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubmitBathroom extends AppCompatActivity {


    private final int COOLDOWN_TIME = 10_000;



    Button submit;
    EditText name;
    Spinner bathroomDropdown;
    // ^^ Everything in the UI, self explanatory

    String fullName, selectedBathroom;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference =  database.getReference();
    // ^^ Firebase setup, very simple

    SimpleDateFormat dateSdf = new SimpleDateFormat("MM.dd.yyyy");
    SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss");
    // ^^ Date formats for time and dates, these are separate columns in the sheets

    String[] bathrooms = new String[] {"Boys Floor 1", "Boys Floor 2", "Boys Floor 3", "Boys B-Wing", "Girls Floor 1", "Girls Floor 2", "Girls Floor 3", "Girls B-Wing", "Nurse's Office", "Gym Bathroom"};
    // List of everything in spinner

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bathroom);

        submit = findViewById(R.id.submit);
        name = findViewById(R.id.name);
        bathroomDropdown = findViewById(R.id.spinner);
        // ^ Get UI elements

        name.setText(getName());


        if (getTime() == null)
            logTime(String.valueOf(System.currentTimeMillis() - COOLDOWN_TIME));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.bathroomspinner, bathrooms);
        bathroomDropdown.setAdapter(adapter);


        submit.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN){
                            submit.animate().scaleX(1.1f).setDuration(100);
                            submit.animate().scaleY(1.1f).setDuration(100);
                        }
                        else if (event.getAction() == MotionEvent.ACTION_UP){
                            submit.animate().scaleX(1f).setDuration(100);
                            submit.animate().scaleY(1f).setDuration(100);
                            if ( !checkCooldown()) return false;
                            try {
                                fullName = name.getText().toString();
                                selectedBathroom = bathroomDropdown.getSelectedItem().toString();
                            }
                            catch (Exception e) {
                                return false;
                            }
                            logName(fullName);
                            String id = reference.push().getKey();
                            reference = database.getReference(id);
                            reference.child("Name").setValue(name.getText().toString());
                            reference.child("Bathroom").setValue(selectedBathroom);
                            reference.child("Date").setValue(dateSdf.format(new Date()));
                            reference.child("Time").setValue(timeSdf.format(new Date()));
                            if (checkCooldown()) {
                                logTime(String.valueOf(System.currentTimeMillis()));
                            }
                            Intent intent = new Intent(SubmitBathroom.this, MainActivity.class);
                            startActivity(intent);

                        }
                        return false;
                    }
                }
        );


    }
// vvv Tyler's code vvv

    public void logName(String text)
    {
        FileOutputStream fos = null;
        try
        {
            fos = openFileOutput("name.log", MODE_PRIVATE);
            fos.write(text.getBytes());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public String getName()
    {
        FileInputStream fis = null;
        try
        {
            fis = openFileInput("name.log");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            return br.readLine();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    public boolean checkCooldown()
    {
        return getTime() !=  null && Long.valueOf(getTime()) + COOLDOWN_TIME < System.currentTimeMillis();
    }


    public void logTime(String text)
    {
        FileOutputStream fos = null;
        try
        {
            fos = openFileOutput("time.log", MODE_PRIVATE);
            fos.write(text.getBytes());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public String getTime()
    {
        FileInputStream fis = null;
        try
        {
            fis = openFileInput("time.log");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            return br.readLine();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
