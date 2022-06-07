package fr.runview.searchengine.repositories;

import fr.runview.searchengine.domain.Cat;
import fr.runview.searchengine.domain.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CatRepository extends JpaRepository<Cat, Long> {
    Page<Cat> findAll(Pageable pageable);

    List<Cat> findChatByGender(Gender gender);

    List<Cat> findChatByPrice(Double value);

    @Query("select c from Cat c where c.price > :value")
    List<Cat> filterCatsByPriceGreaterThan(Double value);

    @Query("select c from Cat c where c.price < :value")
    List<Cat> filterCatsByPriceLessThan(Double value);

    @Query("select c from Cat c where c.price between :min and :max")
    List<Cat> filterCatsByPriceBetween(Double min, Double max);

    @Query("select c from Cat c where c.birthDay <= :ref")
    List<Cat> filterCatsByBirthDayBefore(LocalDate ref);
    @Query("select c from Cat c where c.birthDay >= :ref")
    List<Cat> filterCatsByBirthDayAfter(LocalDate ref);
    @Query("select c from Cat c where c.birthDay between :min and :max")
    List<Cat> filterCatsByBirthDayBetween(LocalDate min, LocalDate max);
}
