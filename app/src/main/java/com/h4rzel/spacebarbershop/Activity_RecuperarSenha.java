package com.h4rzel.spacebarbershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_RecuperarSenha extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText T03_EdtTxt_Email;
    private Button T03_Btn_Enviar;

    private TextView T03_TxtVw_FazerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        // Carregando a instancia do firebase
        mAuth = FirebaseAuth.getInstance();

        IniciarComponentes();
        ConfigurarClicks();

    }

    private void ConfigurarClicks() {

        T03_Btn_Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = T03_EdtTxt_Email.getText().toString().trim();
                if (email.isEmpty()){
                    Toast.makeText(Activity_RecuperarSenha.this,"Entre com seu e-mail",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Activity_RecuperarSenha.this,"E-mail de recuperação de senha enviado!",Toast.LENGTH_SHORT).show();
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(Activity_RecuperarSenha.this,"Error: " + error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        T03_TxtVw_FazerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Activity_RecuperarSenha.this, Activity_Login.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void IniciarComponentes() {

        T03_EdtTxt_Email = findViewById(R.id.T03_EdtTxt_Email);
        T03_Btn_Enviar = findViewById(R.id.T03_Btn_Enviar);
        T03_TxtVw_FazerLogin = findViewById(R.id.T03_TxtVw_FazerLogin);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Activity_RecuperarSenha.this, Activity_Login.class);
        startActivity(intent);
        finish();

    }
}