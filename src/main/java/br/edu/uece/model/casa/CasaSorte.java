package br.edu.uece.model.casa;

import br.edu.uece.jogo.Jogo;
import br.edu.uece.model.jogador.Jogador;

public class CasaSorte extends Casa {

    private static final int CASAS_AVANCAR = 3;

    public CasaSorte(int numero) {
        super(numero);
    }

    public int getCasasAvancar() {
        return CASAS_AVANCAR;
    }

    @Override
    public String getTipo() {
        return "Sorte";
    }

    @Override
    public String getDescricao() {
        return String.format("Casa da Sorte! Avance %d casas (não funciona para jogadores Azarados).", CASAS_AVANCAR);
    }

    @Override
    public String toString() {
        return String.format("Casa %d [Sorte +%d]", getNumero(), CASAS_AVANCAR);
    }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}