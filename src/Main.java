import java.util.*;

public class Main {
    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private char whoseTurn;
    private final Player player;
		private final Player player2;
    private final Player computer;
    private List<Card> deck;
    private final Scanner in;

    public Main() {
        this.whoseTurn = 'P';
        this.player = new Player();
				this.player2 = new Player();
        this.computer = new Player();
        this.in = new Scanner(System.in);
    }

    public void play() {
			 String playerNum = "";
				while(!playerNum.equals("S") && !playerNum.equals("M")) {
				System.out.print("Singleplayer or multiplayer? (Type 's' for singleplayer and 'm' for multiplayer.) ");
				playerNum = in.nextLine().trim().toUpperCase(); 
			}
       if (playerNum.equals("S")) {
				 new Main().playSingleplayer();
			 } else if (playerNum.equals("M")) {
				 new Main().playMultiplayer();
			 }
    }
		public void playSingleplayer() {
			 shuffleAndDeal();

        // play the game until someone wins

        while (true) {
            if (whoseTurn == 'P') {
                whoseTurn = takeTurn(false);

                if (player.findAndRemoveBooks()) {
                    System.out.println("PLAYER: Oh, that's a book!");
                    showBooks(false);
                }
            } else if (whoseTurn == 'C') {
                whoseTurn = takeTurn(true);

                if (computer.findAndRemoveBooks()) {
                    System.out.println("CPU: Oh, that's a book!");
                    showBooks(true);
                }
            }

            // the games doesn't end until all 13 books are completed, or there are
            // no more cards left in the deck. the player with the ,ost books at the
            // end of the game wins.

            int playerBooks = player.getBooks().size();
            int computerBooks = computer.getBooks().size();

            String winMessage = "Congratulations, you win! " + playerBooks + " books to " + computerBooks + ".";
            String loseMessage = "Maybe next time. You lose " + computerBooks + " books to " + playerBooks + ".";
            String tieMessage = "Looks like it's a tie, " + playerBooks + " to " + computerBooks + ".";

            if (playerBooks + computerBooks == 13) {
                if (player.getBooks().size() > computer.getBooks().size()) {
                    System.out.println("\n" + winMessage);
                } else {
                    System.out.println("\n" + loseMessage);
                }
                break;
            } else if (deck.size() == 0) {
                System.out.println("\nOh no, there are no more cards in the deck!");

                if (playerBooks > computerBooks) {
                    System.out.println(winMessage);
                } else if (computerBooks > playerBooks) {
                    System.out.println(loseMessage);
                } else {
                    System.out.println(tieMessage);
                }
                break;
            }
        }
		}

		public void playMultiplayer() {
			multiplayerSetup();

			while(true) {
			if (whoseTurn == 'P') {
      whoseTurn = takeTurnMultiplayer(false);

      if (player.findAndRemoveBooks()) {
         System.out.println("PLAYER1: Oh, that's a book!");
          showBooksMultiplayer(false);
         }
         } else if (whoseTurn == 'Q') {
            whoseTurn = takeTurnMultiplayer(true);

         if (computer.findAndRemoveBooks()) {
            System.out.println("PLAYER2: Oh, that's a book!");
            showBooksMultiplayer(true);
					}
				}
				//adsfghkjlsjgdkjhfksgdk
				 int playerBooks = player.getBooks().size();
         int player2Books = player2.getBooks().size();

            String winMessage = "Congratulations, PLAYER 1 wins! " + playerBooks + " books to " + player2Books + ".";
            String loseMessage = "Congratulations, PLAYER 2 wins! " + player2Books + " books to " + playerBooks + ".";
            String tieMessage = "Looks like it's a tie, " + playerBooks + " to " + player2Books + ".";

            if (playerBooks + player2Books == 13) {
                if (player.getBooks().size() > player2.getBooks().size()) {
                    System.out.println("\n" + winMessage);
                } else {
                    System.out.println("\n" + loseMessage);
                }
                break;
            } else if (deck.size() == 0) {
                System.out.println("\nOh no, there are no more cards in the deck!");

                if (playerBooks > player2Books) {
                    System.out.println(winMessage);
                } else if (player2Books > playerBooks) {
                    System.out.println(loseMessage);
                } else {
                    System.out.println(tieMessage);
                }
                break;
            }
			}
		}

    public void shuffleAndDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);  // shuffles the deck

        while (player.getHand().size() < 7) {
            player.takeCard(deck.remove(0));    // deal 7 cards to the
            computer.takeCard(deck.remove(0));  // player and the computer
        }
    }

		public void multiplayerSetup() {
			if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck); 

        while (player.getHand().size() < 7) {
            player.takeCard(deck.remove(0)); 
            player2.takeCard(deck.remove(0)); 
        }
		}

    ////////// PRIVATE METHODS /////////////////////////////////////////////////////

    private void initializeDeck() {
        deck = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));     // adds 52 cards to the deck (13 ranks, 4 suits)
            }
        }
    }
		
		private char takeTurnMultiplayer(boolean playerTwo) {
			showHandMultiplayer(playerTwo);
			showBooksMultiplayer(playerTwo);
			Card card = requestCardMultiplayer(playerTwo);
			if (card == null) {
				return playerTwo ? 'Q' : 'P';
			}
			if (!playerTwo) {
            if (player2.hasCard(card)) {
                System.out.println("PLAYER 2: Yup, here you go!");
                computer.relinquishCard(player, card);

                return 'P';
            } else {
                System.out.println("PLAYER 2: Nope, go fish!");
                player.takeCard(deck.remove(0));

                return 'Q';
            }
        } else {
            if (player.hasCard(card)) {
                System.out.println("PLAYER 2: Oh, you do? Well, hand it over!");
                player.relinquishCard(player2, card);

                return 'Q';
            } else {
                System.out.println("PLAYER 2: Ah, I guess I'll go fish...");
                player2.takeCard(deck.remove(0));


                return 'P';
            }
        }
		}
    private char takeTurn(boolean cpu) {
        showHand(cpu);
        showBooks(cpu);

        // if requestCard returns null, then the hand was empty and new card was drawn.
        // this restarts the turn, ensuring the updated hand is printed to the console.

        Card card = requestCard(cpu);
        if (card == null) {
            return cpu ? 'C' : 'P';     // restart this turn with updated hand
        }

        // check if your opponent has the card you requested. it will be automatically
        // relinquished if you do. otherwise, draw from the deck. return the character
        // code for whose turn it should be next.

        if (!cpu) {
            if (computer.hasCard(card)) {
                System.out.println("CPU: Yup, here you go!");
                computer.relinquishCard(player, card);

                return 'P';
            } else {
                System.out.println("CPU: Nope, go fish!");
                player.takeCard(deck.remove(0));

                return 'C';
            }
        } else {
            if (player.hasCard(card)) {
                System.out.println("CPU: Oh, you do? Well, hand it over!");
                player.relinquishCard(computer, card);

                return 'C';
            } else {
                System.out.println("CPU: Ah, I guess I'll go fish...");
                computer.takeCard(deck.remove(0));


                return 'P';
            }
        }
    }

    private Card requestCard(boolean cpu) {
        Card card = null;

        while (card == null) {
            if (!cpu) {
                if (player.getHand().size() == 0) {
                    player.takeCard(deck.remove(0));

                    return null;
                } else {
                    System.out.print("PLAYER: Got any... ");
                    String rank = in.nextLine().trim().toUpperCase();
                    card = Card.getCardByRank(rank);
                }
            } else {
                if (computer.getHand().size() == 0) {
                    computer.takeCard(deck.remove(0));

                    return null;
                } else {
                    card = computer.getCardByNeed();
                    System.out.println("CPU: Got any... " + card.getRank());
                }
            }
        }

        return card;
    }

		private Card requestCardMultiplayer(boolean playerTwo) {
        Card card = null;

        while (card == null) {
            if (!playerTwo) {
                if (player.getHand().size() == 0) {
                    player.takeCard(deck.remove(0));

                    return null;
                } else {
                    System.out.print("PLAYER 1: Got any... ");
                    String rank = in.nextLine().trim().toUpperCase();
                    card = Card.getCardByRank(rank);
                }
            } else {
                if (player2.getHand().size() == 0) {
                    player2.takeCard(deck.remove(0));

                    return null;
                } else {
                    System.out.print("PLAYER 2: Got any... ");
                    String rank = in.nextLine().trim().toUpperCase();
                    card = Card.getCardByRank(rank);
                }
            }
        }

        return card;
    }
    private void showHand(boolean cpu) {
        if (!cpu) {
            System.out.println("\nPLAYER hand: " + player.getHand());   // only show player's hand
        }
    }

		private void showHandMultiplayer(boolean playerTwo) {
        if (!playerTwo) {
            System.out.println("\nPLAYER 1 hand: " + player.getHand()); 
        } else {
					System.out.println("\nPLAYER 2 hand: " + player2.getHand());
				}
    }

    private void showBooks(boolean cpu) {
        if (!cpu) {
            System.out.println("PLAYER books: " + player.getBooks());   // shows the player's books
        } else {
            System.out.println("\nCPU books: " + computer.getBooks());  // shows the computer's books
        }
    }

		private void showBooksMultiplayer(boolean playerTwo) {
			if(!playerTwo) {
				System.out.println("PLAYER 1 books: " + player.getBooks());
			} else {
				System.out.println("PLAYER 2 books: " + player2.getBooks());
			}
		}

    ////////// MAIN METHOD /////////////////////////////////////////////////////////

    public static void main(String[] args) {
        System.out.println("#########################################################");
        System.out.println("#                                                       #");
        System.out.println("#   ####### #######   ####### ####### ####### #     #   #");
        System.out.println("#   #       #     #   #          #    #       #     #   #");
        System.out.println("#   #  #### #     #   #####      #    ####### #######   #");
        System.out.println("#   #     # #     #   #          #          # #     #   #");
        System.out.println("#   ####### #######   #       ####### ####### #     #   #");
        System.out.println("#                                                       #");
        System.out.println("#                                                       #");
        System.out.println("#########################################################\n");

				new Main().play();
    }
}
