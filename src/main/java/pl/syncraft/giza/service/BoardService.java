package pl.syncraft.giza.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.syncraft.giza.entity.Board;
import pl.syncraft.giza.repository.BoardRepository;

import javax.transaction.Transactional;
import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/05
 */
@Service
public class BoardService implements CrudService<Board> {
    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public void save(@NonNull Board board) {
        if (board.getCreatedAt() == null) {
            board.setCreatedAt(new GregorianCalendar());
        }

        board.setUpdatedAt(new GregorianCalendar());

        boardRepository.save(board);
    }

    @Transactional
    public Optional<Board> get(int boardId) {
        return boardRepository.findById(boardId);
    }

    @Transactional
    public void delete(int boardId) {
        Board board = boardRepository.getOne(boardId);

        if (board != null) {
            boardRepository.delete(board);
        }
    }
}
