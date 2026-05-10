package main.java.br.edu.uece.model.casa;
import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.jogo.Jogo;

public class CasaEscolhe extends Casa {

    public CasaEscolhe(int numero) {
        super(numero);
    }

    @Override
    public String getTipo() {
        return "Escolhe Jogador";
    }

    @Override
    public String getDescricao() {
        return "Escolha um jogador para voltar ao início!";
    }

    @Override
    public String toString() {
        return String.format("Casa %d [Escolhe Jogador]", getNumero());
    }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        jogo.solicitarEscolhaParaVoltarInicio(j);
    }

}