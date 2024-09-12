import org.example.LocalBoard;
import org.example.Symbol;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

// naprawić
public class GameTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/game_test.csv", numLinesToSkip = 1)
    public void testGameResult(String boardState, String expectedResult) {
        LocalBoard localBoard = new LocalBoard();

        // Debugging line
        System.out.println("boardState: [" + boardState + "]");
        System.out.println("expectedResult: [" + expectedResult + "]");

        // Konwersja boardState (String) na dwuwymiarową tablicę Symbol
        Symbol[][] board = new Symbol[3][3];
        String[] symbols = boardState.split(",");

        // Debugging line
        System.out.println("Parsed symbols: " + String.join(",", symbols));

        // Sprawdzenie, czy liczba symboli jest poprawna
        if (symbols.length != 9) {
            throw new RuntimeException("Invalid number of symbols in CSV: " + symbols.length);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                try {
                    board[i][j] = Symbol.valueOf(symbols[i * 3 + j]);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid symbol in CSV: " + symbols[i * 3 + j], e);
                }
            }
        }

        // Ustawienie planszy
        localBoard.setBoard(board);

        // Ustalenie wyniku gry
        String result = localBoard.determineResult();

        // Sprawdzenie, czy wynik gry jest zgodny z oczekiwanym
        assertEquals(expectedResult, result);
    }
}