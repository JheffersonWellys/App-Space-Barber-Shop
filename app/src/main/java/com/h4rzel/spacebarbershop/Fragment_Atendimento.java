package com.h4rzel.spacebarbershop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Atendimento extends Fragment {
    FloatingActionButton FRAG00_FltBtn_Agendar;
    RecyclerView recyclerView;
    private AgendamentoAdapter adapter;
    private List<Agendamento> agendamentos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atendimento, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IniciarComponentes(view);
        ConfigurarClicks(view);
        ConfigurarRecyclerView(view);
        ConfigurarFireBase();
    }

    private void ConfigurarRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.FRAG00_RcyVw_Atendimentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        agendamentos = new ArrayList<>();
        adapter = new AgendamentoAdapter(agendamentos);
        recyclerView.setAdapter(adapter);
    }

    private void ConfigurarFireBase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("agendamentos");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                agendamentos.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Agendamento agendamento = dataSnapshot.getValue(Agendamento.class);
                    agendamentos.add(agendamento);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ConfigurarClicks(View view) {
        FRAG00_FltBtn_Agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Agendamento.class);
                startActivity(intent);
            }
        });
    }

    private void IniciarComponentes(View view) {
        FRAG00_FltBtn_Agendar = view.findViewById(R.id.FRAG00_FltBtn_Agendar);
    }

}