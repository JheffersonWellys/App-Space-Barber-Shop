package com.h4rzel.spacebarbershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Activity_Agendamento extends AppCompatActivity {

    private CalendarView T05_ClnVw_Calendario;
    private RadioGroup T05_RadGrp_TurnoManha ,T05_RadGrp_TurnoTarde;
    private Spinner T05_Spne_TipoCorte , T05_Spne_Barbeiro;
    private AppCompatButton T05_AppCmpBtn_Agendar;
    private String Data , Hora , TipoDoCorte , Barbeiro, Cliente, Email;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);
        IniciarComponentes();
        ConfigurarCliques();
        IniciarFirebase();
        ConfigurarCalender();
        ConfigurarRadiuGroups();
        ConfigurarSpinners();

        // Recupera dados do usuário
        RecuperarUsuario();

    }

    private void RecuperarUsuario() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            Email = currentUser.getEmail();

            db.collection("usuarios")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                Cliente = document.getString("nome");
                            } else {
                                // Nenhum documento encontrado com o email especificado
                            }
                        } else {
                            // Falha na busca dos documentos
                        }
                    });
        }
    }

    private void ConfigurarCalender() {
        T05_ClnVw_Calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Data = dayOfMonth + "/"+ (month+1)+"/"+ year;
            }
        });
    }



    private void ConfigurarSpinners() {
        ArrayAdapter<CharSequence> adapterCorte = ArrayAdapter.createFromResource(this,R.array.TiposCortes, android.R.layout.simple_spinner_item);
        adapterCorte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        T05_Spne_TipoCorte.setAdapter(adapterCorte);
        T05_Spne_TipoCorte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoDoCorte = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapterBarbeiro = ArrayAdapter.createFromResource(this,R.array.Barbeiros, android.R.layout.simple_spinner_item);
        adapterBarbeiro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        T05_Spne_Barbeiro.setAdapter(adapterBarbeiro);
        T05_Spne_Barbeiro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Barbeiro = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void ConfigurarRadiuGroups() {
        RadioGroup.OnCheckedChangeListener radioGroupsListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null){
                    Hora = radioButton.getText().toString();
                    if (group == T05_RadGrp_TurnoManha){
                        T05_RadGrp_TurnoTarde.clearCheck();
                    }else if (group == T05_RadGrp_TurnoTarde){
                        T05_RadGrp_TurnoManha.clearCheck();
                    }
                }
            }
        };
        T05_RadGrp_TurnoManha.setOnCheckedChangeListener(radioGroupsListener);
        T05_RadGrp_TurnoTarde.setOnCheckedChangeListener(radioGroupsListener);
    }

    private void IniciarFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("agendamentos");
    }

    private void ConfigurarCliques() {
        T05_AppCmpBtn_Agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Data != null && Hora != null && TipoDoCorte != null && Barbeiro != null){
                    String id = databaseReference.push().getKey();
                    Agendamento agendamento = new Agendamento(Data , Hora , TipoDoCorte , Barbeiro, Cliente, Email);
                    if (id != null){
                        databaseReference.child(id).setValue(agendamento).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(Activity_Agendamento.this,"Agendamento realizado com sucesso!",Toast.LENGTH_SHORT).show();
                                T05_AppCmpBtn_Agendar.setEnabled(false);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                },1500);
                            }else{
                                Toast.makeText(Activity_Agendamento.this,"Erro ao realizar o agendamento!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }else{
                    Toast.makeText(Activity_Agendamento.this,"Por favor, preencha todos os campos!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void IniciarComponentes() {
        T05_AppCmpBtn_Agendar = findViewById(R.id.T05_AppCmpBtn_Agendar);
        T05_ClnVw_Calendario = findViewById(R.id.T05_ClnVw_Calendario);
        T05_RadGrp_TurnoManha = findViewById(R.id.T05_RadGrp_TurnoManha);
        T05_RadGrp_TurnoTarde = findViewById(R.id.T05_RadGrp_TurnoTarde);
        T05_Spne_Barbeiro = findViewById(R.id.T05_Spne_Barbeiro);
        T05_Spne_TipoCorte = findViewById(R.id.T05_Spne_TipoCorte);
    }

    public static class Agendamento{
        public String data;
        public String hora;
        public String tipoCorte;
        public String barbeiro;
        public String cliente;
        public String email;
        public Agendamento(){

        }
        public Agendamento(String data , String hora , String tipoCorte , String barbeiro, String Cliente, String Email){
            this.data = data;
            this.hora = hora;
            this.tipoCorte = tipoCorte;
            this.barbeiro = barbeiro;
            this.cliente = Cliente;
            this.email = Email;
        }
    }
}