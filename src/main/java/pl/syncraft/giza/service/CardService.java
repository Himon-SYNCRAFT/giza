package pl.syncraft.giza.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.syncraft.giza.entity.Card;
import pl.syncraft.giza.entity.CardList;
import pl.syncraft.giza.exceptions.NotFoundException;
import pl.syncraft.giza.repository.CardRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

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

        Optional<CardList> optionalCardList = cardListService.get(card.getCardListId());

        Set<Card> cards;

        if (card.getSort() == null) {
            int maxSort = 0;

            if (optionalCardList.isPresent()) {
                cards = optionalCardList.get().getCards();
                Optional<Card> optionalCard = cards.stream().max((a, b) -> a.getSort() - b.getSort());

                if (optionalCard.isPresent()) {
                    maxSort = optionalCard.get().getSort();
                }
            }

            card.setSort(maxSort + 1);
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
        try {
            Card card = cardRepository.getOne(cardId);
            cardRepository.delete(card);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Cannot found Card with id: " + cardId);
        }
    }

    @Transactional
    public void moveToList(int cardId, int cardListId) {
        Card card;

        try {
            card = cardRepository.getOne(cardId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Cannot found Card with id: " + cardId);
        }

        if (card.getCardListId().equals(cardListId)) {
            return;
        }

        Optional<CardList> cardList = cardListService.get(cardListId);

        if (!cardList.isPresent()) {
            throw new NotFoundException("Cannot find CardList with id: " + cardListId);
        }

        card.setCardListId(cardListId);
        cardRepository.save(card);
    }

    @Transactional
    public void moveAboveOtherCard(int sourceId, int targetId) {
        Card source;
        Card target;

        try {
            source = cardRepository.getOne(sourceId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Cannot found Card with id: " + sourceId);
        }

        try {
            target = cardRepository.getOne(targetId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Cannot found Card with id: " + targetId);
        }

        int cardListId = target.getCardListId();

        Optional<CardList> optionalCardList = cardListService.get(cardListId);

        if (optionalCardList.isPresent()) {
            CardList cardList = optionalCardList.get();
            List<Card> cards = new ArrayList<>(cardList.getCards());
            cards.sort((a, b) -> a.getSort() - b.getSort());

            int sort = 1;

            for (Card card : cards) {
                if (card == target) {
                    source.setSort(sort);
                    sort++;
                }

                card.setSort(sort);
                cardRepository.save(card);
                sort++;
            }

            source.setCardList(cardList);
            cardRepository.save(source);
        } else {
            throw new NotFoundException("Cannot found CardList with id: " + cardListId);
        }
    }
}
