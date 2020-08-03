package game;
import ehs.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {
    public Deck Temp = new Deck();
    public Deck deck = new Deck();
    public Card card = Temp.dealOne();
    public int playerSlot = 1;
    public int dealerSlot = 1;
    public int countPlayer = 0;
    public int countDealer = 0;
    public static double yPos;
    public static double xPos;
    public static double xPoint;
    public static double yPoint;
    public AnimationTimer deal;
    public AnimationTimer beginDraws;
    public AnimationTimer hideMenu;
    public AnimationTimer showWinLose;
    public AnimationTimer hideWinLose;
    public AnimationTimer revealDealer;
    public AnimationTimer showBetting;
    public AnimationTimer hideBetting;
    public boolean dealing = false;
    public boolean doneFirst = false;
    public boolean doneSecond = false;
    public boolean doneThird = false;
    public boolean betting = false;
    public static boolean quit = false;
    @FXML ImageView player1;
    @FXML ImageView player2;
    @FXML ImageView player3;
    @FXML ImageView player4;
    @FXML ImageView player5;
    @FXML ImageView player6;
    @FXML ImageView dealer1;
    @FXML ImageView dealer2;
    @FXML ImageView dealer3;
    @FXML ImageView dealer4;
    @FXML ImageView dealer5;
    @FXML ImageView dealer6;
    @FXML Label playerCount;
    @FXML Label dealerCount;
    @FXML ImageView dealingCard;
    @FXML ImageView deckZone;
    @FXML AnchorPane mainMenu;
    @FXML AnchorPane winLose;
    @FXML AnchorPane betWindow;
    public double menuYpos;
    public double menuXpos;
    public boolean sliding = false;
    public boolean doneDealing = false;
    public boolean gameOver = false;
    @FXML Label outcome;
    @FXML Label balance;
    @FXML
    TextField betInput;
    public int bal = 1000;
    public int bet = 0;

    public Controller(){
        deck.shuffle();
    }

    /*
        Closes the program
     */
    @FXML
    public void quit(){
        quit = true;
    }

    /*
        Used to reset the screen position in specific cases.
     */
    @FXML
    public void fixScreen(){
        showWinLose.stop();
        winLose.setLayoutY(0);
    }

    /*
        Sets up the game to begin play
     */
    @FXML
    public void startGame(){
        balance.setText(String.valueOf(bal));
        hideMenuSlide();
        showBetting();
        begin();
    }

    /*
        Slides the menu out of view.
     */
    public void hideMenuSlide(){
        menuYpos = mainMenu.getLayoutY();
        sliding = true;
        hideMenu = new AnimationTimer(){
            @Override
            public void handle(long now){
                if(menuYpos == -400)
                    stopAnimationTimer(hideMenu);
                menuYpos-=5;
                mainMenu.setLayoutY(menuYpos);
            }
        };
        hideMenu.start();
    }


    /*
        Slides the win/lose end screen out of view.
     */
    public void hideWinLoseSlide(){
        menuYpos = winLose.getLayoutY();
        sliding = true;
        hideWinLose = new AnimationTimer() {
            @Override
            public void handle(long now) {
                menuYpos+=5;
                winLose.setLayoutY(menuYpos);
                if(menuYpos == 400)
                    stopAnimationTimer(hideWinLose);
            }
        };
        hideWinLose.start();
    }

    /*
        Slides the win/lose end screen into view.
     */
    public void showWinLoseSlide(){
        menuYpos = winLose.getLayoutY();
        sliding = true;
        showWinLose = new AnimationTimer() {
            @Override
            public void handle(long now) {
                menuYpos-=5;
                winLose.setLayoutY(menuYpos);
                if(winLose.getLayoutY() <= 0)
                    fixScreen();
            }
        };

        showWinLose.start();
    }

    /*
        Resets the various animation timers used to slide the menus in and out of view.
     */
    public void stopAnimationTimer(AnimationTimer timer){
        System.out.println("stop initiated");
        if(timer == deal)
            deal.stop();
        else if(timer == hideMenu)
            hideMenu.stop();
        else if(timer == showWinLose)
            showWinLose.stop();
        else if(timer == hideWinLose)
            hideWinLose.stop();
        else if(timer == beginDraws)
            beginDraws.stop();
        else if(timer == revealDealer)
            revealDealer.stop();
        else if(timer == showBetting)
            showBetting.stop();
        else if(timer == hideBetting)
            hideBetting.stop();
        sliding = false;
    }

    /*
        Slides the betting menu into view.
     */
    public void showBetting(){
        if(!betting) {
            betting = true;
            balance.setText(String.valueOf(bal));
            menuXpos = betWindow.getLayoutX();
            showBetting = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    System.out.println(betWindow.getLayoutX());
                    if (betWindow.getLayoutX() == 0)
                        stopAnimationTimer(showBetting);
                    menuXpos += 5;
                    betWindow.setLayoutX(menuXpos);
                }
            };
            showBetting.start();
        }
    }

    /*
        Slides the betting menu out of view.
     */
    public void hideBetting(){
        menuXpos = betWindow.getLayoutX();
        hideBetting = new AnimationTimer() {
            @Override
            public void handle(long now) {
                System.out.println(betWindow.getLayoutX());
                if(betWindow.getLayoutX() == -200){
                    stopAnimationTimer(hideBetting);
                    betting = false;
                }
                menuXpos -= 5;
                betWindow.setLayoutX(menuXpos);
            }
        };
        hideBetting.start();
    }

    /*
        Sets the user's bet and hides the betting menu.
     */
    @FXML
    public void confirmBet(){
        bet = Integer.valueOf(betInput.getText());
        hideBetting();
    }


    /*
        Deals out the first cards to the player and dealer.
     */
    public void begin(){
        doneDealing = false;
        beginDraws = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(doneDealing)
                    stopAnimationTimer(beginDraws);
                if(!dealing && !doneFirst && !doneSecond && !doneThird && !sliding && !betting){
                    dealCard('p');
                    doneFirst=true;
                }
                if(!dealing && doneFirst && !sliding && !betting){
                    dealCard('p');
                    doneSecond=true;
                    doneFirst=false;
                }
                if(!dealing && doneSecond && !sliding && !betting){
                    dealCard('d');
                    doneThird = true;
                    doneSecond = false;
                }
                if(!dealing && doneThird && !sliding && !betting){
                    dealCard('b');
                    doneDealing = true;
                }
            }
        };
        beginDraws.start();
    }

    /*
        Deals an additional card to the user.
     */
    @FXML
    public void hit(){
        if(!dealing)
            dealCard('p');
        checkBJandOver('p');
    }

    /*
        Ends the users turn and turns over the dealer's card and draws another if necessary.
     */
    @FXML
    public void stand(){
        if(!dealing) {
            drawCard('d');
            Delay.wait(100);
            revealDealer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (countDealer < 17 && !dealing) {
                        dealCard('d');
                        Delay.wait(100);
                    }
                    else if (!dealing){
                        checkWinner();
                        stopAnimationTimer(revealDealer);
                    }
                }
            };
            revealDealer.start();

        }
    }

    /*
        Finds the winner and sets the proper text for the win/lose screen.
     */
    public void checkWinner(){
        if (countPlayer > countDealer && !gameOver && countDealer > 16) {
            outcome.setText("Congratulations you beat the dealer!\n Dealer's Count = " + countDealer + "   Your Count = " + countPlayer + "\nYou Won $" + bet);
            bal += bet;
            stopAnimationTimer(revealDealer);
            showWinLoseSlide();
        } else if (countPlayer <= countDealer && !gameOver && countDealer > 16) {
            outcome.setText("Darn, the dealer beat you!\n Dealer's Count = " + countDealer + "   Your Count = " + countPlayer + "\nYou Lost $" + bet);
            bal -= bet;
            stopAnimationTimer(revealDealer);
            showWinLoseSlide();
        }
    }

    /*
        Deals a card to the specified player.
        @param - char specifying player or dealer
     */
    public void dealCard(char user){
        dealing = true;
        xPos = dealingCard.getLayoutX();
        yPos = dealingCard.getLayoutY();
        double targetX,targetY;
        if(user == 'p') {
            targetX = getCardSlotX();
            targetY = getCardSlotY();
        }
        else{
            targetX = getDealerSlotX();
            targetY = getDealerSlotY();
        }
        xPoint = (xPos-targetX)/100;
        yPoint = (yPos-targetY)/100;
        System.out.println("\nxPos: " + xPos +"\nyPos: " + yPos + "\ntargetX: " + targetX + "\ntargetY: " + targetY + "\nxPoint: " + xPoint + "\nyPoint: " + yPoint);

        deal = new AnimationTimer() {
            @Override
            public void handle(long now) {
                xPos -= xPoint;
                yPos -= yPoint;
                dealingCard.setLayoutX(xPos);
                dealingCard.setLayoutY(yPos);
                if (xPos < targetX) {
                    drawCard(user);
                    stopAnimationTimer(deal);
                }
            }
        };
        deal.start();
    }

    /*
        Finds the x-position the next card should be in.
        @return - the x-position of the next player card slot
     */
    public double getCardSlotX(){
        switch (playerSlot){
            case 1:
                return player1.getLayoutX();
            case 2:
                return player2.getLayoutX();
            case 3:
                return player3.getLayoutX();
            case 4:
                return player4.getLayoutX();
            case 5:
                return player5.getLayoutX();
            case 6:
                return player6.getLayoutX();
        }
        return 0;
    }

    /*
        Finds the y-position the next card should be in.
        @return - the y-position of the next player card slot
     */
    public double getCardSlotY(){
        switch (playerSlot){
            case 1:
                return player1.getLayoutY();
            case 2:
                return player2.getLayoutY();
            case 3:
                return player3.getLayoutY();
            case 4:
                return player4.getLayoutY();
            case 5:
                return player5.getLayoutY();
            case 6:
                return player6.getLayoutY();
        }
        return 0;
    }

    /*
        Finds the x-position the next dealer card should be in.
        @return - the x-position of the next dealer slot
     */
    public double getDealerSlotX(){
        switch (dealerSlot){
            case 1:
                return dealer1.getLayoutX();
            case 2:
                return dealer2.getLayoutX();
            case 3:
                return dealer3.getLayoutX();
            case 4:
                return dealer4.getLayoutX();
            case 5:
                return dealer5.getLayoutX();
            case 6:
                return dealer6.getLayoutX();
        }
        return 0;
    }

    /*
        Finds the y-position the next dealer card should be in.
        @return - the y-position of the next dealer slot
     */
    public double getDealerSlotY(){
        switch (dealerSlot){
            case 1:
                return dealer1.getLayoutY();
            case 2:
                return dealer2.getLayoutY();
            case 3:
                return dealer3.getLayoutY();
            case 4:
                return dealer4.getLayoutY();
            case 5:
                return dealer5.getLayoutY();
            case 6:
                return dealer6.getLayoutY();
        }
        return 0;
    }

    /*
        Draws a card from the deck and assigns it to either the player or dealer.
        @param - char specifying the player or the dealer
     */
    public void drawCard(char user){
        dealingCard.setLayoutX(deckZone.getLayoutX());
        dealingCard.setLayoutY(deckZone.getLayoutY());
        card = deck.dealOne();
        System.out.print(card.suit() + "\t" + card.face());
        if(user == 'p') {
            switch (playerSlot) {
                case 1:
                    assignCard(player1);
                    break;
                case 2:
                    assignCard(player2);
                    break;
                case 3:
                    assignCard(player3);
                    break;
                case 4:
                    assignCard(player4);
                    break;
                case 5:
                    assignCard(player5);
                    break;
                case 6:
                    assignCard(player6);
                    break;
            }
            playerSlot++;
            countPlayer += card.BJvalue();
            playerCount.setText(String.valueOf(countPlayer));
        }
        else if(user == 'b') {
            switch (dealerSlot){
                case 1: assignBack(dealer1);
                    break;
                case 2: assignBack(dealer2);
                    break;
                case 3: assignBack(dealer3);
                    break;
                case 4: assignBack(dealer4);
                    break;
                case 5: assignBack(dealer5);
                    break;
                case 6: assignBack(dealer6);
                    break;
            }
            doneDealing = true;
        }
        else{
            switch (dealerSlot) {
                case 1:
                    assignCard(dealer1);
                    break;
                case 2:
                    assignCard(dealer2);
                    break;
                case 3:
                    assignCard(dealer3);
                    break;
                case 4:
                    assignCard(dealer4);
                    break;
                case 5:
                    assignCard(dealer5);
                    break;
                case 6:
                    assignCard(dealer6);
                    break;
            }
            dealerSlot++;
            countDealer += card.BJvalue();
            dealerCount.setText(String.valueOf(countDealer));
        }
        dealing = false;
        checkBJandOver(user);
    }

    /*
        Checks for a blackjack or if the specified user has gone over.
        @param - char specifying player or dealer
     */
    public void checkBJandOver(char user){
        switch (user){
            case 'p': {
                if(countPlayer == 21)
                    blackJack('p');
                else if(countPlayer > 21)
                    over('p');
            }
                break;
            case 'd':{
                if(countDealer == 21)
                    blackJack('d');
                else if(countDealer >= 22)
                    over('d');
            }
                break;
        }
    }

    /*
        Sets the outcome text to correctly display which user has gone over.
        @param - char specify whether the player or dealer has gone over
     */
    public void over(char user){
        switch(user){
            case 'p':{
                outcome.setText("Your count has gone over 21.\nYou have lost this game!\nYou Lost $" + bet);
                bal -= bet;
                gameOver = true;
                showWinLoseSlide();
            }
                break;
            case 'd':{
                outcome.setText("The dealer's count has gone over 21.\nYou have won this game!\nYou Won $" + bet);
                bal += bet;
                gameOver = true;
                showWinLoseSlide();
            }
                break;
        }
    }

    /*
        Sets the outcome text to correctly display which user has gotten a black jack.
        @param - char specify whether the player or dealer has gotten a black jack
     */
    public void blackJack(char user){
        switch(user){
            case 'p':{
                outcome.setText("Your count is exactly 21!\nYou have gotten a blackjack! You have won this game!\nYou Won $" + bet);
                bal += bet;
                gameOver = true;
                showWinLoseSlide();
            }
                break;
            case 'd':{
                outcome.setText("The dealer's count is exactly 21!\nThey have gotten a blackjack! You have lost this game!\nYou Won $" + bet);
                bal -= bet;
                gameOver = true;
                showWinLoseSlide();
            }
                break;
        }
    }

    /*
        Resets all the timers, text labels, images, and booleans to begin a new game.
     */
    @FXML
    public void restart(){
        if(!betting){
            deck.shuffle();
            String blankSpace = "game/images/emptySlot.png";
            dealerSlot = 1;
            playerSlot = 1;
            countDealer = 0;
            countPlayer = 0;
            playerCount.setText(String.valueOf(0));
            dealerCount.setText(String.valueOf(0));
            player1.setImage(new Image(blankSpace));
            player2.setImage(new Image(blankSpace));
            player3.setImage(new Image(blankSpace));
            player4.setImage(new Image(blankSpace));
            player5.setImage(new Image(blankSpace));
            player6.setImage(new Image(blankSpace));
            dealer1.setImage(new Image(blankSpace));
            dealer2.setImage(new Image(blankSpace));
            dealer3.setImage(new Image(blankSpace));
            dealer4.setImage(new Image(blankSpace));
            dealer5.setImage(new Image(blankSpace));
            dealer6.setImage(new Image(blankSpace));
            betWindow.setLayoutX(-200);
            hideWinLoseSlide();
            doneFirst = false;
            doneSecond = false;
            doneThird = false;
            gameOver = false;
            begin();
        }
    }

    /*
        Sets the specified imageview slot to the back of the card image.
        @param - the imageview to be assigned the image
     */
    public void assignBack(ImageView slot){
        slot.setImage(new Image("game/images/blue_back.png"));
    }

    /*
        Sets the specified imageview slot to the image associated with the card drawn.
        @param - the imageview to be assigned the card specific image
     */
    public void assignCard(ImageView slot){
        String cardFace = card.face();
        switch(cardFace){
            case "Queen": cardFace = "" + card.face().charAt(0);
                break;
            case "King": cardFace = "" + card.face().charAt(0);
                break;
            case "Jack": cardFace = "" + card.face().charAt(0);
                break;
            case "Ace": cardFace = "" + card.face().charAt(0);
                break;
            default:
                break;
        }
        String Card = "game\\images\\" + cardFace + card.suit().charAt(0) + ".png";
        slot.setImage(new Image(Card));
    }
}
