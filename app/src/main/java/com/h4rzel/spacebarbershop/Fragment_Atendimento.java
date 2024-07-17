package com.h4rzel.spacebarbershop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;

public class Fragment_Atendimento extends Fragment {
    private FloatingActionButton FRAG00_FltBtn_Agendar;
    private RecyclerView recyclerView;
    private AgendamentoAdapter adapter;
    private List<Agendamento> agendamentos;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    String Email, TipoCadastro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_atendimento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IniciarComponentes(view);
        ConfigurarClicks();
        ConfigurarRecyclerView(view);
        ConfigurarFireBase();
        RecuperarUsuario();
    }

    @Override
    public void onResume() {
        super.onResume();
        RecuperarUsuario();
    }

    private void CarregarDados() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                agendamentos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String agendamentoId = dataSnapshot.getKey();
                    Agendamento agendamento = dataSnapshot.getValue(Agendamento.class);

                    if (TipoCadastro != null && TipoCadastro.equals("cliente")) {
                        if (agendamento.getEmail().equals(Email)) {
                            agendamento.setId(agendamentoId);
                            agendamentos.add(agendamento);
                        }
                    } else {
                        if (agendamento != null) {
                            agendamento.setId(agendamentoId);
                            agendamentos.add(agendamento);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void ConfigurarRecyclerView(View view) {
        agendamentos = new ArrayList<>();
        recyclerView = view.findViewById(R.id.FRAG00_RcyVw_Atendimentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgendamentoAdapter(agendamentos);
        recyclerView.setAdapter(adapter);
    }

    private void ConfigurarFireBase() {
        ref = database.getReference("agendamentos");
    }

    private void ConfigurarClicks() {
        FRAG00_FltBtn_Agendar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Activity_Agendamento.class);
            startActivity(intent);
        });
    }

    private void IniciarComponentes(View view) {
        FRAG00_FltBtn_Agendar = view.findViewById(R.id.FRAG00_FltBtn_Agendar);
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
                                TipoCadastro = document.getString("tipo-cadastro");
                                // Agora que TipoCadastro foi recuperado, carregar os dados
                                CarregarDados();
                            } else {
                                // Nenhum documento encontrado com o email especificado
                            }
                        } else {
                            // Falha na busca dos documentos
                        }
                    });
        }
    }
}
