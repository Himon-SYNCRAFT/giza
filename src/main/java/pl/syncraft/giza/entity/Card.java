package pl.syncraft.giza.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/02
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Integer cardListId;

    @Column(nullable = false)
    private Integer sort = 1;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;

    public void setCardList(@NonNull CardList cardList) {
        if (cardList.getId() == null) {
            throw new NullPointerException("cardListId should not be null");
        }

        setCardListId(cardList.getId());
        cardList.addCard(this);
    }
}
