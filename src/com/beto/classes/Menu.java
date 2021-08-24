package com.beto.classes;

import com.beto.e.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

/**
 *
 * @author Alberto Félix Rosas
 */
public class Menu {

    private static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {
        Board board = getConfiguratedBoardByUser();
        Instant starts = Instant.now();
        play(board);
        Instant ends = Instant.now();
        Duration duration = Duration.between(starts, ends);
        System.out.println("¡Lo lograste!\nFinalizaste el juego, felicidades");
        long durationInSeconds = duration.getSeconds();
        System.out.println("¡Tu tiempo fue de " + durationInSeconds + " segundos!");
    }

    private static Board getConfiguratedBoardByUser() {
        int size = 0;
        while (true) {
            while (true) {
                size = getInteger("¿Qué tamaño tendrá el tablero?\nTamaño: ");
                if (size > 1) {
                    break;
                }
                System.out.println("El tamaño del tablero como minimo es 2");
            }
            Board board = new Board(size);
            System.out.println(board);
            int option = getInteger("[1] Continuar así\n[Otro numero] Generar tablero de nuevo\nOpción: ");
            if (option == 1) {
                return board;
            }
        }
    }

    private static void play(Board board) {
        while (true) {
            System.out.println(board);
            int value = getInteger("Introduzca un valor para mover el tablero\n"
                    + "El valor vacío tiene que encontrarse en la esquina inferior derecha\n"
                    + "Valor:");
            try {
                board.performMoveByNumber(value);
            } catch (NotAbleToMoveException | NotValidValueException e) {
                System.out.println(e.getMessage());
            }
            if (board.isCompleted()) {
                System.out.println(board);
                break;
            }
        }
    }

    public static int getInteger(String message) {
        while (true) {
            System.out.print(message);
            String input = keyboard.nextLine();
            if (input.length() == 0) {
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un numero entero");
            }
        }
    }

}
