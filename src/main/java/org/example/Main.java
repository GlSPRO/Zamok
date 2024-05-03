import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите ширину бункера:");
        int rows = scanner.nextInt();
        System.out.println("Введите длину бункера:");
        int cols = scanner.nextInt();

        Bunker bunker = new Bunker(rows, cols);

        // Рисуем бункер
        System.out.println("Бункер размера:" + rows + " * " + cols);
        bunker.draw();

        while (true) {
            System.out.println("Выберите, что хотите сделать с бункером:\n" +
                    "     1. Разрушить бункер\n     2. Восстановить бункер\n" +
                    "     3. Проверить запасы продовольствий\n" +
                    "     4. Обновить запасы продовольствий\n" +
                    "     5. Проверить состояние бункера");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    bunker.destroyBunker();
                    System.out.println("Разрушенный бункер:");
                    bunker.draw();
                    break;
                case 2:
                    System.out.println("Восстановленный бункер:");
                    bunker.restoreBunker();
                    bunker.draw();
                    break;
                case 3:
                    int[] supplies = {100, 50, 75};
                    int consumptionPerDay = 10;

                    int remainingDays = bunker.howMuchDays(supplies, consumptionPerDay);
                    System.out.println("Запасы продержатся еще " + remainingDays + " дней");
                    break;
                case 4:
                    int[] restockAmounts = {20, 30, 10};
                    bunker.restockSupplies(restockAmounts);
                    System.out.println("Запасы обновлены");
                    break;
                case 5:
                    System.out.println("Состояние бункера: " + bunker.checkBunkerStatus());
                    break;
                default:
                    System.out.println("Выберите что-то из доступных действий");
            }
        }
    }
}

class Bunker {
    private int[][] layout;
    private int[] supplies;

    public Bunker(int rows, int cols) {
        this.layout = generateRandomLayout(rows, cols);
        this.supplies = new int[3];
    }

    private int[][] generateRandomLayout(int rows, int cols) {
        int[][] layout = new int[rows][cols];
        // Заполняем бункер псевдослучайным образом
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Обнуляем все клетки, кроме контура
                if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1) {
                    layout[i][j] = 1;
                }
            }
        }
        return layout;
    }

    public void draw() {
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                if (layout[i][j] == 1) {
                    System.out.print("# "); // символ для стены
                } else {
                    System.out.print("  "); // символ для пустого места
                }
            }
            System.out.println();
        }
    }

    public void destroyBunker() {
        Random random = new Random();
        // Определяем количество символов для изменения
        int symbolsToChange = (int) (layout.length * layout[0].length * 0.1); // 10% от общего количества символов
        int changedSymbols = 0;

        // Меняем символы на пробелы
        while (changedSymbols < symbolsToChange) {
            int randomRow = random.nextInt(layout.length - 2); // случайная строка, исключая края
            int randomCol = random.nextInt(layout[0].length - 2); // случайный столбец, исключая края

            // Проверяем, не меняли ли уже символ в этой позиции
            if (layout[randomRow][randomCol] == 1) {
                layout[randomRow][randomCol] = 0; // меняем символ на пробел
                changedSymbols++;
            }
        }
    }

    public void restoreBunker() {
        // Восстанавливаем контур бункера
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                if (i == 0 || j == 0 || i == layout.length - 1 || j == layout[0].length - 1) {
                    layout[i][j] = 1; // устанавливаем символы контура обратно
                }
            }
        }
    }

    public int howMuchDays(int[] supplies, int consumptionPerDay) {
        // Рассчитываем оставшееся количество дней
        int totalSupplies = 0;
        for (int supply : supplies) {
            totalSupplies += supply;
        }
        return totalSupplies / consumptionPerDay;
    }

    public void restockSupplies(int[] restockAmounts) {
        // Пополняем запасы
        for (int i = 0; i < supplies.length && i < restockAmounts.length; i++) {
            supplies[i] += restockAmounts[i];
        }
    }

    public String checkBunkerStatus() {
        boolean isIntact = isBunkerIntact();
        if (isIntact) {
            return "Бункер не поврежден";
        } else {
            return "Бункер поврежден";
        }
    }

    private boolean isBunkerIntact() {
        // Проходим по всем ячейкам бункера
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                if (layout[i][j] == 0) {
                    return false;
                }
            }
        }
        // Если не найдено разрушенных стен, бункер цел
        return true;
    }
}
