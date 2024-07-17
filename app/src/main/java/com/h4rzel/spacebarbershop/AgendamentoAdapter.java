package com.h4rzel.spacebarbershop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoViewHolder>{
    private List<Agendamento> agendamentos ;

    public AgendamentoAdapter(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    @NonNull
    @Override
    public AgendamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atendimento,parent, false);

        return new AgendamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendamentoViewHolder holder, int position) {

        Agendamento agendamento = agendamentos.get(position);

        holder.ITM_TxtVw_barbeiro.setText(agendamento.getBarbeiro());
        holder.ITM_TxtVw_Cliente.setText(agendamento.getCliente());
        holder.ITM_TxtVw_corte.setText(agendamento.getTipoCorte());
        holder.ITM_TxtVw_data.setText(agendamento.getData());
        holder.ITM_TxtVw_horario.setText(agendamento.getHora());
        holder.ITM_ImgVw_DeletarAtendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Aqui você implementa a lógica para deletar o agendamento do Firebase
                // Exemplo de como deletar usando Realtime Database:
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                String agendamentoId = agendamento.getId(); // Supondo que cada agendamento tem um ID único

                // Acessar o nó do agendamento e remover
                databaseRef.child("agendamentos").child(agendamentoId).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Remoção bem sucedida, você pode implementar o que acontece após deletar
                                Toast.makeText(view.getContext(), "Agendamento deletado com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Falha ao deletar o agendamento
                                Toast.makeText(view.getContext(), "Erro ao deletar agendamento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount() {
        return agendamentos.size();
    }

}
