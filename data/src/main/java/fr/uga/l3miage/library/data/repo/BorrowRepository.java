package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Borrow;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Repository
public class BorrowRepository implements CRUDRepository<String, Borrow> {

    private final EntityManager entityManager;

    @Autowired
    public BorrowRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Borrow save(Borrow entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Borrow get(String id) {
        return entityManager.find(Borrow.class, id);
    }

    @Override
    public void delete(Borrow entity) {
        entityManager.remove(entity);
    }

    @Override
    public List<Borrow> all() {
        return entityManager.createQuery("from Borrow", Borrow.class).getResultList();
    }

    /**
     * Trouver des emprunts en cours pour un emprunteur donné
     *
     * @param userId l'id de l'emprunteur
     * @return la liste des emprunts en cours
     */
    public List<Borrow> findInProgressByUser(String userId) {
        // String query1 = "FROM Borrow b JOIN b.copy c JOIN c.book bk JOIN b.user u
        // WHERE u.id = :userId AND b.returnDate IS NULL";
        String query1 = "FROM Borrow b LEFT JOIN User u where u.id = :userId AND b.finished != :finished";
        var query = entityManager
                .createQuery(query1, Borrow.class)
                .setParameter("userId", userId)
                .setParameter("finished", true);
        return query.getResultList();
    }

    /**
     * Compte le nombre total de livres emprunté par un utilisateur.
     *
     * @param userId l'id de l'emprunteur
     * @return le nombre de livre
     */
    public int countBorrowedBooksByUser(String userId) {
        String query1 = "SELECT COUNT(*) FROM Book b LEFT JOIN Borrow b LEFT JOIN User u where u.id = :userId ";
        var query = entityManager.createQuery(query1, Long.class).setParameter("userId", userId);
        Long result = query.getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    /**
     * Compte le nombre total de livres non rendu par un utilisateur.
     *
     * @param userId l'id de l'emprunteur
     * @return le nombre de livre
     */
    public int countCurrentBorrowedBooksByUser(String userId) {
        List<Borrow> inProgressBorrows = findInProgressByUser(userId);
        return inProgressBorrows.size();
    }

    /**
     * Recherche tous les emprunt en retard trié
     *
     * @return la liste des emprunt en retard
     */
    public List<Borrow> foundAllLateBorrow() {
        var query = entityManager.createQuery(
                "SELECT b FROM Borrow b WHERE b.returnDate IS NULL AND b.dueDate < CURRENT_TIMESTAMP ORDER BY b.dueDate",
                Borrow.class);
        return query.getResultList();
    }

    /**
     * Calcul les emprunts qui seront en retard entre maintenant et x jours.
     *
     * @param days le nombre de jour avant que l'emprunt soit en retard
     * 
     * @return les emprunt qui sont bientôt en retard
     */
    public List<Borrow> findAllBorrowThatWillLateWithin(int days) {
        String query = "SELECT b FROM Borrow b WHERE b.requestedReturn <= :date";
        return entityManager
                .createQuery(query, Borrow.class)
                .setParameter("date", Date.from(ZonedDateTime.now().plus(days, ChronoUnit.DAYS).toInstant()))
                .getResultList();
    }

    public List<Borrow> findAllLateBorrow() {
        String query = "SELECT b FROM Borrow b WHERE b.requestedReturn < :date";

        return entityManager
                .createQuery(query, Borrow.class)
                .setParameter("date", Date.from(ZonedDateTime.now().toInstant()))
                .getResultList();

    }

}
