package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

   private TextView Username, Password;
   private Button login;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Username = findViewById(R.id.UserName);
        Password = findViewById(R.id.Password);
        login = findViewById(R.id.Save);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if ((Username.getText().toString().length() < 4) || (Password.getText().toString().length() < 4)){
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("Username",Username.getText().toString());
            intent.putExtra("Password",Password.getText().toString());
            startActivity(intent);
        }
    }
}