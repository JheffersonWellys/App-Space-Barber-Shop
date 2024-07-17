package com.h4rzel.spacebarbershop;

import android.content.Intent;
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

    private TextView textViewName, textViewEmail;

    private NavigationView navigationView;

    private Toolbar toolbar;

    private String TipoCadastro, NomeUsuario, Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        RecuperarDadosUsuario();

        IniciarMenuLateral();

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

    private void RecuperarDadosUsuario() {

        Intent intent = getIntent();

        if (intent.hasExtra("NomeUsuario")) {

            NomeUsuario = intent.getStringExtra("NomeUsuario");
            Email = intent.getStringExtra("Email");
            TipoCadastro = intent.getStringExtra("TipoCadastro");

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

        textViewName.setText(NomeUsuario);
        textViewEmail.setText(Email);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.MMN00_Itm_Perfil:
                if(TipoCadastro.equals("cliente")){
                    toolbar.setTitle("Perfil Cliente");
                    getSupportFragmentManager().beginTransaction().replace(R.id.T04_FrmLyt_Telas,
                            new Fragment_PerfilCliente()).commit();
                }else{
                    toolbar.setTitle("Perfil Barbearia");
                    getSupportFragmentManager().beginTransaction().replace(R.id.T04_FrmLyt_Telas,
                            new Fragment_PerfilBarbearia()).commit();
                }
                break;

            case R.id.MMN00_Itm_Agendamentos:
                toolbar.setTitle("Agendamentos");
                getSupportFragmentManager().beginTransaction().replace(R.id.T04_FrmLyt_Telas,
                        new Fragment_Atendimento()).commit();
                break;

            case R.id.MMN00_Itm_Sobre:
                toolbar.setTitle("Sobre");
                getSupportFragmentManager().beginTransaction().replace(R.id.T04_FrmLyt_Telas,
                        new Fragment_Sobre()).commit();
                break;

            case R.id.MMN00_Itm_Logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Activity_MenuPrincipal.this, Activity_Login.class);
                startActivity(intent);
                finish();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}
