package com.example.ivan.cs330_projekat;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.sarita.projekat.helper.InputValidation;
import com.example.sarita.projekat.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView appCompatTextViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews(){
       nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

       textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
       textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

       textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
       textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

       appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        appCompatTextViewLinkRegister = (AppCompatTextView) findViewById(R.id.appCompatTextViewLinkRegister);
    }

    private void initListeners(){
        appCompatButtonLogin.setOnClickListener(this);
        appCompatTextViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects(){
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.appCompatTextViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }

    }

    private void verifyFromSQLite()
    {
        if(!inputValidation.isInputEditTextFilled(textInputEditTextEmail,textInputLayoutEmail, "Email address not entered!")){
            return;
        }

        if(!inputValidation.isInputEditTextEmail(textInputEditTextEmail,textInputLayoutEmail, "Email address not valid!")){
            return;
        }

        if(!inputValidation.isInputEditTextFilled(textInputEditTextPassword,textInputLayoutPassword, "Password not entered!")){
            return;
        }

        if(databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim(), textInputEditTextPassword.getText().toString().trim())){
            Intent mainIntent = new Intent(activity, MainActivity.class);
            mainIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(mainIntent);
            finish();
        }else{
            Snackbar.make(nestedScrollView, "Invalid email and password!", Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText(){
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
