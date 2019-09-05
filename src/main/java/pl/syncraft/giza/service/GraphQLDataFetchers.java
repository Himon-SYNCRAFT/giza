package pl.syncraft.giza.service;

import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
