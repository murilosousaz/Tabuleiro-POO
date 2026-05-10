package main.java.br.edu.uece.model.casa;

import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.jogo.Jogo;

public class CasaSimples extends Casa {

    public CasaSimples(int numero) {
        super(numero);
    }

    @Override
    public String getTipo() {
        if (isInicio()) {
            return "Início";
        } else if (isFinal()) {
            return "Chegada";
        }
        return "Normal";
    }

    @Override
    public String getDescricao() {
        if (isInicio()) {
            return "Casa inicial do jogo.";
        } else if (isFinal()) {
            return "Casa final - você venceu!";
        }
        return "Casa normal sem efeitos especiais.";
    }

    @Override
    public String toString() {
        if (isInicio()) {
            return String.format("Casa %d [Início]", getNumero());
        } else if (isFinal()) {
            return String.format("Casa %d [Chegada]", getNumero());
        }
        return String.format("Casa %d [Normal]", getNumero());
    }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        // Implemente o efeito conforme a regra de cada uma ou deixe vazio se for a Simples
    }

}