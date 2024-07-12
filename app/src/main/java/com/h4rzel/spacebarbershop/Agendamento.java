package com.h4rzel.spacebarbershop;

public class Agendamento {
    private String barbeiro, tipoCorte, data, hora;

    public Agendamento(){

    }
    public Agendamento (String barbeiro, String data, String hora,String tipoCorte){
        this.barbeiro = barbeiro;
        this.data   = data;
        this.hora = hora;
        this.tipoCorte = tipoCorte;

    }
    public String getBarbeiro(){
        return barbeiro;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getTipoCorte() {
        return tipoCorte;
    }

    public void setBarbeiro(String barbeiro) {
        this.barbeiro = barbeiro;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setTipoCorte(String tipoCorte) {
        this.tipoCorte = tipoCorte;
    }

}

