package com.h4rzel.spacebarbershop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.h4rzel.spacebarbershop.databinding.FragmentPerfilBarbeariaBinding;

import java.util.HashMap;
import java.util.Map;

public class Fragment_PerfilBarbearia extends Fragment {

    FragmentPerfilBarbeariaBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String endereco, cnpj, razaosocial,email, documentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilBarbeariaBinding.inflate(inflater, container, false);
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
        binding.T02EdtTxRazaoSocial.setText(razaosocial);
        binding.T02EdtTxCnpj.setText(cnpj);
        binding.T02EdtTxEndereco.setText(endereco);
    }

    private void updateUserProfile() {
        String razaosocial = binding.T02EdtTxRazaoSocial.getText().toString().trim();
        String endereco = binding.T02EdtTxEndereco.getText().toString().trim();
        String cnpj = binding.T02EdtTxCnpj.getText().toString().trim();


        if (TextUtils.isEmpty(razaosocial) || TextUtils.isEmpty(endereco) || TextUtils.isEmpty(cnpj)) {
            Toast.makeText(getActivity(), "Preencha todo os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = db.collection("usuarios").document(documentId);

            Map<String, Object> updates = new HashMap<>();
            updates.put("razaosocial", razaosocial);
            updates.put("endereco", endereco);
            updates.put("cnpj", cnpj);

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
                            razaosocial = document.getString("razaosocial");
                            cnpj = document.getString("cnpj");
                            endereco = document.getString("endereco");
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
