package pl.syncraft.giza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.syncraft.giza.entity.CardList;

public interface CardListRepository extends JpaRepository<CardList, Integer> {
}
