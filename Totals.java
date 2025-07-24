package Blackjack;

import java.util.*;

public class Totals {
    public static int getCardValue(String card) {
        String rank = card.substring(0, card.length() - 1); // cut off the suit
        switch (rank) {
            case "A":
                return 11;
            case "K":
            case "Q":
            case "J":
                return 10;
            default:
                return Integer.parseInt(rank);
        }
    }

    public static int cardsTotal(List<String> cards) {
        int total = 0;
        int aceCount = 0;

        for (String card : cards) {
            int value = getCardValue(card);
            total += value;
            if (card.startsWith("A")) aceCount++;
        }

        // Adjust Aces from 11 to 1 if needed
        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }

    public static double insurance(List<String> dealerCards, Scanner input, double bankroll, double wager) {
        if(bankroll >= (wager / 2)){
            System.out.println("Would you like to buy insurance? ");
            String insurance = input.next();

            while (!insurance.equalsIgnoreCase("yes") && !insurance.equalsIgnoreCase("no")) {
                System.out.println("Yes or No:");
                insurance = input.next();
            }

            boolean boughtInsurance = insurance.equalsIgnoreCase("yes");

            if (boughtInsurance) {
                bankroll -= wager / 2;
            }

            if (getCardValue(dealerCards.get(1)) == 10) {
                if (boughtInsurance) {
                    return bankroll + wager + (wager / 2);
                } else {
                    return bankroll;
                }
            }
        }
        return bankroll; // No insurance or dealer doesn't show Ace
    }


    public static boolean isBlackjack(List<String> cards, boolean hasSplit){
        if(hasSplit)
            return false;
        else if(cardsTotal(cards.subList(0,2)) == 21)
            return true;
        else
            return false;
    }


    public static boolean isBust(List<String> cards){
        if(cardsTotal(cards) > 21)
            return true;
        else
            return false;
    }

    public static String result(List<String> dealerCards, List<String> playerCards){
        if(cardsTotal(playerCards) > cardsTotal(dealerCards) && !isBust(playerCards) || isBust(dealerCards) && !isBust(playerCards))
            return "You Win!";
        else if (cardsTotal(playerCards) == cardsTotal(dealerCards) && !isBust(dealerCards) && !isBust(playerCards))
            return "Push";
        else if (cardsTotal(dealerCards) > cardsTotal(playerCards) && !isBust(dealerCards) || isBust(playerCards))
            return "You Lose!";
        else
            return "I Broke";
    }

    public static double payout(double bankroll, double wager, List<String> dealerCards, List<String> playerCards, boolean isDoubleDown, boolean hasSplit) {
        double finalWager = isDoubleDown ? wager * 2 : wager;
        String outcome = result(dealerCards, playerCards);

        if (isBlackjack(playerCards, hasSplit) && !isBlackjack(dealerCards, hasSplit)) {
            return bankroll + (wager * 2.5);
        } else if (outcome.equalsIgnoreCase("You Win!")) {
            return bankroll + (finalWager * 2);
        } else if (outcome.equalsIgnoreCase("Push")) {
            return bankroll + finalWager;
        } else {
            return bankroll; // player loses
        }
    }

}
