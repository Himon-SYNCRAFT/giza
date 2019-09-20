package pl.syncraft.giza.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.*;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/02
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CardList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    private String name;

    @Column
    @OneToMany(mappedBy = "cardListId")
    private Set<Card> cards = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @Column
    private int boardId;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;


    public void addCard(@NonNull Card card) {
        cards.add(card);
    }

    public void setBoard(@NonNull Board board) {
        if (board.getId() == null) {
            throw new NullPointerException("boardId should not be null");
        }

        setBoardId(board.getId());
    }

    public List<Card> getCards() {
        List<Card> cardsAsList = new ArrayList<>(cards);
        cardsAsList.sort((a, b) -> a.getSort() - b.getSort());
        return cardsAsList;
    }
}
