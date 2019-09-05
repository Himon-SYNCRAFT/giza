package pl.syncraft.giza.controller;

import graphql.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.syncraft.giza.service.GraphQLService;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/05
 */
@RestController
@RequestMapping("/graphql")
public class MainController {
    @Autowired
    GraphQLService graphQLService;

    @PostMapping
    @CrossOrigin
    public ResponseEntity<Object> query(@RequestBody String query) {
        ExecutionResult result = graphQLService.getGraphQL().execute(query);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
