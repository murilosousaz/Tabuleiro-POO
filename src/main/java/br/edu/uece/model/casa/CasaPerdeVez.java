package main.java.br.edu.uece.model.casa;

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
}