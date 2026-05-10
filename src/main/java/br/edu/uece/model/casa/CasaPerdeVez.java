package main.java.br.edu.uece.model.casa;
import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.jogo.Jogo;

public class CasaPerdeVez extends Casa {

    public CasaPerdeVez(int numero) {
        super(numero);
    }

    @Override
    public String getTipo() {
        return "Perde Vez";
    }

    @Override
    public String getDescricao() {
        return "Você perdeu a próxima rodada!";
    }

    @Override
    public String toString() {
        return String.format("Casa %d [Perde Vez]", getNumero());
    }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        // Implemente o efeito conforme a regra de cada uma ou deixe vazio se for a Simples
    }

}