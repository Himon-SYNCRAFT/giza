package pl.syncraft.giza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.syncraft.giza.entity.Card;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/02
 */

public interface CardRepository extends JpaRepository<Card, Integer> {
}
