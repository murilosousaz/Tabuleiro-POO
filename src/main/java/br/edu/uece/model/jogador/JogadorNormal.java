package main.java.br.edu.uece.model.jogador;

public class JogadorNormal extends Jogador {

    public JogadorNormal(String nome, String cor) {
        super(nome, cor);
    }

    @Override
    public int[] jogarDados() {
        int dado1 = rolarDado();
        int dado2 = rolarDado();
        return new int[]{dado1, dado2};
    }

    @Override
    public String getTipo() {
        return "Normal";
    }
}