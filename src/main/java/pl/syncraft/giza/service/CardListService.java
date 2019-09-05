package pl.syncraft.giza.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.syncraft.giza.entity.CardList;
import pl.syncraft.giza.repository.CardListRepository;

import javax.transaction.Transactional;
import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/05
 */
@Service
public class CardListService implements CrudService<CardList> {

    @Autowired
    private CardListRepository cardListRepository;

    @Transactional
    public void save(@NonNull CardList cardList) {
        if (cardList.getCreatedAt() == null) {
            cardList.setCreatedAt(new GregorianCalendar());
        }

        cardList.setUpdatedAt(new GregorianCalendar());
        cardListRepository.save(cardList);
    }

    @Transactional
    public Optional<CardList> get(int cardListId) {
        return cardListRepository.findById(cardListId);
    }

    @Transactional
    public void delete(int cardListId) {
        CardList cardList = cardListRepository.getOne(cardListId);

        if (cardList != null) {
            cardListRepository.delete(cardList);
        }
    }
}
