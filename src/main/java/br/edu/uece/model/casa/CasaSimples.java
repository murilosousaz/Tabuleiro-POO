package main.java.br.edu.uece.model.casa;

import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.jogo.Jogo;

public class CasaSimples extends Casa {
    public CasaSimples(int numero) { super(numero); }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        // Não faz nada, é uma casa neutra
    }

    @Override
    public String getTipo() { 
        if (isInicio()) return "Início";
        if (isFinal()) return "Chegada";
        return "Normal";
    }

    @Override
    public String getDescricao() { return "Casa normal sem efeitos especiais."; }
}