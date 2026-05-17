package br.edu.uece.model.casa;

import br.edu.uece.jogo.Jogo;
import br.edu.uece.model.jogador.Jogador;

public abstract class Casa {
    protected int numero;

    public Casa(int numero) {
        if (numero < 0 || numero > 40) {
            throw new IllegalArgumentException("Número de casa deve estar entre 0 e 40.");
        }
        this.numero = numero;
    }

    public int getNumero() { return numero; }

    // MÉTODO ESSENCIAL: Define que toda casa deve ter um efeito
    public abstract void aplicarEfeito(Jogador j, Jogo jogo);

    public abstract String getTipo();
    public abstract String getDescricao();

    public boolean isInicio() { return numero == 0; }
    public boolean isFinal() { return numero == 40; }

    @Override
    public String toString() {
        return String.format("Casa %d [%s]", numero, getTipo());
    }
}