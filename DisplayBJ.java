package Blackjack;

import java.util.*;

public class DisplayBJ {

    public static void printBlackjackDisplay(List<String> dealersCards, List<String> playersCards) {
        System.out.println("-".repeat(140));
        System.out.println("Dealer's Hand:");

        List<String[]> dealerGraphics = new ArrayList<>();
        dealerGraphics.add(generateCard(dealersCards.get(0)));
        dealerGraphics.add(hiddenCard());

        for (int line = 0; line < 7; line++) {
            for (String[] card : dealerGraphics) {
                System.out.print(card[line] + " ");
            }
            System.out.println();
        }

        System.out.println("\nPlayer's Hand:");
        printHandSideBySide(playersCards);
        System.out.println("-".repeat(140));
    }

    public static void printStand(List<String> dealersCards, List<String> playersCards) {
        System.out.println("-".repeat(140));
        System.out.println("Dealer's Hand:");
        printHandSideBySide(dealersCards);

        System.out.println("\nPlayer's Hand:");
        printHandSideBySide(playersCards);
        System.out.println("-".repeat(140));
    }

    public static String[] generateCard(String card) {
        int totalSpace = 9;
        int padding = totalSpace - card.length();
        int left = padding / 2;
        int right = padding - left;

        String centeredLine = "│" + " ".repeat(left) + card + " ".repeat(right) + "│";

        return new String[]{
                "┌─────────┐",
                centeredLine,
                "│         │",
                "│         │",
                "│         │",
                centeredLine,
                "└─────────┘"
        };
    }

    public static String[] hiddenCard() {
        return new String[]{
                "┌─────────┐",
                "│░░░░░░░░░│",
                "│░░░░░░░░░│",
                "│░░░░░░░░░│",
                "│░░░░░░░░░│",
                "│░░░░░░░░░│",
                "└─────────┘"
        };
    }

    public static void printHandSideBySide(List<String> cards) {
        List<String[]> cardGraphics = new ArrayList<>();
        for (String card : cards) {
            cardGraphics.add(generateCard(card));
        }

        for (int line = 0; line < 7; line++) {
            for (String[] card : cardGraphics) {
                System.out.print(card[line] + " ");
            }
            System.out.println();
        }
    }

    public static void printSplit(List<String> dealerCards, List<String> playerCards, List<String> split, boolean revealDealer) {
        System.out.println("-".repeat(140));
        System.out.println("Dealer's Hand:");

        List<String[]> dealerGraphics = new ArrayList<>();
        if (revealDealer) {
            for (String card : dealerCards) {
                dealerGraphics.add(generateCard(card));
            }
        } else {
            dealerGraphics.add(generateCard(dealerCards.get(0)));
            dealerGraphics.add(hiddenCard());
        }

        for (int line = 0; line < 7; line++) {
            for (String[] card : dealerGraphics) {
                System.out.print(card[line] + " ");
            }
            System.out.println();
        }

        System.out.println("\nPlayer's Hands:");

        List<String[]> hand1Graphics = new ArrayList<>();
        for (String card : playerCards) {
            hand1Graphics.add(generateCard(card));
        }

        List<String[]> hand2Graphics = new ArrayList<>();
        for (String card : split) {
            hand2Graphics.add(generateCard(card));
        }

        for (int line = 0; line < 7; line++) {
            for (String[] card : hand1Graphics) {
                System.out.print(card[line] + " ");
            }

            System.out.print("      ");

            for (String[] card : hand2Graphics) {
                System.out.print(card[line] + " ");
            }

            System.out.println();
        }
        System.out.println("-".repeat(140));
    }
}
