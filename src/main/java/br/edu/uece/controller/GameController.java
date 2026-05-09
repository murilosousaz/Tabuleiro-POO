package main.java.br.edu.uece.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

import main.java.br.edu.uece.model.casa.*;
import main.java.br.edu.uece.model.jogador.*;
import main.java.br.edu.uece.jogo.Jogo;

import java.util.*;

public class GameController {

    // Componentes FXML
    @FXML
    private GridPane tabuleiroGrid;
    @FXML
    private TextArea logArea;
    @FXML
    private Button jogarDadosBtn;
    @FXML
    private Button proximoJogadorBtn;
    @FXML
    private Label jogadorAtualLabel;
    @FXML
    private Label dado1Label;
    @FXML
    private Label dado2Label;
    @FXML
    private Label somaLabel;
    @FXML
    private CheckBox modoDebugCheckBox;
    @FXML
    private TextField casaDebugField;
    @FXML
    private HBox debugBox;
    @FXML
    private ListView<String> placarListView;

    // Atributos do controlador
    private Jogo jogo;
    private boolean modoDebug;
    private Map<Jogador, Circle> jogadorCirculoMap;
    private boolean aguardandoProximoJogador;
    private boolean jogoFinalizado;

    // Lista de jogadores configurados na tela de setup
    private static List<Jogador> jogadoresIniciais;

    @FXML
    public void initialize() {
        modoDebug = false;
        aguardandoProximoJogador = false;
        jogoFinalizado = false;
        jogadorCirculoMap = new HashMap<>();

        // Configurar visibilidade inicial
        debugBox.setVisible(false);
        debugBox.setManaged(false);
        proximoJogadorBtn.setVisible(false);
        proximoJogadorBtn.setManaged(false);

        // Configurar eventos
        modoDebugCheckBox.setOnAction(e -> toggleModoDebug());
        jogarDadosBtn.setOnAction(e -> handleJogarDados());
        proximoJogadorBtn.setOnAction(e -> handleProximoJogador());

        // Limitar entrada no campo debug apenas para números
        casaDebugField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                casaDebugField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        logMensagem("=== Jogo de Tabuleiro Iniciado ===");
        logMensagem("Configure os jogadores e inicie o jogo!");

        // Inicializar jogo automaticamente se jogadores foram configurados
        if (jogadoresIniciais != null && !jogadoresIniciais.isEmpty()) {
            inicializarJogo(jogadoresIniciais);
        }
    }

    public static void setJogadoresIniciais(List<Jogador> jogadores) {
        jogadoresIniciais = jogadores;
    }

    public void inicializarJogo(List<Jogador> jogadores) {
        if (jogadores == null || jogadores.size() < 2) {
            mostrarErro("É necessário ter pelo menos 2 jogadores!");
            return;
        }

        if (jogadores.size() > 6) {
            mostrarErro("O jogo permite no máximo 6 jogadores!");
            return;
        }

        // Criar instância do jogo
        jogo = new Jogo(jogadores);

        // Renderizar tabuleiro
        renderizarTabuleiro();

        // Posicionar jogadores no início
        for (Jogador jogador : jogadores) {
            Circle circulo = criarCirculoJogador(jogador);
            jogadorCirculoMap.put(jogador, circulo);
            posicionarJogadorNoCasa(jogador, 0);
        }

        // Atualizar interface
        atualizarJogadorAtual();
        atualizarPlacar();

        logMensagem("\n=== JOGO INICIADO ===");
        logMensagem("Total de jogadores: " + jogadores.size());
        for (Jogador j : jogadores) {
            logMensagem(String.format("- %s (%s)", j.getNome(), j.getTipo()));
        }
        logMensagem("\nPrimeiro jogador: " + jogo.getJogadorAtual().getNome());
        logMensagem("Clique em 'Jogar Dados' para começar!\n");
    }

    private void renderizarTabuleiro() {
        tabuleiroGrid.getChildren().clear();

        int numCasas = 40;
        int colunasMax = 10;

        for (int i = 0; i <= numCasas; i++) {
            Label casaLabel = new Label(String.valueOf(i));
            casaLabel.setStyle(getEstiloCasa(i));
            casaLabel.setPrefSize(60, 60);
            casaLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // Calcular posição no grid (serpentear)
            int linha = i / colunasMax;
            int coluna = (linha % 2 == 0) ? (i % colunasMax) : (colunasMax - 1 - (i % colunasMax));

            tabuleiroGrid.add(casaLabel, coluna, linha);
        }
    }

    private String getEstiloCasa(int numeroCasa) {
        String baseStyle = "-fx-border-color: #333; -fx-border-width: 1; -fx-font-size: 14px; -fx-font-weight: bold;";

        if (numeroCasa == 0) {
            return baseStyle + "-fx-background-color: #4CAF50; -fx-text-fill: white;"; // Início
        } else if (numeroCasa == 40) {
            return baseStyle + "-fx-background-color: #FFD700; -fx-text-fill: #333;"; // Fim
        } else if (numeroCasa == 10 || numeroCasa == 25 || numeroCasa == 38) {
            return baseStyle + "-fx-background-color: #FF5252; -fx-text-fill: white;"; // Perde vez
        } else if (numeroCasa == 13) {
            return baseStyle + "-fx-background-color: #9C27B0; -fx-text-fill: white;"; // Surpresa
        } else if (numeroCasa == 5 || numeroCasa == 15 || numeroCasa == 30) {
            return baseStyle + "-fx-background-color: #2196F3; -fx-text-fill: white;"; // Sorte
        } else if (numeroCasa == 17 || numeroCasa == 27) {
            return baseStyle + "-fx-background-color: #FF9800; -fx-text-fill: white;"; // Escolhe jogador
        } else if (numeroCasa == 20 || numeroCasa == 35) {
            return baseStyle + "-fx-background-color: #E91E63; -fx-text-fill: white;"; // Mágica
        }

        return baseStyle + "-fx-background-color: #EEEEEE;";
    }

    private Circle criarCirculoJogador(Jogador jogador) {
        Circle circulo = new Circle(8);
        circulo.setFill(Color.web(jogador.getCor()));
        circulo.setStroke(Color.BLACK);
        circulo.setStrokeWidth(1);
        return circulo;
    }

    private void posicionarJogadorNoCasa(Jogador jogador, int numeroCasa) {
        Circle circulo = jogadorCirculoMap.get(jogador);
        if (circulo == null) return;

        // Remover do pai anterior
        if (circulo.getParent() != null) {
            ((javafx.scene.layout.Pane) circulo.getParent()).getChildren().remove(circulo);
        }

        // Encontrar a Label da casa
        int colunasMax = 10;
        int linha = numeroCasa / colunasMax;
        int coluna = (linha % 2 == 0) ? (numeroCasa % colunasMax) : (colunasMax - 1 - (numeroCasa % colunasMax));

        for (javafx.scene.Node node : tabuleiroGrid.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);

            if (nodeCol != null && nodeRow != null && nodeCol == coluna && nodeRow == linha) {
                if (node instanceof Label) {
                    Label casaLabel = (Label) node;

                    // Criar StackPane se não existir
                    if (!(casaLabel.getGraphic() instanceof javafx.scene.layout.StackPane)) {
                        javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane();
                        casaLabel.setGraphic(stack);
                    }

                    javafx.scene.layout.StackPane stack = (javafx.scene.layout.StackPane) casaLabel.getGraphic();

                    // Adicionar círculo ao StackPane
                    if (!stack.getChildren().contains(circulo)) {
                        stack.getChildren().add(circulo);
                    }

                    // Ajustar posicionamento para múltiplos jogadores
                    ajustarPosicionamentoMultiplosJogadores(stack);
                    break;
                }
            }
        }
    }

    private void ajustarPosicionamentoMultiplosJogadores(javafx.scene.layout.StackPane stack) {
        int numJogadores = stack.getChildren().size();
        double angulo = 2 * Math.PI / numJogadores;
        double raio = 15;

        for (int i = 0; i < numJogadores; i++) {
            javafx.scene.Node node = stack.getChildren().get(i);
            if (node instanceof Circle) {
                double x = raio * Math.cos(i * angulo);
                double y = raio * Math.sin(i * angulo);
                node.setTranslateX(x);
                node.setTranslateY(y);
            }
        }
    }

    @FXML
    private void handleJogarDados() {
        if (jogo == null) {
            mostrarErro("Jogo não inicializado!");
            return;
        }

        if (jogoFinalizado) {
            mostrarInfo("O jogo já terminou!");
            return;
        }

        if (aguardandoProximoJogador) {
            mostrarInfo("Clique em 'Próximo Jogador' para continuar.");
            return;
        }

        Jogador jogadorAtual = jogo.getJogadorAtual();

        // Verificar se jogador deve pular rodada
        if (jogadorAtual.devePerderVez()) {
            logMensagem(String.format("\n[%s] perdeu a vez! Pulando rodada...", jogadorAtual.getNome()));
            jogadorAtual.setPerderVez(false);
            jogo.proximoJogador();
            atualizarJogadorAtual();
            atualizarPlacar();
            return;
        }

        int posicaoAtual = jogadorAtual.getPosicao();
        int casaDestino;

        if (modoDebug) {
            // Modo debug: usuário escolhe a casa
            String inputCasa = casaDebugField.getText().trim();
            if (inputCasa.isEmpty()) {
                mostrarErro("Digite o número da casa!");
                return;
            }

            try {
                casaDestino = Integer.parseInt(inputCasa);
                if (casaDestino < 0 || casaDestino > 40) {
                    mostrarErro("Casa deve estar entre 0 e 40!");
                    return;
                }

                logMensagem(String.format("\n[DEBUG] %s: Casa %d -> Casa %d",
                        jogadorAtual.getNome(), posicaoAtual, casaDestino));

                dado1Label.setText("?");
                dado2Label.setText("?");
                somaLabel.setText("DEBUG");

            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido!");
                return;
            }

        } else {
            // Modo normal: jogar dados
            int[] dados = jogadorAtual.jogarDados();
            int dado1 = dados[0];
            int dado2 = dados[1];
            int soma = dado1 + dado2;

            casaDestino = posicaoAtual + soma;

            // Atualizar labels dos dados
            dado1Label.setText(String.valueOf(dado1));
            dado2Label.setText(String.valueOf(dado2));
            somaLabel.setText(String.valueOf(soma));

            logMensagem(String.format("\n%s jogou os dados: [%d] + [%d] = %d",
                    jogadorAtual.getNome(), dado1, dado2, soma));

            // Verificar dados iguais
            boolean dadosIguais = (dado1 == dado2);
            if (dadosIguais) {
                logMensagem("🎲 DADOS IGUAIS! Jogador ganha outra rodada!");
            }
        }

        // Mover jogador
        jogadorAtual.mover(casaDestino);
        int posicaoFinal = jogadorAtual.getPosicao();

        logMensagem(String.format("Movendo de casa %d para casa %d", posicaoAtual, posicaoFinal));

        // Atualizar posição visual
        posicionarJogadorNoCasa(jogadorAtual, posicaoFinal);

        // Verificar vitória
        if (posicaoFinal >= 40) {
            logMensagem(String.format("\n🏆 %s VENCEU O JOGO! 🏆", jogadorAtual.getNome()));
            finalizarJogo(jogadorAtual);
            return;
        }

        // Aplicar efeito da casa
        Casa casa = jogo.getTabuleiro().getCasa(posicaoFinal);
        aplicarEfeitoCasa(casa, jogadorAtual);

        // Incrementar contador de jogadas
        jogadorAtual.incrementarJogadas();

        // Atualizar placar
        atualizarPlacar();

        // Verificar se joga novamente
        if (!modoDebug && !aguardandoProximoJogador) {
            // Verificar dados iguais
            // A lógica de dados iguais seria implementada aqui
            // Por simplicidade, vamos sempre passar para o próximo
            aguardandoProximoJogador = true;
            jogarDadosBtn.setDisable(true);
            proximoJogadorBtn.setVisible(true);
            proximoJogadorBtn.setManaged(true);
        }

        // Limpar campo debug
        if (modoDebug) {
            casaDebugField.clear();
            aguardandoProximoJogador = true;
            jogarDadosBtn.setDisable(true);
            proximoJogadorBtn.setVisible(true);
            proximoJogadorBtn.setManaged(true);
        }
    }

    @FXML
    private void handleProximoJogador() {
        if (jogo == null || jogoFinalizado) return;

        jogo.proximoJogador();
        atualizarJogadorAtual();

        aguardandoProximoJogador = false;
        jogarDadosBtn.setDisable(false);
        proximoJogadorBtn.setVisible(false);
        proximoJogadorBtn.setManaged(false);

        // Limpar dados
        dado1Label.setText("-");
        dado2Label.setText("-");
        somaLabel.setText("-");
    }

    private void aplicarEfeitoCasa(Casa casa, Jogador jogador) {
        int numero = casa.getNumero();

        // Casas 10, 25, 38: Perde a próxima vez
        if (numero == 10 || numero == 25 || numero == 38) {
            jogador.setPerderVez(true);
            logMensagem("⏸️  Casa Especial: Jogador perde a próxima rodada!");
        }

        // Casa 13: Surpresa - troca tipo de jogador
        else if (numero == 13) {
            String tipoAnterior = jogador.getTipo();
            // Implementar lógica de carta surpresa
            String[] tipos = {"Normal", "Sorte", "Azarado"};
            String novoTipo = tipos[new Random().nextInt(tipos.length)];

            logMensagem(String.format("🎴 Casa Surpresa! Jogador era %s", tipoAnterior));
            // Aqui você implementaria a troca de tipo do jogador
            logMensagem(String.format("   Carta sorteada: Agora é jogador %s!", novoTipo));
        }

        // Casas 5, 15, 30: Sorte (avança 3 se não for azarado)
        else if (numero == 5 || numero == 15 || numero == 30) {
            if (!jogador.getTipo().equals("Azarado")) {
                int novaPosicao = Math.min(jogador.getPosicao() + 3, 40);
                jogador.mover(novaPosicao);
                posicionarJogadorNoCasa(jogador, novaPosicao);
                logMensagem("🍀 Casa da Sorte! Avança 3 casas!");

                // Verificar vitória após avanço
                if (novaPosicao >= 40) {
                    logMensagem(String.format("\n🏆 %s VENCEU O JOGO! 🏆", jogador.getNome()));
                    finalizarJogo(jogador);
                }
            } else {
                logMensagem("🍀 Casa da Sorte, mas jogador é Azarado - sem efeito.");
            }
        }

        // Casas 17, 27: Escolhe jogador para voltar ao início
        else if (numero == 17 || numero == 27) {
            logMensagem("⚔️  Casa Especial: Escolha um jogador para voltar ao início!");
            // Aqui seria implementada a escolha via interface
            escolherJogadorParaVoltar();
        }

        // Casas 20, 35: Mágica - troca com último colocado
        else if (numero == 20 || numero == 35) {
            Jogador ultimoColocado = encontrarUltimoColocado();
            if (ultimoColocado != null && ultimoColocado != jogador) {
                int posTemp = jogador.getPosicao();
                jogador.mover(ultimoColocado.getPosicao());
                ultimoColocado.mover(posTemp);

                posicionarJogadorNoCasa(jogador, jogador.getPosicao());
                posicionarJogadorNoCasa(ultimoColocado, ultimoColocado.getPosicao());

                logMensagem(String.format("✨ Casa Mágica! Trocou de posição com %s", ultimoColocado.getNome()));
            } else {
                logMensagem("✨ Casa Mágica, mas jogador já está em último - sem efeito.");
            }
        }
    }

    private void escolherJogadorParaVoltar() {
        List<Jogador> outrosJogadores = new ArrayList<>();
        for (Jogador j : jogo.getJogadores()) {
            if (j != jogo.getJogadorAtual()) {
                outrosJogadores.add(j);
            }
        }

        if (outrosJogadores.isEmpty()) return;

        ChoiceDialog<Jogador> dialog = new ChoiceDialog<>(outrosJogadores.get(0), outrosJogadores);
        dialog.setTitle("Escolher Jogador");
        dialog.setHeaderText("Casa Especial: Escolha um jogador");
        dialog.setContentText("Qual jogador deve voltar ao início?");

        Optional<Jogador> resultado = dialog.showAndWait();
        resultado.ifPresent(jogadorEscolhido -> {
            jogadorEscolhido.mover(0);
            posicionarJogadorNoCasa(jogadorEscolhido, 0);
            logMensagem(String.format("   %s voltou para o início!", jogadorEscolhido.getNome()));
        });
    }

    private Jogador encontrarUltimoColocado() {
        Jogador ultimo = null;
        int menorPosicao = Integer.MAX_VALUE;

        for (Jogador j : jogo.getJogadores()) {
            if (j.getPosicao() < menorPosicao) {
                menorPosicao = j.getPosicao();
                ultimo = j;
            }
        }

        return ultimo;
    }

    private void finalizarJogo(Jogador vencedor) {
        jogoFinalizado = true;
        jogarDadosBtn.setDisable(true);
        proximoJogadorBtn.setVisible(false);

        // Ordenar jogadores por posição
        List<Jogador> ranking = new ArrayList<>(jogo.getJogadores());
        ranking.sort((j1, j2) -> Integer.compare(j2.getPosicao(), j1.getPosicao()));

        logMensagem("\n" + "=".repeat(50));
        logMensagem("              JOGO FINALIZADO");
        logMensagem("=".repeat(50));
        logMensagem(String.format("\n🏆 VENCEDOR: %s", vencedor.getNome()));
        logMensagem("\n📊 ESTATÍSTICAS FINAIS:\n");

        for (int i = 0; i < ranking.size(); i++) {
            Jogador j = ranking.get(i);
            logMensagem(String.format("%d°) %s - Casa %d - %d jogadas",
                    i + 1, j.getNome(), j.getPosicao(), j.getNumeroJogadas()));
        }

        logMensagem("\n" + "=".repeat(50));

        // Mostrar diálogo de vitória
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Jogo Finalizado");
        alert.setHeaderText("🏆 Temos um vencedor! 🏆");
        alert.setContentText(String.format("%s venceu o jogo!", vencedor.getNome()));
        alert.showAndWait();
    }

    private void atualizarJogadorAtual() {
        if (jogo != null) {
            Jogador atual = jogo.getJogadorAtual();
            jogadorAtualLabel.setText(String.format("%s (%s)", atual.getNome(), atual.getTipo()));
            jogadorAtualLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-weight: bold; -fx-font-size: 16px;",
                    atual.getCor()));
        }
    }

    private void atualizarPlacar() {
        if (jogo == null) return;

        placarListView.getItems().clear();

        // Ordenar por posição (maior primeiro)
        List<Jogador> ranking = new ArrayList<>(jogo.getJogadores());
        ranking.sort((j1, j2) -> Integer.compare(j2.getPosicao(), j1.getPosicao()));

        for (int i = 0; i < ranking.size(); i++) {
            Jogador j = ranking.get(i);
            String item = String.format("%d°) %s - Casa %d", i + 1, j.getNome(), j.getPosicao());
            placarListView.getItems().add(item);
        }
    }

    private void toggleModoDebug() {
        modoDebug = modoDebugCheckBox.isSelected();
        debugBox.setVisible(modoDebug);
        debugBox.setManaged(modoDebug);

        if (modoDebug) {
            logMensagem("\n🔧 MODO DEBUG ATIVADO");
            logMensagem("Digite o número da casa para mover o jogador diretamente.\n");
        } else {
            logMensagem("\n🎲 MODO NORMAL ATIVADO\n");
        }
    }

    private void logMensagem(String mensagem) {
        Platform.runLater(() -> {
            logArea.appendText(mensagem + "\n");
        });
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}