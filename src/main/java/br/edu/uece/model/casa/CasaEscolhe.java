package main.java.br.edu.uece.model.casa;

import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.jogo.Jogo;

public class CasaEscolhe extends Casa {
    public CasaEscolhe(int numero) { super(numero); }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        // Aqui pedimos ao jogo para lidar com a interface de escolha
        // O jogo deve mostrar uma lista de adversários para o jogador atual escolher
        System.out.println("Jogador " + j.getCor() + ", escolha alguém para voltar à casa 0!");
        
        // Chamada de método que deve existir no Controller/Jogo do seu amigo
        jogo.solicitarEscolhaParaVoltarInicio(j); 
    }

    @Override
    public String getTipo() { return "Escolhe Jogador"; }

    @Override
    public String getDescricao() { return "Escolha um jogador para voltar ao início!"; }
}