package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Author;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepository implements CRUDRepository<Long, Author> {

    private final EntityManager entityManager;

    @Autowired
    public AuthorRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Author save(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public Author get(Long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public void delete(Author author) {
        entityManager.remove(author);
    }

    /**
     * Renvoie tous les auteurs
     *
     * @return une liste d'auteurs trié par nom
     */
    @Override
    public List<Author> all() {
        String query = "SELECT a FROM Author a ORDER BY a.fullName";
        return entityManager.createQuery(query, Author.class).getResultList();
    }

    /**
     * Recherche un auteur par nom (ou partie du nom) de façon insensible à la
     * casse.
     *
     * @param namePart tout ou partie du nomde l'auteur
     * @return une liste d'auteurs trié par nom
     */
    public List<Author> searchByName(String namePart) {
        String query = "SELECT a FROM Author a WHERE LOWER(a.fullName) LIKE LOWER(:namePart) ORDER BY a.fullName";
        return entityManager.createQuery(query, Author.class)
                .setParameter("namePart", "%" + namePart + "%")
                .getResultList();
    }

    /**
     * Recherche si l'auteur a au moins un livre co-écrit avec un autre auteur
     *
     * @return true si l'auteur partage
     */
    public boolean checkAuthorByIdHavingCoAuthoredBooks(long authorId) {
        String query = "SELECT COUNT(b.id) > 0 FROM Book b JOIN b.authors a WHERE a.id = :authorId AND SIZE(b.authors) > 1";
        return entityManager.createQuery(query, Boolean.class)
                .setParameter("authorId", authorId)
                .getSingleResult();
    }

}
