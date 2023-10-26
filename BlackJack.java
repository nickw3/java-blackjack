import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Collections.shuffle;

public class BlackJack extends Application {
    Statement stmt;
    HBox cardPaneDealer = new HBox(15);
    HBox cardPanePlayer = new HBox(15);
    GridPane gridPane = new GridPane();
    TextField winField = new TextField();
    TextField winCountField = new TextField();
    TextField lossCountField = new TextField();
    TextField usernameField = new TextField();
    BlackJackHand dealerHand = new BlackJackHand();
    BlackJackHand playerHand = new BlackJackHand();
    Deck cardDeck = new Deck();
    int winCount = 0;
    int loseCount = 0;
    boolean gameOver = false;
    boolean win;
    double stageWidth = 500;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        initializeDB();

        //shuffle the deck
        shuffle(cardDeck.deck);

        this.cardDeck = dealerHand.createDealerHand(cardDeck);
        this.cardDeck = playerHand.createPlayerHand(cardDeck);

        //build up cardPane
        cardPaneDealer.setAlignment(Pos.TOP_CENTER);
        cardPaneDealer.getChildren().add(new ImageView(this.dealerHand.getHand().get(0).url));
        cardPaneDealer.getChildren().add(new ImageView(this.dealerHand.getHand().get(1).url));

        cardPanePlayer.setAlignment(Pos.BOTTOM_CENTER);
        cardPanePlayer.getChildren().add(new ImageView(this.playerHand.getHand().get(0).url));
        cardPanePlayer.getChildren().add(new ImageView(this.playerHand.getHand().get(1).url));

        Button hitBtn = new Button("Hit");
        Button standBtn = new Button("Stand");
        Button newGameBtn = new Button("New Game");
        Button submitBtn = new Button("Submit Scores");
        winField.setEditable(false);

        gridPane.setHgap(5);

        gridPane.add(hitBtn, 0, 0);
        gridPane.add(standBtn, 1, 0);
        gridPane.add(newGameBtn, 2, 0);
        gridPane.add(winField, 3,0);

        winCountField.setMaxWidth(70);
        lossCountField.setMaxWidth(70);
        winCountField.setText("Wins: " + winCount);
        lossCountField.setText("Losses: " + loseCount);
        gridPane.add(winCountField, 0, 1);
        gridPane.add(lossCountField, 1, 1);
        gridPane.add(usernameField, 3, 1);
        gridPane.add(submitBtn, 4, 1);

        usernameField.setText("Enter username");

        BorderPane displayPane = new BorderPane();

        displayPane.setTop(cardPaneDealer);
        displayPane.setCenter(cardPanePlayer);
        displayPane.setBottom(gridPane);

        hitBtn.setOnAction(new HitHandler());
        standBtn.setOnAction(new StandHandler());
        newGameBtn.setOnAction(new NewGameHandler());
        submitBtn.setOnAction(new SubmitHandler());

        //Create the scene and add to stage
        Scene scene = new Scene(displayPane);
        primaryStage.setTitle("DisplayThreeCards");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setWidth(stageWidth);
        primaryStage.setMaxWidth(Double.MAX_VALUE);
        primaryStage.setMaxHeight(Double.MAX_VALUE);
        primaryStage.show();
    }

    private void initializeDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
            Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost/blackjack", "root", "root");

            System.out.println("Database connected");

            stmt = connection.createStatement();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class HitHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            playerHand = playerHand.dealCard(cardDeck, playerHand);
            Card newCard = playerHand.hand.get(playerHand.hand.size()-1);
            System.out.println();
            cardPanePlayer.getChildren().add(new ImageView(newCard.url));
            System.out.println(cardDeck.deck.size());
            if(playerHand.getHandValue() > 21){
                System.out.println("Player Loses");
                winField.setText("You Lose :(");
                loseCount++;
                lossCountField.setText("Losses: " + loseCount);
                gameOver = true;
            }
            if(playerHand.getHandValue() == 21){
                System.out.println("Player Wins");
                winField.setText("You Win!");
                winCount++;
                winCountField.setText("Wins: " + winCount);
                gameOver = true;
            }
        }
    }

    class StandHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            dealerHand.hand.get(1).setHidden(false);
            cardPaneDealer.getChildren().set(1, new ImageView(dealerHand.hand.get(1).url));
            if(dealerHand.getHandValue() <= 16){
                dealerHand = dealerHand.dealCard(cardDeck, dealerHand);
                Card newCard = dealerHand.hand.get(dealerHand.hand.size()-1);
                cardPaneDealer.getChildren().add(new ImageView(newCard.url));
                while(dealerHand.getHandValue() <= 16){
                    dealerHand = dealerHand.dealCard(cardDeck, dealerHand);
                    newCard = dealerHand.hand.get(dealerHand.hand.size()-1);
                    cardPaneDealer.getChildren().add(new ImageView(newCard.url));
                }
                if(dealerHand.getHandValue() > 21){
                    win = true;
                    gameOver = true;
                    winCount++;
                    winCountField.setText("Wins: " + winCount);

                    winField.setText("You Win!");
                    System.out.println("Dealer loses");
                }
            }
            if(dealerHand.getHandValue() <= playerHand.getHandValue() && !gameOver){
                win = true;
                gameOver = true;
                winCount++;
                winCountField.setText("Wins: " + winCount);
                winField.setText("You Win!");
                System.out.print("Player Wins");
            }
            if(dealerHand.getHandValue() > playerHand.getHandValue() && !gameOver){
                win = false;
                gameOver = true;
                loseCount++;
                lossCountField.setText("Losses: " + loseCount);
                winField.setText("You Lose :(");
                System.out.print("Dealer Wins");
            }
        }
    }
    class NewGameHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            cardDeck = new Deck();
            win = false;
            gameOver = false;
            winField.clear();
            winCount = 0;
            loseCount = 0;
            winCountField.setText("Wins: " + winCount);
            lossCountField.setText("Losses: " + loseCount);
            usernameField.clear();
            usernameField.setText("Enter username");
            shuffle(cardDeck.deck);
            dealerHand.hand.clear();
            playerHand.hand.clear();
            cardDeck = dealerHand.createDealerHand(cardDeck);
            cardDeck = playerHand.createPlayerHand(cardDeck);

            cardPaneDealer.getChildren().clear();
            cardPaneDealer.getChildren().add(new ImageView(dealerHand.getHand().get(0).url));
            cardPaneDealer.getChildren().add(new ImageView(dealerHand.getHand().get(1).url));

            cardPanePlayer.getChildren().clear();
            cardPanePlayer.getChildren().add(new ImageView(playerHand.getHand().get(0).url));
            cardPanePlayer.getChildren().add(new ImageView(playerHand.getHand().get(1).url));
        }
    }

    class SubmitHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            try {
                String id = "";
                for(int i = 0; i < 5; i++){
                    id += "" + ThreadLocalRandom.current().nextInt(0, 10);
                }
                if(usernameField.getText().equals("Enter username")){

                }
                stmt.execute("INSERT INTO blackjack.blackjack VALUES ('" +
                        id + "', '" +
                        usernameField.getText() + "', '" +
                        winCount + "', '" +
                        loseCount + "')");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cardDeck = new Deck();
            win = false;
            gameOver = false;
            winField.clear();
            usernameField.clear();
            usernameField.setText("Enter username");
            winCount = 0;
            loseCount = 0;
            winCountField.setText("Wins: " + winCount);
            lossCountField.setText("Losses: " + loseCount);
            shuffle(cardDeck.deck);
            dealerHand.hand.clear();
            playerHand.hand.clear();
            cardDeck = dealerHand.createDealerHand(cardDeck);
            cardDeck = playerHand.createPlayerHand(cardDeck);

            cardPaneDealer.getChildren().clear();
            cardPaneDealer.getChildren().add(new ImageView(dealerHand.getHand().get(0).url));
            cardPaneDealer.getChildren().add(new ImageView(dealerHand.getHand().get(1).url));

            cardPanePlayer.getChildren().clear();
            cardPanePlayer.getChildren().add(new ImageView(playerHand.getHand().get(0).url));
            cardPanePlayer.getChildren().add(new ImageView(playerHand.getHand().get(1).url));
        }
    }
}
