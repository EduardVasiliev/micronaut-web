package com.micronaut.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.micronaut.web.data.Symbol;
import com.micronaut.web.store.InMemoryStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;


@MicronautTest
public class SymbolsControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SymbolsControllerTest.class);

    @Inject
    @Client("/symbols")
    HttpClient httpClient;

    @Inject
    InMemoryStore inMemoryStore;

    @BeforeEach
    public void setup() {
        inMemoryStore.initializeWith(10);
        LOG.info("InMemoryStore:\n {}" , inMemoryStore.getSymbols().values());
    }

    @AfterEach
    public void showFinalInMemoryStore() {
        LOG.info("Final InMemoryStore:\n {}", inMemoryStore.getSymbols().values());
    }

    @Test
    public void symbolsGetTest() {
        HttpResponse<JsonNode> response = httpClient.toBlocking().exchange("/" , JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(10, response.getBody().get().size());
    }

    @Test
    public void symbolsGetByValue() {
        Symbol testSymbol = new Symbol("TEST");
        inMemoryStore.getSymbols().put(testSymbol.getValue(), testSymbol);

        HttpResponse<Symbol> response = httpClient.toBlocking().exchange("/" + testSymbol.getValue(), Symbol.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(testSymbol, response.getBody().get());
    }

    @Test
    public void symbolsQueryValuesTest() {
        HttpResponse<JsonNode> response = httpClient.toBlocking().exchange("/filter?max=5", JsonNode.class);
        LOG.debug("Max: 10: {}", response.getBody().get().toPrettyString());
        assertEquals(5, response.getBody().get().size());
    }

    @Test
    public void symbolsUpdateTest() {
        // Creating a Symbol object as a Body
        Symbol symbol = new Symbol("TEST");

        // Build Request
        var request = HttpRequest.PUT("/", symbol)
                .accept(MediaType.APPLICATION_JSON);

        // Send Request
        HttpResponse<JsonNode> response = httpClient.toBlocking().exchange(request, JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    public void symbolsDeleteTest() {
        String symbolToDelete = "TEST";

        var request = HttpRequest.DELETE("/" + symbolToDelete);
        HttpResponse<JsonNode> response = httpClient.toBlocking().exchange(request, JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
    }


}
