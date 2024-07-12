package com.h4rzel.spacebarbershop;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AgendamentoViewHolder extends RecyclerView.ViewHolder {
    TextView ITM_TxtVw_Cliente, ITM_TxtVw_corte,ITM_TxtVw_barbeiro, ITM_TxtVw_horario,ITM_TxtVw_data;

    public AgendamentoViewHolder(@NonNull View itemView) {
        super(itemView);
        ITM_TxtVw_Cliente = itemView.findViewById(R.id.ITM_TxtVw_Cliente);
        ITM_TxtVw_corte = itemView.findViewById(R.id.ITM_TxtVw_corte);
        ITM_TxtVw_barbeiro = itemView.findViewById(R.id.ITM_TxtVw_barbeiro);
        ITM_TxtVw_horario = itemView.findViewById(R.id.ITM_TxtVw_horario);
        ITM_TxtVw_data = itemView.findViewById(R.id.ITM_TxtVw_data);
    }
}
