package com.example.truth_dare;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class DareActivity extends AppCompatActivity {

    private ArrayList<TruthItem> dareList;
    private Values values;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_values);

        sharedPreferences = getSharedPreferences("mySharedPreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dareList = new ArrayList<>();
        values = new Values();

        recyclerViewConfig();
        populateDefaultData();
        if(sharedPreferences.contains("UserDares"))
            populateUserData(sharedPreferences.getString("UserDares", null));

        // Check if this activity was started by an intent
        Intent intent = getIntent();
        if (intent.hasExtra("randomDare")) {
            String randomDare = getRandomDare();
            // Show the random dare in a dialog
            showDialogWithText(randomDare);
        }
    }

    public void populateDefaultData() {
        for(String dare : values.dares)
            dareList.add(new TruthItem(dare));
    }

    public void populateUserData(String jsonDares) {
        String[] dares = gson.fromJson(jsonDares, String[].class);
        for(int i=0; i<dares.length; i++)
            dareList.add(new TruthItem(dares[i]));
    }

    public void recyclerViewConfig() {
        // config for RV
        recyclerView = findViewById(R.id.recyclerView);

        //performance
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        adapter = new TruthAdapter(dareList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // setup buttons
        final EditText input = dialog.findViewById(R.id.editText);
        Button dismiss = dialog.findViewById(R.id.dismiss);
        Button add = dialog.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mText = input.getText().toString();
                if(mText.isEmpty())
                    Toast.makeText(getApplicationContext(), "Empty Text", Toast.LENGTH_LONG).show();
                else{
                    updateUserData(mText);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void updateUserData(String string) {
        ArrayList<String> textList = new ArrayList<>();

        if(sharedPreferences.contains("UserDares")) {
            String jsonDares = sharedPreferences.getString("UserDares", null);
            String[] dares = gson.fromJson(jsonDares, String[].class);
            for(int i=0; i<dares.length; i++)
                textList.add(dares[i]);
        }

        textList.add(string);
        editor.putString("UserDares", gson.toJson(textList));
        editor.apply();
        dareList.add(new TruthItem(string));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            showDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    public void onDareClicked(View view) {
        String randomDare = getRandomDare();
        showDialogWithText(randomDare);
    }

    private String getRandomDare() {
        return dareList.get(new Random().nextInt(dareList.size())).getText();
    }

    private void showDialogWithText(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
