package com.beto.classes;

import com.beto.e.*;

/**
 *
 * @author Alberto Félix Rosas
 */
public class Board {

    private int size;
    private int[][] matrix = null;

    public Board(int size) {
        this.size = size;
        initialize(size);
    }

    private void initialize(int size) {
        matrix = getFullMatrix(size, getValues(size)); 
        shuffle();
    }

    private int[] getValues(int size) {
        int valuesCount = size * size; // example: 3 x 3 = 9
        int[] values = new int[valuesCount];
        for (int i = 0; i < values.length; i++) {
            values[i] = i + 1;
        }
        values[values.length - 1] = 0; // the "empty" value of the matrix > zero
        return values;
    }

    public void shuffle() {
        int countOfChanges = (size * size) * (size * size);
        int[] xyOfZero = locationOfValueOnMatrix(0);
        int rowOfZero = xyOfZero[0];
        int columnOfZero = xyOfZero[1];
        int[] valuesOfTheSameLine = new int[size - 1];
        for (int i = 0; i < countOfChanges; i++) {
            int randomValue = (int) (Math.random() * 2);
            if (randomValue == 0) {
                // Change a value the horizontal axis
                int j = 0;
                for (int k = 0; k < size; k++) {
                    if (matrix[rowOfZero][k] != 0) {
                        valuesOfTheSameLine[j] = matrix[rowOfZero][k];
                        j++;
                    }
                }
            } else if (randomValue == 1) {
                // Change a value the vertical axis
                int j = 0;
                for (int k = 0; k < size; k++) {
                    if (matrix[k][columnOfZero] != 0) {
                        valuesOfTheSameLine[j] = matrix[k][columnOfZero];
                        j++;
                    }
                }
            }
            int randomIndex = (int) (Math.random() * valuesOfTheSameLine.length);
            randomValue = valuesOfTheSameLine[randomIndex];
            try {
                performMoveByNumber(randomValue);
            } catch (NotAbleToMoveException | NotValidValueException e) {
                // ignore... always the random value is valid and able to move
            }
            xyOfZero = locationOfValueOnMatrix(0);
            rowOfZero = xyOfZero[0];
            columnOfZero = xyOfZero[1];
        }
        if (isCompleted()) {
            shuffle();
        }
    }

    private int[][] getFullMatrix(int size, int[] values) {
        int[][] matrix = new int[size][size];
        int column = 0;
        int row = 0;
        for (int i = 0; i < values.length; i++) {
            matrix[row][column] = values[i];
            column++;
            if (column == size) {
                column = 0;
                row++;
            }
        }
        return matrix;
    }

    private int getNumberOfSpacesByCell() {
        return String.valueOf(size * size).length();
    }

    private String getValueWithSpaces(String value, int spacesCount) {
        int lenghtOfValue = String.valueOf(value).length();
        int missingSpaces = spacesCount - lenghtOfValue;
        String result = "";
        for (int i = 0; i < missingSpaces; i++) {
            result += ' ';
        }
        result += String.valueOf(value);
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        int numberOfSpaces = getNumberOfSpacesByCell();
        for (int i = 0; i < size; i++) {
            String row = "|";
            int[] rowNumbers = matrix[i];
            for (int j = 0; j < size; j++) {
                if (rowNumbers[j] != 0) {
                    row += getValueWithSpaces(String.valueOf(rowNumbers[j]), numberOfSpaces) + "|";
                } else {
                    row += getValueWithSpaces("", numberOfSpaces) + "|";
                }
            }
            result += row + '\n';
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    public boolean isCompleted() {
        int[][] completedMatrix = getFullMatrix(size, getValues(size));
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                int valueOfMatrix = matrix[row][column];
                int valueOfCompletedMatrix = completedMatrix[row][column];
                if (valueOfMatrix != valueOfCompletedMatrix) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidValue(int value) {
        return value > 0 && value < (size * size);
    }

    public void performMoveByNumber(int value) throws NotAbleToMoveException, NotValidValueException {
        if (!isValidValue(value)) {
            throw new NotValidValueException();
        }

        int[] xyOfValue = locationOfValueOnMatrix(value);
        int[] xyOfZero = locationOfValueOnMatrix(0);

        if (!isAbleToMove(xyOfValue, xyOfZero)) {
            throw new NotAbleToMoveException();
        }

        int rowOfValue = xyOfValue[0];
        int rowOfZero = xyOfZero[0];
        int columnOfValue = xyOfValue[1];
        int columnOfZero = xyOfZero[1];
        if (rowOfValue == rowOfZero) {
            if (columnOfValue < columnOfZero) {
                transformWhenTheValueIsLocatedOnTheLeft(rowOfValue, columnOfValue, columnOfZero);
            } else {
                transformWhenTheValueIsLocatedOnTheRight(rowOfValue, columnOfValue, columnOfZero);
            }
        }

        if (columnOfValue == columnOfZero) {
            if (rowOfValue < rowOfZero) {
                transformWhenTheValueIsLocatedAbove(columnOfValue, rowOfValue, rowOfZero);
            } else {
                transformWhenTheValueIsLocatedBelow(columnOfValue, rowOfValue, rowOfZero);
            }
        }
    }

    private void transformWhenTheValueIsLocatedOnTheLeft(int rowOfValue, int columnOfValue, int columnOfZero) {
        int[] theRow = matrix[rowOfValue];
        int countOfIterations = columnOfZero - columnOfValue;
        int index = columnOfZero;
        int i = 0;
        while (i < countOfIterations) {
            if (i != countOfIterations - 1) {
                theRow[index] = theRow[index - 1];
            } else {
                theRow[index] = theRow[index - 1];
                theRow[index - 1] = 0;
            }
            i++;
            index--;
        }

    }

    private void transformWhenTheValueIsLocatedOnTheRight(int rowOfValue, int columnOfValue, int columnOfZero) {
        int[] theRow = matrix[rowOfValue];
        int countOfIterations = columnOfValue - columnOfZero;
        int index = columnOfValue;
        int i = 0;
        int aux = 0;
        while (i <= countOfIterations) {
            if (i == 0) {
                aux = theRow[index];
                theRow[index] = 0;
            } else {
                int aux2 = aux;
                aux = theRow[index];
                theRow[index] = aux2;
            }
            i++;
            index--;
        }
    }

    private void transformWhenTheValueIsLocatedAbove(int columnOfValue, int rowOfValue, int rowOfZero) {
        int countOfIterations = rowOfZero - rowOfValue;
        int aux = matrix[rowOfValue][columnOfValue];
        int row = rowOfValue;
        for (int i = 0; i <= countOfIterations; i++) {
            if (i == 0) {
                matrix[row][columnOfValue] = 0;
            } else {
                int aux2 = aux;
                aux = matrix[row][columnOfValue];
                matrix[row][columnOfValue] = aux2;
            }
            row++;
        }
    }

    private void transformWhenTheValueIsLocatedBelow(int columnOfValue, int rowOfValue, int rowOfZero) {
        int countOfIterations = rowOfValue - rowOfZero;
        int aux = matrix[rowOfValue][columnOfValue];
        int row = rowOfValue;
        for (int i = countOfIterations; i >= 0; i--) {
            if (i == countOfIterations) {
                matrix[row][columnOfValue] = 0;
            } else {
                int aux2 = aux;
                aux = matrix[row][columnOfValue];
                matrix[row][columnOfValue] = aux2;
            }
            row--;
        }
    }

    private int[] locationOfValueOnMatrix(int value) {
        int[] xy = new int[2];
        for (int row = 0; row < this.size; row++) {
            for (int column = 0; column < this.size; column++) {
                if (matrix[row][column] == value) {
                    xy[0] = row;
                    xy[1] = column;
                    break;
                }
            }
        }
        return xy;
    }

    private boolean isAbleToMove(int[] xyOfValue, int[] xyOfZero) {
        int rowOfValue = xyOfValue[0];
        int rowOfZero = xyOfZero[0];
        if (rowOfValue == rowOfZero) {
            return true;
        }

        int columnOfValue = xyOfValue[1];
        int columnOfZero = xyOfZero[1];
        return columnOfValue == columnOfZero;
    }

}
