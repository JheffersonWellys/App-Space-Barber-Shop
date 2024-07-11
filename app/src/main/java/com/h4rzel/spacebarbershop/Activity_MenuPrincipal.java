package com.h4rzel.spacebarbershop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_MenuPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;
    private TextView textViewName, textViewEmail;

    private NavigationView navigationView;

    private Toolbar toolbar;

    private String TipoCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        IniciarMenuLateral();

        // Recupera dados do usuário
        RecuperarUsuario();

        // Configurar Toolbar personalizada
        toolbar = findViewById(R.id.T04_Tlbr_MenuPrincipal);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Agendamentos");

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.T04_NavVw_MenuLateral);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.T04_FrmLyt_Telas,
                    new Fragment_Atendimento()).commit();
            navigationView.setCheckedItem(R.id.MMN00_Itm_Agendamentos);
        }

    }

    private void RecuperarUsuario() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            textViewEmail.setText(userEmail);

            db.collection("usuarios")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                if (document.getString("tipo-cadastro") == "cliente"){
                                    textViewName.setText(document.getString("nome"));
                                }else{
                                    textViewName.setText(document.getString("razaosocial"));
                                }

                            } else {
                                // Nenhum documento encontrado com o email especificado
                            }
                        } else {
                            // Falha na busca dos documentos
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void IniciarMenuLateral(){

        navigationView = findViewById(R.id.T04_NavVw_MenuLateral);

        View headerView = navigationView.getHeaderView(0);

        textViewName = headerView.findViewById(R.id.MMN00_TxtVw_NomeUsuario);
        textViewEmail = headerView.findViewById(R.id.MMN00_TxtVw_EmailUsuario);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MMN00_Itm_Perfil:

                if(TipoCadastro == "cliente"){
                    toolbar.setTitle("Perfil Cliente");
                    getSupportFragmentManager().beginTransaction().replace(R.id.T04_FrmLyt_Telas,
                            new Fragment_PerfilCliente()).commit();
                }else{
                    toolbar.setTitle("Perfil Barbearia");
                    getSupportFragmentManager().beginTransaction().replace(R.id.T04_FrmLyt_Telas,
                            new Fragment_PerfilBarbearia()).commit();
                }

                // handle click
                break;
            case R.id.MMN00_Itm_Agendamentos:
                toolbar.setTitle("Agendamentos");
                getSupportFragmentManager().beginTransaction().replace(R.id.T04_FrmLyt_Telas,
                        new Fragment_Atendimento()).commit();
                // handle click
                break;
            case R.id.MMN00_Itm_Sobre:
                toolbar.setTitle("Sobre");
                // handle click
                break;
            case R.id.MMN00_Itm_Logout:
                // handle click
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}
