package main.java.br.edu.uece.model.casa;

import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.jogo.Jogo;
import java.util.List;


public class CasaMagica extends Casa {

    public CasaMagica(int numero) {
        super(numero);
    }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        // Regra do PDF: Troca com o último colocado
        List<Jogador> jogadores = jogo.getJogadores();
        Jogador ultimo = j;

        for (Jogador p : jogadores) {
            if (p.getPosicao() < ultimo.getPosicao()) {
                ultimo = p;
            }
        }

        if (ultimo != j) {
            int posOriginal = j.getPosicao();
            j.setPosicao(ultimo.getPosicao());
            ultimo.setPosicao(posOriginal);
            System.out.println("Mágica! " + j.getCor() + " trocou com " + ultimo.getCor());
        }
    }

    @Override public String getTipo() { return "Mágica"; }
    @Override public String getDescricao() { return "Troque de lugar com o jogador mais atrás."; }
}