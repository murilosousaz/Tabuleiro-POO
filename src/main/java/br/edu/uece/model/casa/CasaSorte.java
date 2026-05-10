package main.java.br.edu.uece.model.casa;

import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.model.jogador.JogadorAzarado;
import main.java.br.edu.uece.jogo.Jogo;

public class CasaSorte extends Casa {

    // Este construtor permite que o Tabuleiro.java funcione
    public CasaSorte(int numero) {
        super(numero);
    }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        // Regra do PDF: Avança 3 casas se não for azarado
        if (!(j instanceof JogadorAzarado)) {
            j.setPosicao(j.getPosicao() + 3);
            System.out.println("Sorte! " + j.getCor() + " avançou para a casa " + j.getPosicao());
        } else {
            System.out.println("Jogadores azarados não ganham bônus na Casa da Sorte.");
        }
    }

    @Override public String getTipo() { return "Sorte"; }
    @Override public String getDescricao() { return "Avance 3 casas (exceto azarados)."; }

}