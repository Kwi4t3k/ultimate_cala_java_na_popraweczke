import org.example.HumanPlayer;
import org.example.LocalBoard;
import org.example.Player;
import org.example.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class GameTest {
    @ParameterizedTest
    @CsvFileSource(files = "src/main/resources/game_test.csv", numLinesToSkip = 1)
    public void testIfGameHasEnded(String board, String expectedResult) {
        LocalBoard localBoard = new LocalBoard();

        // Ustawienie planszy zgodnie z danymi z pliku CSV
        String[] symbols = board.split(",");
        int index = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Symbol symbol;
                switch (symbols[index++]) {
                    case "KRZYZYK":
                        symbol = Symbol.KRZYZYK;
                        break;
                    case "KOLKO":
                        symbol = Symbol.KOLKO;
                        break;
                    default:
                        symbol = Symbol.PUSTE_POLE;
                }
                localBoard.setSymbol(symbol, i, j);
            }
        }

        // Utworzenie graczy
        Player player1 = new HumanPlayer(Symbol.KOLKO, localBoard);
        Player player2 = new HumanPlayer(Symbol.KRZYZYK, localBoard);

        // Uruchomienie gry
        localBoard.playGame(player1, player2);

        // Sprawdzenie wyniku
        String result = localBoard.getGameResult();
        Assertions.assertEquals(expectedResult, result);
    }
}
