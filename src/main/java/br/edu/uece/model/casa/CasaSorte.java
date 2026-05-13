package main.java.br.edu.uece.model.casa;

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
}