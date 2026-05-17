package br.edu.uece.model.jogador;

import java.util.Random;

public abstract class Jogador {

    protected String nome;
    protected String cor;
    protected int posicao;
    protected int numeroJogadas;
    protected boolean perderVez;
    protected Random random;

    public Jogador(String nome, String cor) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do jogador não pode ser vazio!");
        }
        if (cor == null || cor.trim().isEmpty()) {
            throw new IllegalArgumentException("Cor do jogador não pode ser vazia!");
        }

        this.nome = nome;
        this.cor = cor;
        this.posicao = 0;
        this.numeroJogadas = 0;
        this.perderVez = false;
        this.random = new Random();
    }

    public String getNome() {return nome;}
    public String getCor() {return cor;}
    public int getPosicao() {return posicao;}

    public void mover(int novaPosicao) {
        if (novaPosicao < 0) {
            this.posicao = 0;
        } else if (novaPosicao > 40) {
            this.posicao = 40;
        } else {
            this.posicao = novaPosicao;
        }
    }

    public int getNumeroJogadas() {return numeroJogadas;}
    public void incrementarJogadas() {this.numeroJogadas++;}
    public void resetarJogadas() {this.numeroJogadas = 0;}
    public boolean devePerderVez() {return perderVez;}
    public void setPerderVez(boolean perderVez) {this.perderVez = perderVez;}
    public abstract int[] jogarDados();
    public abstract String getTipo();
    protected int rolarDado() {return random.nextInt(6) + 1;}
    
    public boolean venceu() {
        return posicao >= 40;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) return true;
        if (outro == null || getClass() != outro.getClass()) return false;
        Jogador jogador = (Jogador) outro;
        return nome.equals(jogador.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - Casa %d", nome, getTipo(), posicao);
    }

    public void setPreso(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}