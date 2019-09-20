package pl.syncraft.giza.service;

import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.syncraft.giza.entity.Card;
import pl.syncraft.giza.entity.CardList;
import pl.syncraft.giza.entity.Customer;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/05
 */
@Component
public class GraphQLDataFetchers {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardListService cardListService;

    public DataFetcher getCustomerByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            int customerId = Integer.parseInt(dataFetchingEnvironment.getArgument("id"));
            return customerService.get(customerId);
        };
    }

    public DataFetcher getBoardByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            int boardId = Integer.parseInt(dataFetchingEnvironment.getArgument("id"));
            return boardService.get(boardId);
        };
    }

    public DataFetcher getCardByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            int cardId = Integer.parseInt(dataFetchingEnvironment.getArgument("id"));
            return cardService.get(cardId);
        };
    }

    public DataFetcher addCustomer() {
        return dataFetchingEnvironment -> {
            String firstName = dataFetchingEnvironment.getArgument("firstName");
            String lastName = dataFetchingEnvironment.getArgument("lastName");
            String email = dataFetchingEnvironment.getArgument("email");
            String login = dataFetchingEnvironment.getArgument("login");

            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setLogin(login);
            customer.setEmail(email);
            customerService.save(customer);

            return customer;
        };
    }

    public DataFetcher moveCardToOtherList() {
        return dataFetchingEnvironment -> {
            int cardId = Integer.parseInt(dataFetchingEnvironment.getArgument("cardId"));
            int cardListId = Integer.parseInt(dataFetchingEnvironment.getArgument("cardListId"));

            cardService.moveToList(cardId, cardListId);
            Optional<Card> optionalCard = cardService.get(cardId);

            return optionalCard.orElse(null);
        };
    }

    public DataFetcher moveCardAboveOtherCard() {
        return dataFetchingEnvironment -> {
            int sourceId = Integer.parseInt(dataFetchingEnvironment.getArgument("sourceId"));
            int targetId = Integer.parseInt(dataFetchingEnvironment.getArgument("targetId"));

            cardService.moveAboveOtherCard(sourceId, targetId);
            Optional<Card> optionalCard = cardService.get(sourceId);

            if (!optionalCard.isPresent()) {
                return null;
            }

            Card card = optionalCard.get();
            Optional<CardList> optionalCardList = cardListService.get(card.getCardListId());

            if (!optionalCardList.isPresent()) {
                return null;
            }

            int boardId = optionalCardList.get().getBoardId();
            return boardService.get(boardId).orElse(null);
        };
    }

    public DataFetcher addCard() {
        return e -> {
            LinkedHashMap<String, Object> input = e.getArgument("input");

            String title = (String) input.get("title");
            String description = (String) input.get("description");
            int cardListId = (int) input.get("cardListId");
            int ownerId = (int) input.get("ownerId");

            Card card = new Card();
            card.setTitle(title);
            card.setDescription(description);
            card.setCardListId(cardListId);

            Optional<Customer> owner = customerService.get(ownerId);
            owner.ifPresent(card::setOwner);

            cardService.save(card);
            return card;
        };
    }

    public DataFetcher updateCard() {
        return e -> {
            int cardId = Integer.parseInt(e.getArgument("id"));
            LinkedHashMap<String, Object> input = e.getArgument("input");

            String title = (String) input.get("title");
            String description = (String) input.get("description");
            int cardListId = Integer.parseInt((String)input.get("cardListId"));
            int ownerId = Integer.parseInt((String)input.get("ownerId"));

            Optional<Card> optionalCard = cardService.get(cardId);

            if (!optionalCard.isPresent()) {
                return null;
            }

            Card card = optionalCard.get();
            card.setTitle(title);
            card.setDescription(description);
            card.setCardListId(cardListId);

            Optional<Customer> owner = customerService.get(ownerId);
            owner.ifPresent(card::setOwner);

            cardService.save(card);
            return card;
        };
    }

    public DataFetcher addCardList() {
        return e -> {
            LinkedHashMap<String, Object> input = e.getArgument("input");

            String name = (String) input.get("name");
            int boardId = Integer.parseInt((String)input.get("boardId"));
            int ownerId = Integer.parseInt((String)input.get("ownerId"));

            CardList cardList = new CardList();
            cardList.setName(name);
            cardList.setBoardId(boardId);

            Optional<Customer> owner = customerService.get(ownerId);
            owner.ifPresent(cardList::setOwner);

            cardListService.save(cardList);
            return cardList;
        };
    }

    public DataFetcher updateCardList() {
        return e -> {
            int cardListId = e.getArgument("id");
            String name = e.getArgument("name");
            int ownerId = e.getArgument("ownerId");
            int boardId = e.getArgument("boardId");

            Optional<CardList> optionalCardList = cardListService.get(cardListId);

            if (!optionalCardList.isPresent()) {
                return null;
            }

            CardList cardList = optionalCardList.get();
            cardList.setName(name);
            cardList.setBoardId(boardId);

            Optional<Customer> owner = customerService.get(ownerId);
            owner.ifPresent(cardList::setOwner);

            cardListService.save(cardList);
            return cardList;
        };
    }
}
