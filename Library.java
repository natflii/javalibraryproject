import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.time.LocalDate;

public class Library {
    private HashMap<String, ArrayList<Book>> booksByGenre = new HashMap<>();

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Книга не может быть null");
        }

        String genre = book.getGenre();
        booksByGenre.putIfAbsent(genre, new ArrayList<>());

        // чек дубликаты
        for (Book existingBook : booksByGenre.get(genre)) {
            if (existingBook.equals(book)) {
                throw new IllegalArgumentException("Книга с таким названием и автором уже существует");
            }
        }

        booksByGenre.get(genre).add(book);
    }

    public void editBook(String name, String newName, String newAuthor, String newGenre, LocalDate newDate) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название книги для редактирования не может быть пустым");
        }

        Book bookToEdit = findBookByName(name);
        if (bookToEdit == null) {
            throw new IllegalArgumentException("Книга \"" + name + "\" не найдена.");
        }


        String oldGenre = bookToEdit.getGenre();

        try {

            if (newGenre != null && !newGenre.trim().isEmpty() && !newGenre.equalsIgnoreCase(oldGenre)) {

                List<Book> oldList = booksByGenre.get(oldGenre);
                if (oldList != null) {
                    oldList.remove(bookToEdit);
                    if (oldList.isEmpty()) {
                        booksByGenre.remove(oldGenre);
                    }
                }


                booksByGenre.putIfAbsent(newGenre, new ArrayList<>());

                String tempName = (newName != null && !newName.trim().isEmpty()) ? newName : bookToEdit.getName();
                String tempAuthor = (newAuthor != null && !newAuthor.trim().isEmpty()) ? newAuthor : bookToEdit.getAuthor();

                for (Book existingBook : booksByGenre.get(newGenre)) {
                    if (existingBook.getName().equalsIgnoreCase(tempName) &&
                            existingBook.getAuthor().equalsIgnoreCase(tempAuthor) &&
                            !existingBook.equals(bookToEdit)) {
                        throw new IllegalArgumentException("Книга с таким названием и автором уже существует в жанре: " + newGenre);
                    }
                }

                booksByGenre.get(newGenre).add(bookToEdit);
                bookToEdit.setGenre(newGenre);
            }

            if (newName != null && !newName.trim().isEmpty()) {
                bookToEdit.setName(newName);
            }

            if (newAuthor != null && !newAuthor.trim().isEmpty()) {
                bookToEdit.setAuthor(newAuthor);
            }

            if (newDate != null) {
                bookToEdit.setPublicationDate(newDate);
            }

        } catch (Exception e) {
            if (!bookToEdit.getGenre().equals(oldGenre)) {
                bookToEdit.setGenre(oldGenre);
            }
            throw e;
        }
    }

    public void editBook(String name, String newName, String newAuthor, String newGenre, Integer newYear) {
        LocalDate newDate = null;
        if (newYear != null && newYear > 0 && newYear <= LocalDate.now().getYear()) {
            newDate = LocalDate.of(newYear, 1, 1);
        }
        editBook(name, newName, newAuthor, newGenre, newDate);
    }

    public void printBooksByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("Ошибка: жанр не может быть пустым.");
            return;
        }

        if (booksByGenre.isEmpty()) {
            System.out.println("Библиотека пуста.");
            return;
        }

        ArrayList<Book> list = booksByGenre.get(genre.trim());
        if (list == null || list.isEmpty()) {
            System.out.println("Жанр \"" + genre + "\" не найден или пуст.");
            return;
        }

        System.out.println("Жанр: " + genre);
        for (Book book : list) {
            String dateInfo = (book.getPublicationDate() != null) ?
                    String.valueOf(book.getPublicationDate().getYear()) : "неизвестен";
            System.out.println(book.getName() + ", " + book.getAuthor() + ", " + dateInfo);
        }
        System.out.println();
    }

    public void printAllBooks() {
        if (booksByGenre.isEmpty()) {
            System.out.println("Библиотека пуста.");
            return;
        }

        System.out.println("Список всех книг в библиотеке:");
        for (Map.Entry<String, ArrayList<Book>> entry : booksByGenre.entrySet()) {
            String genre = entry.getKey();
            ArrayList<Book> list = entry.getValue();

            System.out.println("\nЖанр: " + genre);
            for (Book book : list) {
                String dateInfo = (book.getPublicationDate() != null) ?
                        String.valueOf(book.getPublicationDate().getYear()) : "неизвестен";
                System.out.printf(" - \"%s\" (%s, %s)\n", book.getName(), book.getAuthor(), dateInfo);
            }
        }
    }

    public void removeBookByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Ошибка: название книги не может быть пустым.");
            return;
        }

        boolean found = false;
        String nameToRemove = name.trim();

        for (Map.Entry<String, ArrayList<Book>> entry : booksByGenre.entrySet()) {
            String genre = entry.getKey();
            ArrayList<Book> list = entry.getValue();

            boolean removed = list.removeIf(book -> book.getName().equalsIgnoreCase(nameToRemove));

            if (removed) {
                found = true;
                System.out.println("Книга \"" + name + "\" удалена из жанра \"" + genre + "\".");

                if (list.isEmpty()) {
                    booksByGenre.remove(genre);
                    System.out.println("Жанр \"" + genre + "\" удалён, так как больше нет книг.");
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Книга \"" + name + "\" не найдена в библиотеке.");
        }
    }

    public Book findBookByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        String nameToFind = name.trim();
        for (ArrayList<Book> list : booksByGenre.values()) {
            for (Book book : list) {
                if (book.getName().equalsIgnoreCase(nameToFind)) {
                    return book;
                }
            }
        }
        return null;
    }

    public void findBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            System.out.println("Ошибка: автор не может быть пустым.");
            return;
        }

        ArrayList<Book> result = new ArrayList<>();
        String authorToFind = author.trim();

        for (ArrayList<Book> list : booksByGenre.values()) {
            for (Book book : list) {
                if (book.getAuthor().equalsIgnoreCase(authorToFind)) {
                    result.add(book);
                }
            }
        }

        if (result.isEmpty()) {
            System.out.println("Книги автора \"" + author + "\" не найдены.");
        } else {
            System.out.println("Книги автора " + author + ": ");
            for (Book book : result) {
                System.out.println(" - " + book.getName());
            }
        }
        System.out.println();
    }

    public void loadBooksFromFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Файл не существует: " + filePath);
        }
        if (!file.canRead()) {
            throw new RuntimeException("Нет прав на чтение файла: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern pattern = Pattern.compile(
                    "\\[name = \"(.*?)\", author = \"(.*?)\", genre = \"(.*?)\", year = (-?\\d+)\\]"
            );

            int loadedCount = 0;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String name = matcher.group(1);
                    String author = matcher.group(2);
                    String genre = matcher.group(3);
                    int year = Integer.parseInt(matcher.group(4));

                    if (author.isEmpty()) author = "Неизвестен";
                    if (genre.isEmpty()) genre = "Не указан";

                    LocalDate publicationDate = null;
                    if (year > 0 && year <= LocalDate.now().getYear()) {
                        publicationDate = LocalDate.of(year, 1, 1);
                    }

                    Book book = new Book(name, author, genre, publicationDate);
                    addBook(book);
                    loadedCount++;
                }
            }

            System.out.println("Успешно загружено " + loadedCount + " книг из файла: " + filePath);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Ошибка формата данных в файле: " + e.getMessage(), e);
        }
    }

    public void saveBooksToFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            int savedCount = 0;
            for (Map.Entry<String, ArrayList<Book>> entry : booksByGenre.entrySet()) {
                for (Book book : entry.getValue()) {
                    int year = (book.getPublicationDate() != null) ? book.getPublicationDate().getYear() : -1;
                    String line = String.format(
                            "[name = \"%s\", author = \"%s\", genre = \"%s\", year = %d]",
                            book.getName(),
                            book.getAuthor(),
                            book.getGenre(),
                            year
                    );
                    writer.write(line);
                    writer.newLine();
                    savedCount++;
                }
            }
            System.out.println("Успешно сохранено " + savedCount + " книг в файл: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении в файл: " + e.getMessage(), e);
        }
    }

    public void importBooksFromFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Файл не существует: " + filePath);
        }
        if (!file.canRead()) {
            throw new RuntimeException("Нет прав на чтение файла: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern pattern = Pattern.compile(
                    "\\[name = \"(.*?)\", author = \"(.*?)\", genre = \"(.*?)\", year = (-?\\d+)\\]"
            );

            int importedCount = 0;
            int skippedCount = 0;

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String name = matcher.group(1);
                    String author = matcher.group(2);
                    String genre = matcher.group(3);
                    int year = Integer.parseInt(matcher.group(4));

                    if (author.isEmpty()) author = "Неизвестен";
                    if (genre.isEmpty()) genre = "Не указан";

                    LocalDate publicationDate = null;
                    if (year > 0 && year <= LocalDate.now().getYear()) {
                        publicationDate = LocalDate.of(year, 1, 1);
                    }


                    Book existingBook = findBookByName(name);
                    if (existingBook != null && existingBook.getAuthor().equalsIgnoreCase(author)) {
                        skippedCount++;
                    } else {
                        addBook(new Book(name, author, genre, publicationDate));
                        importedCount++;
                    }
                }
            }

            System.out.println("Импортировано " + importedCount + " новых книг, пропущено " +
                    skippedCount + " дубликатов из файла: " + filePath);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при импорте книг: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Ошибка формата данных в файле: " + e.getMessage(), e);
        }
    }
}