package main.java.br.edu.uece.model.casa;


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
}