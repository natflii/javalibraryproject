import java.time.LocalDate;

public class Book {
    private String name;
    private String genre;
    private String author;
    private LocalDate publicationDate;


    public Book(String name, String author, String genre, LocalDate publicationDate) {
        setName(name);
        setAuthor(author);
        setGenre(genre);
        setPublicationDate(publicationDate);
    }

    public Book(String name, String author, String genre) {
        this(name, author, genre, null);
    }

    public Book(String name, String author) {
        this(name, author, "Не указан", null);
    }

    public Book(String name) {
        this(name, "Неизвестен", "Не указан", null);
    }

    // сеттеры с проверкой
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название книги не может быть пустым");
        }
        this.name = name.trim();
    }

    public void setPublicationDate(LocalDate publicationDate) {
        if (publicationDate != null && publicationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата публикации не может быть в будущем");
        }
        this.publicationDate = publicationDate;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Жанр не может быть пустым");
        }
        this.genre = genre.trim();
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Автор не может быть пустым");
        }
        this.author = author.trim();
    }

    // Геттеры
    public String getGenre() {
        return this.genre;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getPublicationDate() {
        return this.publicationDate;
    }

    public String getAuthor() {
        return this.author;
    }

    public void printInfo() {
        String dateInfo = (publicationDate != null) ? String.valueOf(publicationDate.getYear()) : "неизвестен";
        System.out.println("Название книги: " + this.name +
                "; Автор: " + this.author +
                "; Жанр: " + this.genre +
                "; Год написания: " + dateInfo);
    }

    public int getDate() {
        return (publicationDate != null) ? publicationDate.getYear() : -1;
    }

    public void setDate(int year) {
        if (year <= 0) {
            this.publicationDate = null;
        } else if (year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Год не может быть в будущем");
        } else {
            this.publicationDate = LocalDate.of(year, 1, 1);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return name.equalsIgnoreCase(book.name) &&
                author.equalsIgnoreCase(book.author);
    }

    @Override
    public int hashCode() {
        return (name.toLowerCase() + author.toLowerCase()).hashCode();
    }
}