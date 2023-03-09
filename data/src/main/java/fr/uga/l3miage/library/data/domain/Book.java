package fr.uga.l3miage.library.data.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NamedQueries({
    @NamedQuery(
        name="all-books",
        query="select b from Book b ORDER BY b.title ASC"
    ),
    @NamedQuery(
        name="find-books-by-title",
        query="select b from book b where LOWER(b.title) like :title  Oder by title ASC"
    ),
    @NamedQuery(
        name="find-books-by-author-and-title",
        query="select b  from book b where LOWER(b.author) like : author and LOWER(b.title) like :title ORDER BY title,author ASC"
    ),
    @NamedQuery(
        name = "find-books-by-authors-name",
        query = "SELECT b FROM Book b JOIN b.authors a WHERE a.fullName = :authorName"
    ),
    @NamedQuery(
        name = "find-books-by-several-authors",
        query = "SELECT b FROM Book b WHERE b.authors IN :authors"
    )
})
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true)
    private long isbn;


    private String publisher;

    private short year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;


    @ManyToMany(mappedBy="books")
    private Set<Author> authors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        if (this.authors == null) {
            this.authors = new HashSet<>();
        }
        this.authors.add(author);
    }

    public enum Language {
        FRENCH,
        ENGLISH
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn == book.isbn && year == book.year && Objects.equals(title, book.title) && Objects.equals(publisher, book.publisher) && language == book.language && Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, isbn, publisher, year, language, authors);
    }
}
