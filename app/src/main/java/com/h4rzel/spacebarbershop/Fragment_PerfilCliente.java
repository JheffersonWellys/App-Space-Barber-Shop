package com.h4rzel.spacebarbershop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.h4rzel.spacebarbershop.databinding.FragmentPerfilBarbeariaBinding;
import com.h4rzel.spacebarbershop.databinding.FragmentPerfilClienteBinding;

import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Fragment_PerfilCliente extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String cidade, nome, telefone, email, documentId;
    private EditText T02_EdtTx_Nome, T02_EdtTx_Telefone, T02_EdtTx_Cidade;
    private FragmentPerfilClienteBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPerfilClienteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        FirebaseApp.initializeApp(getContext());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        RecuperarDados(view); // Passa a View para RecuperarDados

        binding.T02AppCmpBtnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });

        return view;
    }

    private void SetarDadosTextView(View view) {
        T02_EdtTx_Nome = view.findViewById(R.id.T02_EdtTx_Nome);
        T02_EdtTx_Nome.setText(nome);
        T02_EdtTx_Telefone = view.findViewById(R.id.T02_EdtTx_Telefone);
        T02_EdtTx_Telefone.setText(telefone);
        T02_EdtTx_Cidade = view.findViewById(R.id.T02_EdtTx_Cidade);
        T02_EdtTx_Cidade.setText(cidade);
    }

    private void updateUserProfile() {
        String nome = binding.T02EdtTxNome.getText().toString().trim();
        String cidade = binding.T02EdtTxCidade.getText().toString().trim();
        String telefone = binding.T02EdtTxTelefone.getText().toString().trim();


        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(cidade) || TextUtils.isEmpty(telefone)) {
            Toast.makeText(getActivity(), "Preencha todo os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = db.collection("usuarios").document(documentId);

            Map<String, Object> updates = new HashMap<>();
            updates.put("nome", nome);
            updates.put("telefone", telefone);
            updates.put("cidade", cidade);

            userRef.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Erro ao atualizar dados", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void RecuperarDados(View view) {
        db.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            cidade = document.getString("cidade");
                            nome = document.getString("nome");
                            telefone = document.getString("telefone");
                            documentId = document.getId();
                        }
                        // Chama SetarDadosTextView após os dados serem recuperados
                        SetarDadosTextView(view);
                    } else {
                        Toast.makeText(getContext(), "Falha ao recuperar dados", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
