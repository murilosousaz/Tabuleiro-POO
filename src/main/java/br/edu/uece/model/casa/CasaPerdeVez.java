package main.java.br.edu.uece.model.casa;

import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.jogo.Jogo;

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