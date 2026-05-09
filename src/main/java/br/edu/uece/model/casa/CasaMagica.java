package br.edu.uece.model.casa;

import br.edu.uece.model.jogador.Jogador;
import br.edu.uece.jogo.Jogo;
import java.util.List;

public class CasaMagica extends Casa {

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        // Pegamos a lista de todos os competidores através da instância do jogo
        List<Jogador> jogadores = jogo.getJogadores();
        Jogador jogadorMaisAtras = j;

        // Algoritmo de busca: encontrar quem tem a menor posição no tabuleiro
        for (Jogador p : jogadores) {
            if (p.getPosicao() < jogadorMaisAtras.getPosicao()) {
                jogadorMaisAtras = p;
            }
        }

        // Regra: troca de lugar com o jogador que está mais atrás 
        // Se o próprio jogador for o último, ele não sai do lugar 
        if (jogadorMaisAtras != j) {
            int posicaoOriginal = j.getPosicao();
            j.setPosicao(jogadorMaisAtras.getPosicao());
            jogadorMaisAtras.setPosicao(posicaoOriginal);
            
            System.out.println("Magia! O jogador " + j.getCor() + 
                               " trocou de lugar com " + jogadorMaisAtras.getCor());
        } else {
            System.out.println("O jogador já está em último lugar, nada acontece.");
        }
    }
}