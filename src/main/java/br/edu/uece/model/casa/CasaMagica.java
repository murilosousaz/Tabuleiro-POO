package main.java.br.edu.uece.model.casa;

public class CasaMagica extends Casa {

    public CasaMagica(int numero) {
        super(numero);
    }

    @Override
    public String getTipo() {
        return "Mágica";
    }

    @Override
    public String getDescricao() {
        return "Casa Mágica! Troque de posição com o jogador que está em último lugar.";
    }

    @Override
    public String toString() {
        return String.format("Casa %d [Mágica ⇄]", getNumero());
    }
}