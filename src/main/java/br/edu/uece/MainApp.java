package br.edu.uece;

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
                return;
            }

            Parent root = loader.load();

            // CORREÇÃO: Aumentamos a altura de 600 para 680 para caber o rodapé inteiro no Fedora
            setupScene = new Scene(root, 800, 680);

            try {
                setupScene.getStylesheets().add(MainApp.class.getResource("/css/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("Arquivo CSS não encontrado (opcional).");
            }

            // CORREÇÃO: Permitir maximizar e redimensionar a tela livremente
            primaryStage.setResizable(true); 
            primaryStage.setScene(setupScene);
            primaryStage.centerOnScreen();

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
                return;
            }

            Parent root = loader.load();

            gameScene = new Scene(root, 1200, 820);

            try {
                gameScene.getStylesheets().add(MainApp.class.getResource("/css/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("Arquivo CSS não encontrado (opcional).");
            }

            // CORREÇÃO: Permitir maximizar também na tela do jogo
            primaryStage.setResizable(true);
            primaryStage.setScene(gameScene);
            primaryStage.centerOnScreen();

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