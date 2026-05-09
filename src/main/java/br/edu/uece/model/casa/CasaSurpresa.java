package main.java.br.edu.uece.model.casa;

public class CasaSurpresa extends Casa {

    public CasaSurpresa(int numero) {
        super(numero);
    }

    @Override
    public String getTipo() {
        return "Surpresa";
    }

    @Override
    public String getDescricao() {
        return "Casa surpresa! Tire uma carta para trocar de tipo de jogador.";
    }

    @Override
    public String toString() {
        return String.format("Casa %d [Surpresa]", getNumero());
    }
}