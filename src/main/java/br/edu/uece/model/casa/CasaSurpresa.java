package br.edu.uece.model.casa;

import br.edu.uece.jogo.Jogo;
import br.edu.uece.model.jogador.Jogador;
import java.util.Random;

public class CasaSurpresa extends Casa {
    public CasaSurpresa(int numero) { super(numero); }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        Random rand = new Random();
        int carta = rand.nextInt(3) + 1; // Sorteia 1, 2 ou 3
        
        // Avisa ao controlador do jogo para realizar a troca de classe/objeto
        jogo.transformarJogador(j, carta);
    }

    @Override
    public String getTipo() { return "Surpresa"; }

    @Override
    public String getDescricao() { return "Casa surpresa! Tire uma carta para trocar de tipo de jogador."; }
}