package com.h4rzel.spacebarbershop;

public class Agendamento {
    private String barbeiro, tipoCorte, data, hora, cliente;

    public Agendamento() {

    }

    public Agendamento(String barbeiro, String data, String hora, String tipoCorte, String Cliente, String id) {
        this.barbeiro = barbeiro;
        this.data = data;
        this.hora = hora;
        this.tipoCorte = tipoCorte;
        this.cliente = Cliente;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(String barbeiro) {
        this.barbeiro = barbeiro;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipoCorte() {
        return tipoCorte;
    }

    public void setTipoCorte(String tipoCorte) {
        this.tipoCorte = tipoCorte;
    }

}

