package com.h4rzel.spacebarbershop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.QuerySnapshot;


public class Activity_Login extends AppCompatActivity {

    private TextView T01_TxtVw_CriarConta, T01_TxtVw_RedefinirSenha;
    private EditText T01_EdtTx_Email, T01_EdtTx_Senha;
    private AppCompatButton T01_AppCmpBtn_Entrar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Switch T01_Swch_ClienteBarbearia;
    private ProgressBar T01_PrgBar_Loagind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar o Firebase
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar os componentes da tela
        IniciarComponentes();

        // Configura os clicks botões
        ConfigurarClicks();

    }

    private void ConfigurarClicks() {

        // Configura o evento de click para logar no aplicatrivo
        T01_AppCmpBtn_Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                T01_PrgBar_Loagind.setVisibility(View.VISIBLE);

                // Armazena os dados para verificar o login
                String email = T01_EdtTx_Email.getText().toString().trim();
                String password = T01_EdtTx_Senha.getText().toString().trim();
                boolean isBarbearia = T01_Swch_ClienteBarbearia.isChecked();

                // Verifica o preenchimentos dos campos
                if (!email.isEmpty() && !password.isEmpty()) {

                    // Chama função para tentar realizar o login
                    signInUser(email, password, isBarbearia);

                } else {

                    // Mostra mensagem de erro, caso tenha campos não preenchidos
                    Toast.makeText(Activity_Login.this, "Por favor, insira email e senha.",
                            Toast.LENGTH_SHORT).show();
                    T01_PrgBar_Loagind.setVisibility(View.GONE);
                }
            }
        });


        // Configura click para abrir tela de redefinir senha
        T01_TxtVw_RedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_RecuperarSenha.class);
                startActivity(intent);
                finish();
            }
        });

        // Configura click para abrir tela de criar conta
        T01_TxtVw_CriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_Cadastro.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void IniciarComponentes() {

        // Inicializa todos componentes manipulaveis da tela de login
        T01_TxtVw_CriarConta = findViewById(R.id.T01_TxtVw_CriarConta);
        T01_TxtVw_RedefinirSenha = findViewById(R.id.T01_TxtVw_RedefinirSenha);
        T01_EdtTx_Email = findViewById(R.id.T01_EdtTx_Email);
        T01_EdtTx_Senha = findViewById(R.id.T01_EdtTx_Senha);
        T01_AppCmpBtn_Entrar = findViewById(R.id.T01_AppCmpBtn_Entrar);
        T01_Swch_ClienteBarbearia = findViewById(R.id.T01_Swch_ClienteBarbearia);
        T01_PrgBar_Loagind = findViewById(R.id.T01_PrgBar_Loagind);

    }

    private void signInUser(String email, String password, boolean isBarbearia) {

        // Realiza o teste de login por emial e senha
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    // Analisa o resultado do teste de login
                    if (task.isSuccessful()) {

                        // Autenticação bem-sucedida, verificar o tipo de cadastro
                        validateUserType(email, isBarbearia);

                    } else {

                        // Se o login falhar, exibir uma mensagem ao usuário.
                        Toast.makeText(Activity_Login.this, "Autenticação falhou.",
                                Toast.LENGTH_SHORT).show();
                        T01_PrgBar_Loagind.setVisibility(View.GONE);

                    }

                });

    }

    private void validateUserType(String email, boolean isBarbearia) {

        db.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {

                    // Verifica se foi encontrado algum cadastro com o email informado
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {

                        // Analisa o documento que foi encontrado o email
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            // Recupera o tipo de cadastro do cliente
                            String tipoCadastro = document.getString("tipo-cadastro");
                            String nome;
                            if (tipoCadastro.equals("cliente")){
                                nome = document.getString("nome");
                            }else{
                                nome = document.getString("razaosocial");
                            }

                            // Compara o tipo de cadastro cadastrado com o ativado no login
                            if ((isBarbearia && "barbearia".equals(tipoCadastro)) ||
                                    (!isBarbearia && "cliente".equals(tipoCadastro))) {
                                Intent intent = new Intent(Activity_Login.this, Activity_MenuPrincipal.class);
                                intent.putExtra("NomeUsuario", nome);
                                intent.putExtra("Email", email);
                                intent.putExtra("TipoCadastro", tipoCadastro);
                                startActivity(intent);
                                finish();
                            } else {

                                // Tipo de cadastro não corresponde, mostra a mensagem de erro
                                Toast.makeText(Activity_Login.this, "Tipo de cadastro inválido.", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                T01_PrgBar_Loagind.setVisibility(View.GONE);
                            }
                        }
                    } else {

                        // Caso não tenha nenhum cadastro com o email informado
                        Toast.makeText(Activity_Login.this, "Usuário não encontrado.",
                                Toast.LENGTH_SHORT).show();
                        T01_PrgBar_Loagind.setVisibility(View.GONE);
                    }
                });
    }

}

