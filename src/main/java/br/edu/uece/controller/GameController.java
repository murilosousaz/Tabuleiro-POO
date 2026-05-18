package br.edu.uece.controller;

import br.edu.uece.jogo.Jogo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

import br.edu.uece.model.casa.*;
import br.edu.uece.model.jogador.Jogador;

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

        // CORRIGIDO: Colocado dentro de um runLater para evitar travar o carregamento do FXML
        Platform.runLater(() -> {
            logMensagem("=== Jogo de Tabuleiro Iniciado ===");
            logMensagem("Configure os jogadores e inicie o jogo!");
            
            // Inicializar jogo automaticamente se jogadores foram configurados
            if (jogadoresIniciais != null && !jogadoresIniciais.isEmpty()) {
                inicializarJogo(jogadoresIniciais);
            }
        });
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

    // Criar as 40 casas posicionando-as apenas nas bordas do quadrado 11x11
    for (int i = 0; i < 40; i++) {
        Label casaLabel = new Label(String.valueOf(i));
        casaLabel.setStyle(getEstiloCasa(i));
        casaLabel.setPrefSize(60, 60);
        casaLabel.setAlignment(javafx.geometry.Pos.CENTER);

        // Calcula a posição exata na borda do quadrado
        int[] pos = calcularLinhaColunaBorda(i);
        int coluna = pos[0];
        int linha = pos[1];

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

        return baseStyle + "-fx-background-color: #EEEEEE; -fx-text-fill: #333;";
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

    if (circulo.getParent() != null) {
        ((javafx.scene.layout.Pane) circulo.getParent()).getChildren().remove(circulo);
    }

    // Se o jogador passar da casa 40 (por exemplo, 42), ele trava na 40
    int casaEfetiva = Math.min(numeroCasa, 40);

    // Calcula a posição usando a nova lógica de borda quadrada
    int[] pos = calcularLinhaColunaBorda(casaEfetiva);
    int coluna = pos[0];
    int linha = pos[1];

    for (javafx.scene.Node node : tabuleiroGrid.getChildren()) {
        Integer nodeCol = GridPane.getColumnIndex(node);
        Integer nodeRow = GridPane.getRowIndex(node);

        if (nodeCol != null && nodeRow != null && nodeCol == coluna && nodeRow == linha) {
            if (node instanceof Label) {
                Label casaLabel = (Label) node;

                if (!(casaLabel.getGraphic() instanceof javafx.scene.layout.StackPane)) {
                    javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane();
                    casaLabel.setGraphic(stack);
                }

                javafx.scene.layout.StackPane stack = (javafx.scene.layout.StackPane) casaLabel.getGraphic();

                if (!stack.getChildren().contains(circulo)) {
                    stack.getChildren().add(circulo);
                }

                ajustarPosicionamentoMultiplosJogadores(stack);
                break;
            }
        }
    }
}

    private void ajustarPosicionamentoMultiplosJogadores(javafx.scene.layout.StackPane stack) {
        int numJogadores = stack.getChildren().size();
        if (numJogadores <= 1) {
            if (numJogadores == 1) {
                stack.getChildren().get(0).setTranslateX(0);
                stack.getChildren().get(0).setTranslateY(0);
            }
            return;
        }

        double angulo = 2 * Math.PI / numJogadores;
        double raio = 12;

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
            int[] dados = jogadorAtual.jogarDados();
            int dado1 = dados[0];
            int dado2 = dados[1];
            int soma = dado1 + dado2;

            casaDestino = posicaoAtual + soma;

            dado1Label.setText(String.valueOf(dado1));
            dado2Label.setText(String.valueOf(dado2));
            somaLabel.setText(String.valueOf(soma));

            logMensagem(String.format("\n%s jogou os dados: [%d] + [%d] = %d",
                    jogadorAtual.getNome(), dado1, dado2, soma));
        }

        jogadorAtual.mover(casaDestino);
        int posicaoFinal = jogadorAtual.getPosicao();

        logMensagem(String.format("Movendo de casa %d para casa %d", posicaoAtual, posicaoFinal));
        posicionarJogadorNoCasa(jogadorAtual, posicaoFinal);

        if (posicaoFinal >= 40) {
            logMensagem(String.format("\n🏆 %s VENCEU O JOGO! 🏆", jogadorAtual.getNome()));
            finalizarJogo(jogadorAtual);
            return;
        }

        Casa casa = jogo.getTabuleiro().getCasa(posicaoFinal);
        aplicarEfeitoCasa(casa, jogadorAtual);

        jogadorAtual.incrementarJogadas();
        atualizarPlacar();

        aguardandoProximoJogador = true;
        jogarDadosBtn.setDisable(true);
        proximoJogadorBtn.setVisible(true);
        proximoJogadorBtn.setManaged(true);
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

        dado1Label.setText("-");
        dado2Label.setText("-");
        somaLabel.setText("-");
    }

    private void aplicarEfeitoCasa(Casa casa, Jogador jogador) {
        int numero = casa.getNumero();

        if (numero == 10 || numero == 25 || numero == 38) {
            jogador.setPerderVez(true);
            logMensagem("⏸️  Casa Especial: Jogador perde a próxima rodada!");
        }

        else if (numero == 13) {
            String tipoAnterior = jogador.getTipo();
            String[] tipos = {"Normal", "Sorte", "Azarado"};
            String novoTipo = tipos[new Random().nextInt(tipos.length)];

            logMensagem(String.format("🎴 Casa Surpresa! Jogador era %s", tipoAnterior));
            logMensagem(String.format("   Carta sorteada: Agora é jogador %s!", novoTipo));
        }

        else if (numero == 5 || numero == 15 || numero == 30) {
            if (!jogador.getTipo().equals("Azarado")) {
                int novaPosicao = Math.min(jogador.getPosicao() + 3, 40);
                jogador.mover(novaPosicao);
                posicionarJogadorNoCasa(jogador, novaPosicao);
                logMensagem("🍀 Casa da Sorte! Avança 3 casas!");

                if (novaPosicao >= 40) {
                    logMensagem(String.format("\n🏆 %s VENCEU O JOGO! 🏆", jogador.getNome()));
                    finalizarJogo(jogador);
                }
            } else {
                logMensagem("🍀 Casa da Sorte, mas jogador é Azarado - sem efeito.");
            }
        }

        else if (numero == 17 || numero == 27) {
            logMensagem("⚔️  Casa Especial: Escolha um jogador para voltar ao início!");
            Platform.runLater(this::escolherJogadorParaVoltar);
        }

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
            atualizarPlacar();
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
            if (logArea != null) {
                logArea.appendText(mensagem + "\n");
            }
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
    
    private int[] calcularLinhaColunaBorda(int numeroCasa) {
        int coluna = 0;
        int linha = 0;

        // Lógica para contornar um quadrado 11x11 (índices de 0 a 10)
        if (numeroCasa <= 10) {
            // Borda Superior: vai da esquerda para a direita (linha 0)
            coluna = numeroCasa;
            linha = 0;
        } else if (numeroCasa <= 20) {
            // Borda Direita: vai de cima para baixo (coluna 10)
            coluna = 10;
            linha = numeroCasa - 10;
        } else if (numeroCasa <= 30) {
            // Borda Inferior: vai da direita para a esquerda (linha 10)
            coluna = 10 - (numeroCasa - 20);
            linha = 10;
        } else {
            // Borda Esquerda: vai de baixo para cima (coluna 0)
            coluna = 0;
            linha = 10 - (numeroCasa - 30);
        }

        return new int[]{coluna, linha};
    }
}