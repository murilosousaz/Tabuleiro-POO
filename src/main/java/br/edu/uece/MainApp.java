package main.java.br.edu.uece;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static Scene setupScene;
    private static Scene gameScene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Configurar o palco principal
        primaryStage.setTitle("Jogo de Tabuleiro - Estilo Banco Imobiliário");
        primaryStage.setResizable(false);

        // Tentar carregar ícone da aplicação
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        } catch (Exception e) {
            System.out.println("Ícone da aplicação não encontrado.");
        }

        // Carregar tela de configuração
        carregarTelaSetup();

        // Exibir o palco
        primaryStage.show();
    }

    public static void carregarTelaSetup() {
        try {
            System.out.println("Tentando carregar: /fxml/setup_view.fxml");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/setup_view.fxml"));

            if (loader.getLocation() == null) {
                System.err.println("ERRO: Arquivo FXML não encontrado!");
                System.err.println("Verifique se resources/fxml/setup_view.fxml existe");
                System.err.println("E se a pasta 'resources' está marcada como Resources Root");
                return;
            }

            Parent root = loader.load();

            setupScene = new Scene(root, 800, 600);

            // Aplicar CSS se existir
            try {
                setupScene.getStylesheets().add(MainApp.class.getResource("/css/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("Arquivo CSS não encontrado (opcional).");
            }

            primaryStage.setScene(setupScene);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar tela de configuração: " + e.getMessage());
        }
    }

    public static void carregarTelaJogo() {
        try {
            System.out.println("Tentando carregar: /fxml/main_view.fxml");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/main_view.fxml"));

            if (loader.getLocation() == null) {
                System.err.println("ERRO: Arquivo FXML não encontrado!");
                System.err.println("Verifique se resources/fxml/main_view.fxml existe");
                return;
            }

            Parent root = loader.load();

            gameScene = new Scene(root, 1200, 800);

            // Aplicar CSS se existir
            try {
                gameScene.getStylesheets().add(MainApp.class.getResource("/css/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("Arquivo CSS não encontrado (opcional).");
            }

            primaryStage.setScene(gameScene);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar tela do jogo: " + e.getMessage());
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        System.out.println("Aplicação encerrada.");
    }
}