package com.alexvak.msscbeerservice.web.controller;

import com.alexvak.msscbeerservice.services.BeerService;
import com.alexvak.msscbeerservice.web.model.BeerDto;
import com.alexvak.msscbeerservice.web.model.BeerPagedList;
import com.alexvak.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/beer")
@RestController
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<BeerPagedList> listBeers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "beerName", required = false) String beerName,
                                                   @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
                                                   @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        if (Objects.isNull(showInventoryOnHand)) {
            showInventoryOnHand = false;
        }

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        BeerPagedList beerList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);

        return new ResponseEntity<>(beerList, HttpStatus.OK);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId,
                                               @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        if (Objects.isNull(showInventoryOnHand)) {
            showInventoryOnHand = false;
        }
        return new ResponseEntity<>(beerService.getById(beerId, showInventoryOnHand), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveNewBeer(@Validated @RequestBody BeerDto beerDto) {

        beerService.saveNewBeer(beerDto);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) {

        beerService.updateBeer(beerId, beerDto);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
