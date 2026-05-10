package main.java.br.edu.uece.jogo;

import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.model.casa.*;

import java.util.ArrayList;
import java.util.List;

public class Jogo {

    private List<Jogador> jogadores;
    private Tabuleiro tabuleiro;
    private int indiceJogadorAtual;
    private boolean jogoFinalizado;

    public Jogo(List<Jogador> jogadores) {
        if (jogadores == null || jogadores.size() < 2) {
            throw new IllegalArgumentException("O jogo precisa ter pelo menos 2 jogadores!");
        }

        if (jogadores.size() > 6) {
            throw new IllegalArgumentException("O jogo permite no máximo 6 jogadores!");
        }

        this.jogadores = new ArrayList<>(jogadores);
        this.tabuleiro = new Tabuleiro();
        this.indiceJogadorAtual = 0;
        this.jogoFinalizado = false;

        // Inicializar todos os jogadores na casa 0
        for (Jogador jogador : this.jogadores) {
            jogador.mover(0);
        }
    }

    public Jogador getJogadorAtual() {
        return jogadores.get(indiceJogadorAtual);
    }

    public void proximoJogador() {
        indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
    }

    public List<Jogador> getJogadores() {
        return new ArrayList<>(jogadores);
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public boolean isJogoFinalizado() {
        return jogoFinalizado;
    }

    public void finalizarJogo() {
        this.jogoFinalizado = true;
    }

    public int getNumeroJogadores() {
        return jogadores.size();
    }

    public Jogador getVencedor() {
        for (Jogador jogador : jogadores) {
            if (jogador.getPosicao() >= 40) {
                return jogador;
            }
        }
        return null;
    }

    public int getIndiceJogadorAtual() {
        return indiceJogadorAtual;
    }

    public void reiniciar() {
        for (Jogador jogador : jogadores) {
            jogador.mover(0);
            jogador.resetarJogadas();
            jogador.setPerderVez(false);
        }
        indiceJogadorAtual = 0;
        jogoFinalizado = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Estado do Jogo ===\n");
        sb.append("Jogador atual: ").append(getJogadorAtual().getNome()).append("\n");
        sb.append("Jogadores:\n");

        for (int i = 0; i < jogadores.size(); i++) {
            Jogador j = jogadores.get(i);
            sb.append(String.format("  %d) %s - Casa %d - %d jogadas%s\n",
                    i + 1,
                    j.getNome(),
                    j.getPosicao(),
                    j.getNumeroJogadas(),
                    (i == indiceJogadorAtual) ? " <-- ATUAL" : ""));
        }

        return sb.toString();
    }

    public void solicitarEscolhaParaVoltarInicio(Jogador j) {
    }
}