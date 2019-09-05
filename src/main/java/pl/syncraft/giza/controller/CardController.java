package pl.syncraft.giza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.syncraft.giza.entity.Board;
import pl.syncraft.giza.entity.Card;
import pl.syncraft.giza.entity.CardList;
import pl.syncraft.giza.entity.Customer;
import pl.syncraft.giza.exceptions.NotFoundException;
import pl.syncraft.giza.service.BoardService;
import pl.syncraft.giza.service.CardListService;
import pl.syncraft.giza.service.CardService;
import pl.syncraft.giza.service.CustomerService;

import java.util.Optional;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/02
 */
@RestController
@RequestMapping("api")
public class CardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardListService cardListService;

    @GetMapping(value = "/")
    public String index() {
        return "hello world";
    }

    @GetMapping("/card")
    public void insert() {
        Customer owner = new Customer();
        owner.setEmail("d@d.pl");
        owner.setFirstName("Daniel");
        owner.setLastName("Zawlocki");
        owner.setLogin("danzaw");
        customerService.save(owner);

        Board board = new Board();
        board.setName("ToDo");
        board.setOwner(owner);
        boardService.save(board);

        CardList cardList = new CardList();
        cardList.setName("Plan");
        cardList.setOwner(owner);
        cardList.setBoard(board);
        cardListService.save(cardList);

        Card card = new Card();
        card.setTitle("test title");
        card.setDescription("test description");
        card.setOwner(owner);
        card.setCardList(cardList);
        cardService.save(card);

        board.addCardList(cardList);
        boardService.save(board);
    }

    @GetMapping(value = "/card/{cardId}", produces = "application/json")
    public Card get(@PathVariable int cardId) {
        Optional<Card> card = cardService.get(cardId);

        if (card.isPresent()) {
            return card.get();
        }

        throw new NotFoundException("Not found Card with id: " + cardId);
    }
}
