package main.java.br.edu.uece.model.casa;


// É necessário importar essas classes para os parâmetros do método
import main.java.br.edu.uece.model.jogador.Jogador;
import main.java.br.edu.uece.jogo.Jogo;

public abstract class Casa {

    protected int numero;

    public Casa(int numero) {
        if (numero < 0 || numero > 40) {
            throw new IllegalArgumentException("Número de casa deve estar entre 0 e 40. Recebido: " + numero);
        }
        this.numero = numero;
    }



    public int getNumero() {
        return numero;
    }

    public abstract String getTipo();

    public abstract String getDescricao();

    public boolean isInicio() {
        return numero == 0;
    }

    public boolean isFinal() {
        return numero == 40;
    }

    @Override
    public boolean equals(Object outra) {
        if (this == outra) return true;
        if (outra == null || getClass() != outra.getClass()) return false;
        Casa casa = (Casa) outra;
        return numero == casa.numero;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numero);
    }

    @Override
    public String toString() {
        return String.format("Casa %d - %s", numero, getTipo());
    }

    // --- NOVO MÉTODO (Obrigatório para o Polimorfismo) ---
    // Este método permite que cada tipo de casa execute sua regra do PDF
    public abstract void aplicarEfeito(Jogador j, Jogo jogo);

}