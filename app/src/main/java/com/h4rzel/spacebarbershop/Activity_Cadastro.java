package com.h4rzel.spacebarbershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_Cadastro extends AppCompatActivity {

    String[] mensagens = {"Preencha todos os campos!", "Cadastro realizado!"};
    private EditText T02_EdtTx_Nome, T02_EdtTx_RazaoSocial, T02_EdtTx_Telefone, T02_EdtTx_Cnpj,
            T02_EdtTx_Cidade, T02_EdtTx_Endereco, T02_EdtTx_Email, T02_EdtTx_Senha;
    private String nome,razaosocial, cnpj, telefone, cidade, email, endereco, senha;
    private Button T02_AppCmpBtn_Entrar;
    private Switch T02_Swch_ClienteBarbearia;
    private ProgressBar T02_PrgBar_Loading;

    private String UserId;
    private TextView T02_TxtVw_FazerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        InicializaFirebase();

        IniciarComponentes();
        ConfigurarClicks();

    }

    private void InicializaFirebase() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null){
            UserId = currentUser.getUid();
        }

    }

    private void ConfigurarClicks() {

        T02_AppCmpBtn_Entrar.setOnClickListener(v -> {

            T02_PrgBar_Loading.setVisibility(View.VISIBLE);

            // Obter os valores dos campos de entrada
            nome = T02_EdtTx_Nome.getText().toString();
            email = T02_EdtTx_Email.getText().toString();
            senha = T02_EdtTx_Senha.getText().toString();
            telefone = T02_EdtTx_Telefone.getText().toString();
            cidade = T02_EdtTx_Cidade.getText().toString();
            razaosocial = T02_EdtTx_RazaoSocial.getText().toString();
            cnpj = T02_EdtTx_Cnpj.getText().toString();
            endereco = T02_EdtTx_Endereco.getText().toString();

            if (T02_Swch_ClienteBarbearia.isChecked()) {
                if (camposVazios(razaosocial, email, senha, cnpj, endereco)) {
                    mostrarSnackbar(v, mensagens[0], Color.WHITE, Color.BLACK);
                } else {
                    CadastrarUsuario(v);
                }
            } else {
                if (camposVazios(nome, email, senha, telefone, cidade)) {
                    mostrarSnackbar(v, mensagens[0], Color.WHITE, Color.BLACK);
                } else {
                    CadastrarUsuario(v);
                }
            }
        });

        T02_Swch_ClienteBarbearia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModoCadastro();
            }
        });

        T02_TxtVw_FazerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Cadastro.this, Activity_Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private boolean camposVazios(String... campos) {
        for (String campo : campos) {
            if (campo.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void CadastrarUsuario(View v) {

        String email = T02_EdtTx_Email.getText().toString();
        String senha = T02_EdtTx_Senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mostrarSnackbar(v, "Usuário cadastrado com sucesso!", Color.WHITE, Color.BLACK);
                        SalvarDadosUsuario();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                T02_PrgBar_Loading.setVisibility(View.GONE);
                                Intent intent = new Intent(Activity_Cadastro.this, Activity_Login.class);
                                startActivity(intent);
                                finish();
                            }
                        },500);
                    } else {
                        String erro = obterMensagemErro(task.getException());
                        mostrarSnackbar(v, erro, Color.WHITE, Color.BLACK);
                        T02_PrgBar_Loading.setVisibility(View.GONE);
                    }
                });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }

    private void mostrarSnackbar(View v, String mensagem, int backgroundColor, int textColor) {
        Snackbar snackbar = Snackbar.make(v, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(backgroundColor);
        snackbar.setTextColor(textColor);
        snackbar.show();
    }

    private String obterMensagemErro(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return "Digite uma senha com 6 caracteres!";
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return "Conta já cadastrada!";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "Email inválido!";
        } else {
            return "Erro ao cadastrar o usuário!";
        }
    }


    private void IniciarComponentes() {

        T02_EdtTx_Nome = findViewById(R.id.T02_EdtTx_Nome);
        T02_EdtTx_RazaoSocial = findViewById(R.id.T02_EdtTx_RazaoSocial);
        T02_EdtTx_Telefone = findViewById(R.id.T02_EdtTx_Telefone);
        T02_EdtTx_Cnpj = findViewById(R.id.T02_EdtTx_Cnpj);
        T02_EdtTx_Cidade = findViewById(R.id.T02_EdtTx_Cidade);
        T02_EdtTx_Endereco = findViewById(R.id.T02_EdtTx_Endereco);
        T02_EdtTx_Email = findViewById(R.id.T02_EdtTx_Email);
        T02_EdtTx_Senha = findViewById(R.id.T02_EdtTx_Senha);
        T02_AppCmpBtn_Entrar = findViewById(R.id.T02_AppCmpBtn_Entrar);
        T02_Swch_ClienteBarbearia = findViewById(R.id.T02_Swch_ClienteBarbearia);
        T02_PrgBar_Loading = findViewById(R.id.T02_PrgBar_Loading);
        T02_TxtVw_FazerLogin = findViewById(R.id.T02_TxtVw_FazerLogin);

    }

        private void SalvarDadosUsuario() {

             nome = T02_EdtTx_Nome.getText().toString();
             razaosocial = T02_EdtTx_RazaoSocial.getText().toString();
             telefone = T02_EdtTx_Telefone.getText().toString();
             cnpj = T02_EdtTx_Cnpj.getText().toString();
             cidade = T02_EdtTx_Cidade.getText().toString();
             endereco = T02_EdtTx_Endereco.getText().toString();
             email = T02_EdtTx_Email.getText().toString();

             if (T02_Swch_ClienteBarbearia.isChecked()) {
                 CriarCadastroBarbearia();
             } else {
                 CriarCadastroCliente();
             }

        }

    private void CriarCadastro(String tipoCadastro, Map<String, Object> dados) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dados.put("tipo-cadastro", tipoCadastro);

        db.collection("usuarios").add(dados).addOnSuccessListener(documentReference -> {
                    Log.d("TAG", tipoCadastro + " adicionado com id: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Erro ao adicionar " + tipoCadastro + "!", e);
                });
    }

    private void CriarCadastroCliente() {
        Map<String, Object> cliente = new HashMap<>();
        cliente.put("nome", nome);
        cliente.put("telefone", telefone);
        cliente.put("cidade", cidade);
        cliente.put("email", email);
        CriarCadastro("cliente", cliente);
    }

    private void CriarCadastroBarbearia() {
        Map<String, Object> barbearia = new HashMap<>();
        barbearia.put("razaosocial", razaosocial);
        barbearia.put("cnpj", cnpj);
        barbearia.put("endereco", endereco);
        barbearia.put("email", email);
        CriarCadastro("barbearia", barbearia);
    }


    private void ModoCadastro() {
        if (T02_Swch_ClienteBarbearia.isChecked()) {
            configurarVisibilidade(View.VISIBLE, View.GONE);
        } else {
            configurarVisibilidade(View.GONE, View.VISIBLE);
        }

        LimparComponentes();

    }

    private void LimparComponentes() {

        T02_EdtTx_RazaoSocial.setText("");
        T02_EdtTx_Nome.setText("");
        T02_EdtTx_Cidade.setText("");
        T02_EdtTx_Telefone.setText("");
        T02_EdtTx_Cnpj.setText("");
        T02_EdtTx_Endereco.setText("");
        T02_EdtTx_Email.setText("");
        T02_EdtTx_Senha.setText("");

    }

    private void configurarVisibilidade(int visibilidadeBarbearia, int visibilidadeCliente) {

        T02_EdtTx_RazaoSocial.setVisibility(visibilidadeBarbearia);
        T02_EdtTx_Nome.setVisibility(visibilidadeCliente);
        T02_EdtTx_Cidade.setVisibility(visibilidadeCliente);
        T02_EdtTx_Telefone.setVisibility(visibilidadeCliente);
        T02_EdtTx_Cnpj.setVisibility(visibilidadeBarbearia);
        T02_EdtTx_Endereco.setVisibility(visibilidadeBarbearia);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Activity_Cadastro.this, Activity_Login.class);
        startActivity(intent);
        finish();

    }
}

