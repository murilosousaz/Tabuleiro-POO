package br.edu.uece.model.casa;

import br.edu.uece.jogo.Jogo;
import br.edu.uece.model.jogador.Jogador;

public class CasaPerdeVez extends Casa {
    public CasaPerdeVez(int numero) { super(numero); }

    @Override
    public void aplicarEfeito(Jogador j, Jogo jogo) {
        // Define o estado do jogador como preso/pulando rodada
        j.setPreso(true); 
        System.out.println("O jogador " + j.getCor() + " parou na casa " + numero + " e perderá a próxima vez.");
    }

    @Override
    public String getTipo() { return "Perde Vez"; }

    @Override
    public String getDescricao() { return "Você perdeu a próxima rodada!"; }
}