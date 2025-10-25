import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHandler {
    private Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Предупреждение: ввод пуст.");
        }
        return input;
    }

    public String readNonEmptyString(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Ошибка: это поле не может быть пустым. Попробуйте снова.");
        }
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new NumberFormatException("Пустой ввод");
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное целое число!");
            }
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Ошибка: число должно быть положительным!");
        }
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            String input = readString(prompt + " (гггг-мм-дд или оставьте пустым): ");
            if (input.isEmpty()) {
                return null;
            }
            try {
                LocalDate date = LocalDate.parse(input);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("Ошибка: дата не может быть в будущем!");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Ошибка: введите дату в формате гггг-мм-дд (например: 2023-12-31)!");
            }
        }
    }

    // Метод для чтения года (обратная совместимость)
    public Integer readYear(String prompt) {
        while (true) {
            String input = readString(prompt + " (или 0 если неизвестен): ");
            if (input.isEmpty() || input.equals("0")) {
                return null;
            }
            try {
                int year = Integer.parseInt(input);
                if (year < 0) {
                    System.out.println("Ошибка: год не может быть отрицательным!");
                } else if (year > LocalDate.now().getYear()) {
                    System.out.println("Ошибка: год не может быть в будущем!");
                } else {
                    return year;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректный год!");
            }
        }
    }

    public int readMenuChoice(String[] options) {
        System.out.println("\n======================");
        System.out.println("   МЕНЮ БИБЛИОТЕКИ");
        System.out.println("======================");

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.println("======================");

        while (true) {
            try {
                int choice = readInt("Выберите пункт меню: ");
                if (choice >= 1 && choice <= options.length) {
                    return choice;
                } else {
                    System.out.println("Ошибка: выберите пункт от 1 до " + options.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число!");
            }
        }
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}