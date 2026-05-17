package br.edu.uece.jogo;

import br.edu.uece.model.casa.*;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {

    private List<Casa> casas;
    private static final int NUMERO_CASAS = 41; // 0 a 40

    public Tabuleiro() {
        casas = new ArrayList<>();
        inicializarCasas();
    }

    private void inicializarCasas() {
        for (int i = 0; i < NUMERO_CASAS; i++) {
            casas.add(criarCasa(i));
        }
    }

    private Casa criarCasa(int numero) {
        // Casas 10, 25, 38: Perde a próxima vez
        if (numero == 10 || numero == 25 || numero == 38) {
            return new CasaPerdeVez(numero);
        }

        // Casa 13: Surpresa (troca tipo de jogador)
        else if (numero == 13) {
            return new CasaSurpresa(numero);
        }

        // Casas 5, 15, 30: Casas da Sorte (avança 3 casas)
        else if (numero == 5 || numero == 15 || numero == 30) {
            return new CasaSorte(numero);
        }

        // Casas 17, 27: Escolhe jogador para voltar ao início
        else if (numero == 17 || numero == 27) {
            return new CasaEscolhe(numero);
        }

        // Casas 20, 35: Casas Mágicas (troca com último colocado)
        else if (numero == 20 || numero == 35) {
            return new CasaMagica(numero);
        }

        // Casas normais (incluindo início e fim)
        else {
            return new CasaSimples(numero);
        }
    }

    public Casa getCasa(int numero) {
        if (numero < 0 || numero >= NUMERO_CASAS) {
            throw new IndexOutOfBoundsException("Número de casa inválido: " + numero);
        }
        return casas.get(numero);
    }

    public int getNumeroCasas() {
        return NUMERO_CASAS;
    }

    public List<Casa> getCasas() {
        return new ArrayList<>(casas);
    }

    public boolean isCasaValida(int numero) {
        return numero >= 0 && numero < NUMERO_CASAS;
    }

    public String getDescricaoCasa(int numero) {
        if (!isCasaValida(numero)) {
            return "Casa inválida";
        }

        Casa casa = getCasa(numero);

        if (numero == 0) {
            return "Casa " + numero + " - Início";
        } else if (numero == 40) {
            return "Casa " + numero + " - Chegada (Vitória!)";
        } else if (numero == 10 || numero == 25 || numero == 38) {
            return "Casa " + numero + " - Perde a próxima rodada";
        } else if (numero == 13) {
            return "Casa " + numero + " - Surpresa (troca tipo)";
        } else if (numero == 5 || numero == 15 || numero == 30) {
            return "Casa " + numero + " - Sorte (avança 3)";
        } else if (numero == 17 || numero == 27) {
            return "Casa " + numero + " - Escolhe jogador para voltar";
        } else if (numero == 20 || numero == 35) {
            return "Casa " + numero + " - Mágica (troca com último)";
        } else {
            return "Casa " + numero + " - Normal";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Tabuleiro ===\n");
        sb.append("Total de casas: ").append(NUMERO_CASAS).append("\n");
        sb.append("Casas especiais:\n");

        for (int i = 0; i < NUMERO_CASAS; i++) {
            if (i == 0 || i == 40 || i == 10 || i == 13 || i == 17 ||
                    i == 20 || i == 25 || i == 27 || i == 30 || i == 35 ||
                    i == 38 || i == 5 || i == 15) {
                sb.append("  ").append(getDescricaoCasa(i)).append("\n");
            }
        }

        return sb.toString();
    }
}