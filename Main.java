import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class Main {
    private static final Library library = new Library();
    private static final InputHandler inputHandler = new InputHandler();
    private static final String LIBRARY_FILE = "files/lib.txt";

    public static void main(String[] args) {
        try {
            library.loadBooksFromFile(LIBRARY_FILE);
        } catch (Exception e) {
            System.out.println("Предупреждение: " + e.getMessage());
            System.out.println("Библиотека будет пустой.");
        }

        runMainMenu();
        inputHandler.close();
    }

    private static void runMainMenu() {
        String[] mainMenuOptions = {
                "Добавить книгу",
                "Удалить книгу",
                "Найти книгу",
                "Вывести список книг",
                "Редактировать книгу",
                "Импортировать книги из books.txt",
                "Помощь",
                "Сохранить и выйти"
        };

        boolean exitRequested = false;

        while (!exitRequested) {
            try {
                int menuChoice = inputHandler.readMenuChoice(mainMenuOptions);

                switch (menuChoice) {
                    case 1 -> addBook();
                    case 2 -> removeBook();
                    case 3 -> findBook();
                    case 4 -> listBooks();
                    case 5 -> editBook();
                    case 6 -> importBooks();
                    case 7 -> showHelp();
                    case 8 -> exitRequested = true;
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
                System.out.println("Продолжаем работу...\n");
            }
        }

        try {
            library.saveBooksToFile(LIBRARY_FILE);
            System.out.println("Библиотека сохранена. До свидания!");
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private static void addBook() {
        System.out.println("\n--- ДОБАВЛЕНИЕ НОВОЙ КНИГИ ---");

        try {
            String name = inputHandler.readNonEmptyString("Введите название: ");
            String author = inputHandler.readNonEmptyString("Введите фамилию автора: ");
            String genre = inputHandler.readNonEmptyString("Введите жанр: ");
            Integer year = inputHandler.readYear("Введите год создания");

            LocalDate publicationDate = null;
            if (year != null) {
                publicationDate = LocalDate.of(year, 1, 1);
            }

            Book book = new Book(name, author, genre, publicationDate);
            library.addBook(book);
            System.out.println("Книга успешно добавлена!\n");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении книги: " + e.getMessage());
        }
    }

    private static void removeBook() {
        System.out.println("\n--- УДАЛЕНИЕ КНИГИ ---");
        String title = inputHandler.readNonEmptyString("Введите название книги для удаления: ");

        Book book = library.findBookByName(title);
        if (book == null) {
            System.out.println("Книга не найдена в библиотеке.");
        } else {
            library.removeBookByName(book.getName());
            System.out.println("Книга удалена.");
        }
    }

    private static void findBook() {
        System.out.println("\n--- ПОИСК КНИГИ ---");
        String[] searchOptions = {"По названию", "По автору"};
        int searchType = inputHandler.readMenuChoice(searchOptions);

        switch (searchType) {
            case 1 -> {
                String name = inputHandler.readNonEmptyString("Введите название: ");
                Book book = library.findBookByName(name);
                if (book != null) {
                    System.out.println("Книга \"" + name + "\" находится в библиотеке");
                    book.printInfo();
                } else {
                    System.out.println("Книга не найдена.");
                }
            }
            case 2 -> {
                String author = inputHandler.readNonEmptyString("Введите автора: ");
                library.findBooksByAuthor(author);
            }
        }
    }

    private static void listBooks() {
        System.out.println("\n--- СПИСОК КНИГ ---");
        String[] listOptions = {"Все книги", "По жанру"};
        int listType = inputHandler.readMenuChoice(listOptions);

        switch (listType) {
            case 1 -> library.printAllBooks();
            case 2 -> {
                String genre = inputHandler.readNonEmptyString("Введите жанр: ");
                library.printBooksByGenre(genre);
            }
        }
    }

    private static void editBook() {
        System.out.println("\n--- РЕДАКТИРОВАНИЕ КНИГИ ---");
        String titleToEdit = inputHandler.readNonEmptyString("Введите название книги для редактирования: ");

        if (library.findBookByName(titleToEdit) == null) {
            System.out.println("Книга не найдена в библиотеке.");
            return;
        }

        try {
            String newName = inputHandler.readString("Новое название (или оставьте пустым): ");
            String newAuthor = inputHandler.readString("Новый автор (или оставьте пустым): ");
            String newGenre = inputHandler.readString("Новый жанр (или оставьте пустым): ");
            Integer newYear = inputHandler.readYear("Новый год");

            library.editBook(titleToEdit,
                    newName.isEmpty() ? null : newName,
                    newAuthor.isEmpty() ? null : newAuthor,
                    newGenre.isEmpty() ? null : newGenre,
                    newYear);

            System.out.println("Книга успешно отредактирована!");
        } catch (Exception e) {
            System.out.println("Ошибка при редактировании книги: " + e.getMessage());
        }
    }

    private static void importBooks() {
        System.out.println("\n--- ИМПОРТ КНИГ ---");
        try {
            library.importBooksFromFile("files/books.txt");
            library.saveBooksToFile(LIBRARY_FILE);
            System.out.println("Книги импортированы и сохранены!");
        } catch (Exception e) {
            System.out.println("Ошибка при импорте: " + e.getMessage());
        }
    }

    private static void showHelp() {
        System.out.println("\n--- ПОМОЩЬ ---");
        try (BufferedReader reader = new BufferedReader(new FileReader("files/help.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла помощи: " + e.getMessage());
        }
    }
}