package br.edu.uece.model.jogador;

public class JogadorAzarado extends Jogador {

    public JogadorAzarado(String nome, String cor) {
        super(nome, cor);
    }

    @Override
    public int[] jogarDados() {
        // Primeira tentativa
        int dado1_tentativa1 = rolarDado();
        int dado2_tentativa1 = rolarDado();
        int soma1 = dado1_tentativa1 + dado2_tentativa1;

        // Segunda tentativa
        int dado1_tentativa2 = rolarDado();
        int dado2_tentativa2 = rolarDado();
        int soma2 = dado1_tentativa2 + dado2_tentativa2;

        // Retorna a tentativa com menor soma (azar!)
        if (soma1 <= soma2) {
            return new int[]{dado1_tentativa1, dado2_tentativa1};
        } else {
            return new int[]{dado1_tentativa2, dado2_tentativa2};
        }
    }

    @Override
    public String getTipo() {
        return "Azarado";
    }
}