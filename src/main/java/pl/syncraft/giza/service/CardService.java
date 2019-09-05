package pl.syncraft.giza.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.syncraft.giza.entity.Card;
import pl.syncraft.giza.entity.CardList;
import pl.syncraft.giza.exceptions.NotFoundException;
import pl.syncraft.giza.repository.CardRepository;

import javax.transaction.Transactional;
import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/02
 */

@Service
public class CardService implements CrudService<Card> {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardListService cardListService;

    @Transactional
    public void save(@NonNull Card card) {
        if (card.getCreatedAt() == null) {
            card.setCreatedAt(new GregorianCalendar());
        }

        card.setUpdatedAt(new GregorianCalendar());

        cardRepository.save(card);
    }

    @Transactional
    public Optional<Card> get(int cardId) {
        return cardRepository.findById(cardId);
    }

    @Transactional
    public void delete(int cardId) {
        Card card = cardRepository.getOne(cardId);

        if (card != null) {
            cardRepository.delete(card);
        }
    }

    @Transactional
    public void moveToDifferentList(Card card, @NonNull Integer cardListId) {
        Optional<CardList> cardList = cardListService.get(cardListId);

        if (cardList.isPresent()) {
            throw new NotFoundException("Cannot find CardList with id: " + cardListId);
        }

        card.setCardListId(cardListId);
        cardRepository.save(card);
    }
}
