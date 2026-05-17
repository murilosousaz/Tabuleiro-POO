package br.edu.uece.model.casa;

import br.edu.uece.jogo.Jogo;
import br.edu.uece.model.jogador.Jogador;

public class CasaMagica extends Casa {

    public CasaMagica(int numero) {
        super(numero);
    }

    @Override
    public String getTipo() {
        return "Mágica";
    }

    @Override
    public String getDescricao() {
        return "Casa Mágica! Troque de posição com o jogador que está em último lugar.";
    }

    @Override
    public String toString() {
        return String.format("Casa %d [Mágica ⇄]", getNumero());
    }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}