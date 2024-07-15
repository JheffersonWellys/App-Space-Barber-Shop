package com.h4rzel.spacebarbershop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoViewHolder>{
    private List<Agendamento>agendamentos;

    public AgendamentoAdapter(List<Agendamento> agendamentos) {
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
        holder.ITM_TxtVw_corte.setText(agendamento.getTipoCorte());
        holder.ITM_TxtVw_data.setText(agendamento.getData());
        holder.ITM_TxtVw_horario.setText(agendamento.getHora());
    }

    @Override
    public int getItemCount() {
        return agendamentos.size();
    }
}
