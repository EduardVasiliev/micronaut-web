package com.micronaut.web.controller;

import com.micronaut.web.data.Symbol;
import com.micronaut.web.errors.CustomErrorResponse;
import com.micronaut.web.errors.CustomOkResponse;
import com.micronaut.web.errors.CustomResponse;
import com.micronaut.web.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/symbols")
public class SymbolsController {

    private final InMemoryStore inMemoryStore;

    public SymbolsController(InMemoryStore inMemoryStore) {
        this.inMemoryStore = inMemoryStore;
    }

    @Get("/")
    public List<Symbol> getAll() {
        return new ArrayList<>(inMemoryStore.getSymbols().values());
    }

    @Get("/{value}")
    public Symbol getSymbolByValue(@PathVariable String value) {
        return inMemoryStore.getSymbols().get(value);
    }

    @Get("/filter{?max,offset}")
    public List<Symbol> getSymbols(@QueryValue Optional<Integer> max, @QueryValue Optional<Integer> offset) {
        return inMemoryStore.getSymbols().values()
                .stream()
                .skip(offset.orElse(0))
                .limit(max.orElse(0))
                .collect(Collectors.toList());
    }

    @Post(
            value = "/",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public List<Symbol> add(@Body Symbol symbol) {
        return inMemoryStore.addSymbol(symbol);
    }

    @Put(
            value = "/",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public List<Symbol> update(@Body Symbol symbol) {
        return inMemoryStore.updateSymbol(symbol);
    }

    @Delete(
            value = "/{valueToDelete}",
            produces = MediaType.APPLICATION_JSON
    )
    public List<Symbol> delete(@PathVariable String valueToDelete) {
        return inMemoryStore.deleteSymbol(valueToDelete);
    }

    // Custom Response
    @Post("/customResponse")
    public HttpResponse<CustomResponse> postSymbols2(@Body Symbol symbol) {
        try {
            inMemoryStore.addSymbol(symbol);
            return HttpResponse.ok().body(new CustomOkResponse(200, "Symbols added successfully"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpResponse.badRequest().body(new CustomErrorResponse(415, "Error while adding a new Symbol"));
    }
}
