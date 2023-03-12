package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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
        // requete pour obtenir le nom des authors triés par nom
        String query = "select a FROM Author ORDER BY a.fullname ASC";

        // retourne la liste des autheurs
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
        TypedQuery<Author> query = entityManager.createQuery(
                "SELECT a FROM Author a WHERE a.fullName LIKE CONCAT('%', :namePart, '%')",
                Author.class);
        query.setParameter("namePart", namePart);
        return query.getResultList();
    }

    /**
     * Recherche si l'auteur a au moins un livre co-écrit avec un autre auteur
     *
     * @return true si l'auteur partage
     */
    public boolean checkAuthorByIdHavingCoAuthoredBooks(long authorId) {

        String query = "SELECT COUNT(*) FROM Book b JOIN b.authors a WHERE a.id = :authorId AND EXISTS (SELECT 1 FROM Book b2 JOIN b2.authors a2 WHERE a2.id <> a.id AND b2.id = b.id)";

        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("authorId", authorId)
                .getSingleResult();

        return count > 0;
    }

}