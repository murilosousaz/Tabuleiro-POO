package br.edu.uece.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;

import br.edu.uece.model.jogador.*;
import br.edu.uece.*;

import java.util.ArrayList;
import java.util.List;

public class SetupController {

    @FXML
    private TextField nomeJogadorField;
    @FXML
    private ComboBox<String> tipoJogadorCombo;
    @FXML
    private ComboBox<String> corJogadorCombo;
    @FXML
    private ListView<String> listaJogadoresView;
    @FXML
    private Button adicionarBtn;
    @FXML
    private Button removerBtn;
    @FXML
    private Button iniciarJogoBtn;
    @FXML
    private Label statusLabel;

    private List<Jogador> jogadores;
    private List<String> coresDisponiveis;
    private ObservableList<String> jogadoresDisplay;

    // Cores disponíveis para jogadores
    private static final String[][] CORES = {
            {"Vermelho", "#E53935"},
            {"Azul", "#1E88E5"},
            {"Verde", "#43A047"},
            {"Amarelo", "#FDD835"},
            {"Roxo", "#8E24AA"},
            {"Laranja", "#FB8C00"},
            {"Rosa", "#D81B60"},
            {"Ciano", "#00ACC1"}
    };

    @FXML
    public void initialize() {
        jogadores = new ArrayList<>();
        coresDisponiveis = new ArrayList<>();
        jogadoresDisplay = FXCollections.observableArrayList();

        // Preencher cores disponíveis
        for (String[] cor : CORES) {
            coresDisponiveis.add(cor[0]);
        }

        // Configurar ComboBox de tipos
        tipoJogadorCombo.setItems(FXCollections.observableArrayList("Normal", "Sorte", "Azarado"));
        tipoJogadorCombo.setValue("Normal");

        // Configurar ListView
        listaJogadoresView.setItems(jogadoresDisplay);

        // Configurar botões padrões
        removerBtn.setDisable(true);
        iniciarJogoBtn.setDisable(true);

        // Adicionar listeners de seleção na lista
        listaJogadoresView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> removerBtn.setDisable(newVal == null)
        );

        // Evento ao apertar Enter no campo de texto
        nomeJogadorField.setOnAction(e -> handleAdicionar());

        // CORRIGIDO: Envolvendo a atualização de componentes de texto em um runLater para evitar quebra gráfica
        Platform.runLater(() -> {
            atualizarCoresDisponiveis();
            statusLabel.setText("Adicione de 2 a 6 jogadores para começar!");
        });
    }

    @FXML
    private void handleAdicionar() {
        String nome = nomeJogadorField.getText().trim();
        String tipo = tipoJogadorCombo.getValue();
        String corNome = corJogadorCombo.getValue();

        // Validações
        if (nome.isEmpty()) {
            mostrarErro("Digite o nome do jogador!");
            return;
        }

        if (jogadores.size() >= 6) {
            mostrarErro("Máximo de 6 jogadores!");
            return;
        }

        if (corNome == null) {
            mostrarErro("Selecione uma cor!");
            return;
        }

        // Verificar nome duplicado
        for (Jogador j : jogadores) {
            if (j.getNome().equalsIgnoreCase(nome)) {
                mostrarErro("Já existe um jogador com este nome!");
                return;
            }
        }

        // Obter código da cor
        String corHex = obterCorHex(corNome);

        // Criar jogador baseado no tipo
        Jogador novoJogador;
        switch (tipo) {
            case "Sorte":
                novoJogador = new JogadorSorte(nome, corHex);
                break;
            case "Azarado":
                novoJogador = new JogadorAzarado(nome, corHex);
                break;
            default:
                novoJogador = new JogadorNormal(nome, corHex);
                break;
        }

        // Adicionar jogador nas listas
        jogadores.add(novoJogador);
        jogadoresDisplay.add(String.format("%s (%s) - %s", nome, tipo, corNome));

        // Remover cor da lista de disponíveis
        coresDisponiveis.remove(corNome);
        atualizarCoresDisponiveis();

        // Limpar campos de entrada
        nomeJogadorField.clear();
        nomeJogadorField.requestFocus();

        // Atualizar status do botão de início
        atualizarStatus();
    }

    @FXML
    private void handleRemover() {
        int indice = listaJogadoresView.getSelectionModel().getSelectedIndex();

        if (indice >= 0) {
            Jogador jogadorRemovido = jogadores.remove(indice);
            jogadoresDisplay.remove(indice);

            // Devolver cor para lista de disponíveis
            String corNome = obterNomeCor(jogadorRemovido.getCor());
            if (!coresDisponiveis.contains(corNome) && !corNome.equals("Desconhecido")) {
                coresDisponiveis.add(corNome);
            }
            atualizarCoresDisponiveis();
            atualizarStatus();
        }
    }

    @FXML
    private void handleIniciarJogo() {
        if (jogadores.size() < 2) {
            mostrarErro("São necessários pelo menos 2 jogadores!");
            return;
        }

        // Passar jogadores para o GameController estático
        GameController.setJogadoresIniciais(new ArrayList<>(jogadores));

        // Carregar tela principal do jogo
        MainApp.carregarTelaJogo();
    }

    private void atualizarCoresDisponiveis() {
        corJogadorCombo.setItems(FXCollections.observableArrayList(coresDisponiveis));
        if (!coresDisponiveis.isEmpty()) {
            corJogadorCombo.setValue(coresDisponiveis.get(0));
        } else {
            corJogadorCombo.setValue(null);
        }
    }

    private void atualizarStatus() {
        int numJogadores = jogadores.size();

        if (numJogadores == 0) {
            statusLabel.setText("Adicione de 2 a 6 jogadores para começar!");
            iniciarJogoBtn.setDisable(true);
        } else if (numJogadores == 1) {
            statusLabel.setText("Adicione mais 1 jogador para começar!");
            iniciarJogoBtn.setDisable(true);
        } else if (numJogadores < 6) {
            statusLabel.setText(String.format("%d jogadores prontos! Pode adicionar mais ou iniciar.", numJogadores));
            iniciarJogoBtn.setDisable(false);
        } else {
            statusLabel.setText("6 jogadores (máximo atingido). Pronto para iniciar!");
            iniciarJogoBtn.setDisable(false);
        }
    }

    private String obterCorHex(String nomeCor) {
        for (String[] cor : CORES) {
            if (cor[0].equalsIgnoreCase(nomeCor)) {
                return cor[1];
            }
        }
        return "#000000";
    }

    private String obterNomeCor(String hexCor) {
        for (String[] cor : CORES) {
            if (cor[1].equalsIgnoreCase(hexCor)) {
                return cor[0];
            }
        }
        return "Desconhecido";
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}