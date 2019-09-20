package pl.syncraft.giza.service;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/05
 */
@Service
public class GraphQLService {
    @Value("classpath:schema.graphql")
    private Resource schema;

    @Autowired
    GraphQLDataFetchers graphQLDataFetcher;

    private GraphQL graphQL;

    @PostConstruct
    public void loadSchema() throws IOException {
        File schemaFile = schema.getFile();

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        Map<String, DataFetcher> queryDataFetchers = new HashMap<>();
        queryDataFetchers.put("board", graphQLDataFetcher.getBoardByIdDataFetcher());
        queryDataFetchers.put("card", graphQLDataFetcher.getCardByIdDataFetcher());
        queryDataFetchers.put("customer", graphQLDataFetcher.getCustomerByIdDataFetcher());

        Map<String, DataFetcher> mutationDataFetchers = new HashMap<>();
        mutationDataFetchers.put("addCustomer", graphQLDataFetcher.addCustomer());
        mutationDataFetchers.put("moveCardToOtherList", graphQLDataFetcher.moveCardToOtherList());
        mutationDataFetchers.put("moveCardAboveOtherCard", graphQLDataFetcher.moveCardAboveOtherCard());
        mutationDataFetchers.put("addCardList", graphQLDataFetcher.addCardList());
        mutationDataFetchers.put("updateCardList", graphQLDataFetcher.updateCardList());
        mutationDataFetchers.put("addCard", graphQLDataFetcher.addCard());
        mutationDataFetchers.put("updateCard", graphQLDataFetcher.updateCard());

        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetchers(queryDataFetchers))
                .type(TypeRuntimeWiring.newTypeWiring("Mutation")
                        .dataFetchers(mutationDataFetchers))
                .type("Customer", typeWiring -> typeWiring)
                .type("Card", typeWiring -> typeWiring)
                .type("CardList", typeWiring -> typeWiring)
                .type("Board", typeWiring -> typeWiring)
                .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
