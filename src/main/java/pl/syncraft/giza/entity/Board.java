package pl.syncraft.giza.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/02
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    private String name;

    @Column
    @OneToMany(mappedBy = "boardId")
    private List<CardList> cardLists = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;

    public void addCardList(@NonNull CardList cardList) {
        cardLists.add(cardList);
    }
}
