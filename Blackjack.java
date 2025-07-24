package Blackjack;

import java.util.*;
import static Blackjack.DeckCreation.*;
import static Blackjack.DisplayBJ.*;
import static Blackjack.Totals.*;

public class Blackjack {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<String> deck = createDeck();

        System.out.println("Choose a Number of Decks: ");
        int numOfDecks = input.nextInt();
        Shoe shoe = new Shoe(deck, numOfDecks);

        System.out.println("How much would you like to deposit? ");
        double bankroll = input.nextDouble();
        double wager;

        while (true) {
            if (bankroll == 0) {
                System.out.println("You have no money left. Please deposit more to continue playing:");
                bankroll = input.nextDouble();
            }

            if (shoe.getPenetration() <= 0) {
                System.out.println("Reshuffling shoe...");
                shoe.reset(deck, numOfDecks);
            }

            // Wager input loop with validation
            while (true) {
                System.out.println("How much would you like to wager? (0 to quit)");
                if (input.hasNextDouble()) {
                    wager = input.nextDouble();

                    if (wager == 0) {
                        System.out.println("Thanks for playing!");
                        System.exit(0);
                    }

                    if (wager > bankroll) {
                        System.out.println("You don't have enough money to place that bet. Your balance is $" + bankroll);
                    } else {
                        break;  // valid wager entered, exit wager input loop
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    input.next();  // consume invalid token
                }
            }

            bankroll -= wager;
            bankroll = playHand(shoe, input, bankroll, wager);
        }
    }

    public static double playHand(Shoe shoe, Scanner input, double bankroll, double wager) {
        List<String> playerCards = new ArrayList<>();
        List<String> dealerCards = new ArrayList<>();
        List<String> split = new ArrayList<>();

        dealerCards.add(shoe.dealCard());
        playerCards.add(shoe.dealCard());
        dealerCards.add(shoe.dealCard());
        playerCards.add(shoe.dealCard());

        boolean isDoubleDown = false;

        printBlackjackDisplay(dealerCards, playerCards);

        boolean hasSplit = false;
        boolean playerHasBJ = isBlackjack(playerCards, hasSplit);
        boolean dealerHasBJ = isBlackjack(dealerCards, hasSplit);
        boolean dealerShowsAce = dealerCards.get(0).startsWith("A");
        boolean hasHit = false;
        int standCount = 0;

        // Even money
        if (playerHasBJ && dealerShowsAce) {
            System.out.println("You have blackjack and the dealer shows an Ace. Take even money? (Yes/No)");
            String evenMoney = input.next();

            while (!evenMoney.equalsIgnoreCase("yes") && !evenMoney.equalsIgnoreCase("no")) {
                System.out.println("Please enter Yes or No:");
                evenMoney = input.next();
            }

            if (evenMoney.equalsIgnoreCase("yes")) {
                bankroll += wager * 2;
                System.out.println("Even money taken. You win $" + wager);
                return bankroll;
            }
        }

        if (dealerShowsAce && !playerHasBJ)
            bankroll = insurance(dealerCards, input, bankroll, wager);

        if (dealerHasBJ || playerHasBJ) {
            printStand(dealerCards, playerCards);

            if (dealerHasBJ && !playerHasBJ) {
                System.out.println("Dealer has blackjack.");
                System.out.println("You lose.");
            } else if (dealerHasBJ && playerHasBJ) {
                System.out.println("Both have blackjack — Push.");
            } else if (playerHasBJ) {
                System.out.println("Blackjack!");
            }

            bankroll = payout(bankroll, wager, dealerCards, playerCards, false, hasSplit);
            System.out.println("New Balance: " + bankroll);
            return bankroll;
        }

        while (!isBust(playerCards) && cardsTotal(playerCards) != 21) {
            System.out.println("Choose your action: Hit / Stand / Split / Double");
            String choice = input.next();

            if (choice.equalsIgnoreCase("Hit")) {
                hasHit = true;

                if (hasSplit && !isBust(split) && standCount < 1) {
                    split.add(shoe.dealCard());
                } else {
                    playerCards.add(shoe.dealCard());
                }

                if (hasSplit)
                    printSplit(dealerCards, playerCards, split, false);
                else
                    printBlackjackDisplay(dealerCards, playerCards);
            } else if (choice.equalsIgnoreCase("Stand")) {
                if (hasSplit) {
                    if (standCount == 0 && isBust(split))
                        standCount++;

                    standCount++;

                    if (standCount >= 2)
                        break;
                } else
                    break;
            } else if (choice.equalsIgnoreCase("Double")) {
                if (hasHit) {
                    System.out.println("You can't double after a hit");
                } else if (bankroll >= wager) {
                    isDoubleDown = true;
                    bankroll -= wager;
                    playerCards.add(shoe.dealCard());
                    printBlackjackDisplay(dealerCards, playerCards);
                    break;
                } else {
                    System.out.println("You don't have enough money to double");
                }
            } else if (choice.equalsIgnoreCase("Split")) {
                if(hasSplit) {
                    System.out.println("You cannot re-split cards");
                    continue;
                }
                if (getCardValue(playerCards.get(0)) != getCardValue(playerCards.get(1))) {
                    System.out.println("You cannot split these cards.");
                    continue;
                }

                if (bankroll < wager) {
                    System.out.println("You don't have enough money to split.");
                    continue;
                }

                hasSplit = true;
                bankroll -= wager;

                split.add(playerCards.get(1));
                playerCards.remove(1);

                split.add(shoe.dealCard());
                playerCards.add(shoe.dealCard());

                printSplit(dealerCards, playerCards, split, false);
            }
        }

        // Dealer plays
        if (hasSplit) {
            printSplit(dealerCards, playerCards, split, true);

            if (!isBust(playerCards) || !isBust(split)) {
                while (cardsTotal(dealerCards) < 17) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    dealerCards.add(shoe.dealCard());
                    printSplit(dealerCards, playerCards, split, true);
                }
            }
        } else {
            printStand(dealerCards, playerCards);

            if (!isBust(playerCards)) {
                while (cardsTotal(dealerCards) < 17) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    dealerCards.add(shoe.dealCard());
                    printStand(dealerCards, playerCards);
                }
            }
        }

        if (hasSplit) {
            System.out.println("Result for original hand:");
            System.out.println(result(dealerCards, playerCards));
            bankroll = payout(bankroll, wager, dealerCards, playerCards, false, hasSplit);

            System.out.println("Result for split hand:");
            System.out.println(result(dealerCards, split));
            bankroll = payout(bankroll, wager, dealerCards, split, false, hasSplit);
        } else {
            System.out.println(result(dealerCards, playerCards));
            bankroll = payout(bankroll, wager, dealerCards, playerCards, isDoubleDown, hasSplit);
        }

        System.out.println("New Balance: " + bankroll);
        return bankroll;
    }
}
