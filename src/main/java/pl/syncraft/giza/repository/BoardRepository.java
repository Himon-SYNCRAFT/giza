package pl.syncraft.giza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.syncraft.giza.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByOwnerId(int ownerId);
}
