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
    private Resource resource;

    @Autowired
    GraphQLDataFetchers graphQLDataFetcher;

    private GraphQL graphQL;

    @PostConstruct
    public void loadSchema() throws IOException {
        File schemaFile = resource.getFile();

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        Map<String, DataFetcher> dataFetchers = new HashMap<>();
        dataFetchers.put("board", graphQLDataFetcher.getBoardByIdDataFetcher());
        dataFetchers.put("card", graphQLDataFetcher.getCardByIdDataFetcher());
        dataFetchers.put("customer", graphQLDataFetcher.getCustomerByIdDataFetcher());

        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetchers(dataFetchers)).build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
