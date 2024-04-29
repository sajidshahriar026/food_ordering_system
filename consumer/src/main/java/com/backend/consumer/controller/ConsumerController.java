package com.backend.consumer.controller;

import com.backend.consumer.dto.*;
import com.backend.consumer.entity.Consumer;
import com.backend.consumer.service.ConsumerExtractionService;
import com.backend.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConsumerController {
    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ConsumerExtractionService consumerExtractionService;

    @GetMapping("/consumer")
    public ResponseEntity<List<RestaurantDTO>> getListOfRestaurants(){
        List<RestaurantDTO> restaurantDTOList = consumerService.getAllRestaurants();
        if(restaurantDTOList == null){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(restaurantDTOList);

    }

    @GetMapping("/consumer/{restaurantId}")
    public ResponseEntity<List<DishListDTO>> getDishListFromRestaurantId(@PathVariable("restaurantId")Long restaurantId){
        List<DishListDTO> dishListDTOList = consumerService.getDishListFromRestaurantId(restaurantId);
        if(dishListDTOList == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(dishListDTOList);
    }
//
    @GetMapping("/consumer/dish/{dishId}")
    public ResponseEntity<DishDetailDTO> getDishDetailFromDishId(@PathVariable("dishId")Long dishId){
        DishDetailDTO dishDetailDTO = consumerService.getDishDetailFromDishId(dishId);
        if(dishDetailDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(dishDetailDTO);
        }

    }

    @PutMapping("/consumer/update/{dishId}")
    public HttpStatus updateCart(@PathVariable("dishId") Long dishId, @RequestHeader("Authorization")String authHeader, @RequestBody QuantityDTO quantityDTO){

        Long consumerId = consumerExtractionService.getConsumerIdFromAuthHeader(authHeader);
        if(consumerId == null){
            return HttpStatus.FORBIDDEN;
        }

       boolean isCartUpdated =  consumerService.updateCart(dishId, consumerId, quantityDTO);
       if(isCartUpdated){
           return HttpStatus.OK;
       }
       else{
           return HttpStatus.NOT_ACCEPTABLE;
       }
    }

    @GetMapping("/consumer/cart")
    public ResponseEntity<List<CartDTO>> getActiveCartByConsumerId(@RequestHeader("Authorization") String authHeader){

        Long consumerId = consumerExtractionService.getConsumerIdFromAuthHeader(authHeader);
        if(consumerId == null){
            return ResponseEntity.ok(null);
        }

        List<CartDTO>cartDTOList = consumerService.getActiveCartByConsumerId(consumerId);

        if(cartDTOList == null){
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(cartDTOList);
    }

    @PostMapping("/consumer/checkout")
    public HttpStatus checkout(@RequestHeader("Authorization") String authHeader, @RequestBody OrderDTO orderDTO){

        Long consumerId = consumerExtractionService.getConsumerIdFromAuthHeader(authHeader);
        if(consumerId == null){
            return HttpStatus.FORBIDDEN;
        }

        boolean isOrderCreated = consumerService.checkout(consumerId, orderDTO);

        if(isOrderCreated){
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.NOT_ACCEPTABLE;
        }

    }

    @DeleteMapping("/consumer/remove/{cartId}")
    public HttpStatus removeCartItemFromActiveCart(@RequestHeader("Authorization")String authHeader, @PathVariable("cartId")Long cartId){
        Long consumerId = consumerExtractionService.getConsumerIdFromAuthHeader(authHeader);
        if(consumerId == null){
            return HttpStatus.FORBIDDEN;
        }

        boolean isDeleted = consumerService.removeItemFromCart(cartId, consumerId);

        if(isDeleted){
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.NOT_ACCEPTABLE;
        }
    }






}
