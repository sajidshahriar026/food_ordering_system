package com.backend.restaurant.controller;

import com.backend.restaurant.dto.*;
import com.backend.restaurant.entity.Restaurant;
import com.backend.restaurant.service.RestaurantExtractionService;
import com.backend.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    RestaurantExtractionService restaurantExtractionService;


//    @PostMapping("/restaurant/add")
//    public HttpStatus addDish(@RequestParam("file")MultipartFile file, @RequestParam("dishData")String dishData, @RequestHeader("Authorization")String authHeader){
//
//        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);
//
//        if(restaurantId == null){
//            return HttpStatus.FORBIDDEN;
//        }
//
//        boolean isSaved = restaurantService.addDish(file, dishData, restaurantId);
//
//        if (isSaved) {
//            return HttpStatus.CREATED;
//        } else {
//            return HttpStatus.BAD_REQUEST;
//        }
//    }
    @PostMapping("/restaurant/add")
    public HttpStatus addDish(@RequestHeader("Authorization")String authHeader, @RequestBody AddDishDTO addDishDTO){
//        System.out.println(authHeader);
        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);
        if(restaurantId == null){
            return HttpStatus.FORBIDDEN;
        }

        boolean isAdded = restaurantService.addDish(addDishDTO, restaurantId);

        return isAdded ? HttpStatus.CREATED : HttpStatus.NOT_ACCEPTABLE;

    }

    @PutMapping("/restaurant/update/{dishId}")
    public HttpStatus updateDish(@RequestHeader("Authorization") String authHeader, @PathVariable("dishId")Long dishId, @RequestBody UpdateDishDTO updateDishDTO){
        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);
        if(restaurantId == null){
            return HttpStatus.FORBIDDEN;
        }
        boolean isUpdated = restaurantService.updateDish(restaurantId,dishId,updateDishDTO);

        if(isUpdated){
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.NOT_ACCEPTABLE;
        }
    }

//    @PutMapping("restaurant/update/{dishId}")
//    public HttpStatus updateDish(
//            @RequestParam("file")MultipartFile file,
//            @RequestParam("dishData")String dishData,
//            @PathVariable("dishId")Long dishId,
//            @RequestHeader("Authorization")String authHeader
//    ){
//
//        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);
//
//        if(restaurantId == null){
//            return HttpStatus.FORBIDDEN;
//        }
//
//
//
//        boolean isUpdated = restaurantService.updateDish(file, dishData, restaurantId, dishId);
//
//        if(isUpdated){
//            return HttpStatus.ACCEPTED;
//        }
//        else{
//            return HttpStatus.BAD_REQUEST;
//        }
//    }

    @GetMapping("/restaurant")
    public ResponseEntity<List<DishListDTO>> getDishListByRestaurantId(@RequestHeader("Authorization")String authHeader){

        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);

        if(restaurantId == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        List<DishListDTO> dishListDTOList = restaurantService.getDishListByRestaurantId(restaurantId);

        if(dishListDTOList == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(null);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(dishListDTOList);
    }


    @GetMapping("/restaurant/dish/{dishId}")
    public ResponseEntity<DishDetailDTO> getDishDetailByDishIdAndRestaurantId(@RequestHeader("Authorization")String authHeader, @PathVariable("dishId")Long dishId){
        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);

        if(restaurantId == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }



        DishDetailDTO dishDetailDTO = restaurantService.getDishDetailByDishIdAndRestaurantId(restaurantId, dishId);

        if(dishDetailDTO != null){
            return ResponseEntity.status(HttpStatus.OK).body(dishDetailDTO);
        }
        else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @DeleteMapping("restaurant/delete/{dishId}")
    public HttpStatus deleteDish(@RequestHeader("Authorization")String authHeader, @PathVariable("dishId")Long dishId){

        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);

        if(restaurantId == null){
            System.out.println("Restaurant ID not found");
            return HttpStatus.FORBIDDEN;
        }


        boolean isDishDeleted = restaurantService.deleteDish(restaurantId,dishId);

        if(isDishDeleted){
            System.out.println("Dish Deleted");
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.NOT_ACCEPTABLE;
        }
    }

    @GetMapping("restaurant/order")
    public ResponseEntity<List<OrderDTO>> getOrderListByRestaurantId(@RequestHeader("Authorization")String authHeader){

        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);

        if(restaurantId == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        List<OrderDTO>orderDTOList = restaurantService.getOrderListByRestaurantId(restaurantId);

        if(orderDTOList == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(orderDTOList);
        }
    }

    @PutMapping("/restaurant/order/{cartId}")
    public HttpStatus deliverOrder(@RequestHeader("Authorization")String authHeader, @PathVariable("cartId")Long cartId){

        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);

        if(restaurantId == null){
            return HttpStatus.FORBIDDEN;
        }

        boolean isDelivered = restaurantService.deliverOrder(restaurantId, cartId);

        if(isDelivered){
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.NOT_ACCEPTABLE;
        }
    }

    @PutMapping("/restaurant/order/cancel/{cartId}")
    public HttpStatus cancelOrder(@RequestHeader("Authorization")String authHeader, @PathVariable("cartId")Long cartId){

        Long restaurantId = restaurantExtractionService.getRestaurantIdFromAuthHeader(authHeader);

        if(restaurantId == null){
            return HttpStatus.FORBIDDEN;
        }


        boolean isCanceled = restaurantService.cancelOrder(restaurantId,cartId);
        if(isCanceled){
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.NOT_ACCEPTABLE;
        }

    }


}
