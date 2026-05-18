package br.edu.uece.model.jogador;

public class JogadorAzarado extends Jogador {

    public JogadorAzarado(String nome, String cor) {
        super(nome, cor);
    }

    @Override
    public int[] jogarDados() {
        int dado1, dado2, soma;

        // Loop de validação estrita: força a rolagem até que a soma seja menor ou igual a 6
        do {
            dado1 = rolarDado();
            dado2 = rolarDado();
            soma = dado1 + dado2;
        } while (soma > 6); // Se a soma for maior que 6, joga novamente

        return new int[]{dado1, dado2};
    }

    @Override
    public String getTipo() {
        return "Azarado";
    }
}