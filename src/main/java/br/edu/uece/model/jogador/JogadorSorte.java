package br.edu.uece.model.jogador;

public class JogadorSorte extends Jogador {

    public JogadorSorte(String nome, String cor) {
        super(nome, cor);
    }

    @Override
    public int[] jogarDados() {
        int dado1, dado2, soma;

        // Loop de validação estrita: força a rolagem até que a soma seja maior ou igual a 7
        do {
            dado1 = rolarDado();
            dado2 = rolarDado();
            soma = dado1 + dado2;
        } while (soma < 7); // Se a soma for menor que 7, joga novamente

        return new int[]{dado1, dado2};
    }

    @Override
    public String getTipo() {
        return "Sorte";
    }
}