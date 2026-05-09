package br.edu.uece.model.casa;

import br.edu.uece.model.jogador.Jogador;
import br.edu.uece.model.jogador.JogadorAzarado;
import br.edu.uece.jogo.Jogo;

public class CasaSorte extends Casa {

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        // Regra: ande 3 casas para frente desde que não seja um jogador azarado 
        if (!(j instanceof JogadorAzarado)) {
            j.setPosicao(j.getPosicao() + 3);
            System.out.println("Casa da Sorte! O jogador " + j.getCor() + " avançou 3 casas.");
        } else {
            System.out.println("O jogador azarado não recebe o bônus da Casa da Sorte.");
        }
    }
}