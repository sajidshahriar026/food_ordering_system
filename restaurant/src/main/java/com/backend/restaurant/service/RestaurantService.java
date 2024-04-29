package com.backend.restaurant.service;

import com.backend.restaurant.dto.*;
import com.backend.restaurant.entity.Cart;
import com.backend.restaurant.entity.Dish;
import com.backend.restaurant.entity.Order;
import com.backend.restaurant.entity.Restaurant;
import com.backend.restaurant.repository.CartRepository;
import com.backend.restaurant.repository.DishRepository;
import com.backend.restaurant.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.*;

import com.backend.restaurant.utility.ImageHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RestaurantService {

    @Autowired
    private ImageHelper imageHelper;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CartRepository cartRepository;

    @Value("${project.image}")
    private String imageDestinationPath;

    @Value("${serve.image}")
    private String imageServingURL;

    @Autowired
    ObjectMapper objectMapper;

//    @Transactional
//    public boolean addDish(MultipartFile file, String dishData, Long restaurantId) {
//        Dish dish;
//        boolean isUploaded = false;
//        String newFileName = UUID.randomUUID().toString();
//
//        try{
//            dish = objectMapper.readValue(dishData, Dish.class);
//        }
//        catch(JsonProcessingException e){
//            return false;
//        }
//
//        if(dish.getDishName() == null || dish.getDishName().isEmpty() || dish.getDishPrice() == 0){
//            return false;
//        }
//
//        //check if restaurant exists
//        Optional<Restaurant> restaurantByRestaurantId = restaurantRepository.findById(restaurantId);
//        //if not return false
//        if(!restaurantByRestaurantId.isPresent()){
//            return false;
//        }
//
//
//        //check if the dish name is matching another dish name of the restaurant
//        Optional<Dish> dishByDishName = dishRepository.findByDishNameAndRestaurantId(dish.getDishName(),restaurantByRestaurantId.get().getRestaurantId());
//
//
//        //if matches return false
//        if(dishByDishName.isPresent()){
//            return false;
//        }
//
//
//        //if file not empty upload image
//        if(!file.isEmpty()){
//            newFileName += '.' + Files.getFileExtension(file.getOriginalFilename());
//
//            isUploaded = imageHelper.uploadImage(imageDestinationPath,file, newFileName);
//        }
//
//
//        dish.setRestaurant(restaurantByRestaurantId.get());
//        //save the image url in database
//        if(isUploaded){
//            System.out.println("Image uploaded");
//            dish.setDishImageUrl(newFileName);
//        }
//
//        dishRepository.save(dish);
//
//        return true;
//
//
//    }
//    @Transactional
//    public boolean updateDish(MultipartFile file, String dishData, Long restaurantId, Long dishId) {
//        Dish dish=null;
//        boolean isUploaded = false;
//        String newFileName = UUID.randomUUID().toString();
//        boolean isImageDeleted = false;
//
//        if(!dishData.isEmpty()){
//            try{
//                dish = objectMapper.readValue(dishData, Dish.class);
//            }
//            catch(JsonProcessingException e){
//                return false;
//            }
//        }
//
//        System.out.println("passes the mapper test");
//        //if restaurant does not exist return false
//
//        Optional<Restaurant> restaurantByRestaurantId = restaurantRepository.findById(restaurantId);
//
//        if(!restaurantByRestaurantId.isPresent()){
//            return false;
//        }
//
//        System.out.println("restaurant exists");
//
//
//        if(dish != null && dish.getDishName() != null && !dish.getDishName().isEmpty()){
//            //check if the dish name is matching another dish name of the restaurant
//            Optional<Dish> dishByDishName =
//                    dishRepository.findByDishNameAndRestaurantId(dish.getDishName(),restaurantByRestaurantId.get().getRestaurantId());
//            //if matches return false
//
//            if(dishByDishName.isPresent()){
//                return false;
//            }
//        }
//        System.out.println("passes the dish name unique test");
//
//        //if dish to be updated does not exist return false
//        Optional<Dish>dishToBeUpdatedOptional = dishRepository.findById(dishId);
//
//
//
//        if(!dishToBeUpdatedOptional.isPresent()){
//            return false;
//        }
//        System.out.println("dish exists");
//
//        //update dish
//        Dish dishToBeUpdated = dishToBeUpdatedOptional.get();
//
//        if (dish != null && dish.getDishName() != null && !dish.getDishName().isEmpty()){
//            dishToBeUpdated.setDishName(dish.getDishName());
//        }
//
//        if(dish!=null && dish.getDishDescription() != null && !dish.getDishDescription().isEmpty()){
//            dishToBeUpdated.setDishDescription(dish.getDishDescription());
//        }
//        if(dish!= null && dish.getDishPrice() > 0){
//            dishToBeUpdated.setDishPrice(dish.getDishPrice());
//        }
//        System.out.println("updated description");
//
//        if(!file.isEmpty()){
//            if(dishToBeUpdated.getDishImageUrl()!=null){
//                imageHelper.deleteImage(imageDestinationPath, dishToBeUpdated.getDishImageUrl());
//            }
//            newFileName += '.' + Files.getFileExtension(file.getOriginalFilename());
//            isUploaded = imageHelper.uploadImage(imageDestinationPath, file, newFileName);
//            if(isUploaded){
//                dishToBeUpdated.setDishImageUrl(newFileName);
//            }
//
//        }
//        System.out.println("update sucessful");
//
//        return true;
//    }

    public boolean addDish(AddDishDTO addDishDTO, Long restaurantId){
        Optional<Restaurant>restaurantByRestaurantId = restaurantRepository.findById(restaurantId);
        Optional<Dish> dishByDishNameAndRestaurantId = dishRepository.findByDishNameAndRestaurantId(addDishDTO.getDishName(),restaurantId);
        if(dishByDishNameAndRestaurantId.isPresent() || restaurantByRestaurantId.isEmpty()){
            return false;
        }

        Dish dish = Dish.builder()
                .dishName(addDishDTO.getDishName())
                .dishDescription(addDishDTO.getDishDescription())
                .dishPrice(addDishDTO.getDishPrice())
                .dishImageUrl(addDishDTO.getDishImageUrl())
                .restaurant(restaurantByRestaurantId.get())
                .build();

        dishRepository.save(dish);
        return true;
    }


    public List<DishListDTO> getDishListByRestaurantId(Long restaurantId) {
        List<Dish>dishList = dishRepository.getDishListByRestaurantId(restaurantId);

        if(dishList.isEmpty()){
            return null;
        }

        List<DishListDTO> dishListDTOList = new ArrayList<>();

        for(Dish dish : dishList){
            DishListDTO dishListDTO = DishListDTO.builder()
                    .dishId(dish.getDishId())
                    .dishName(dish.getDishName())
                    .dishDescription(dish.getDishDescription())
                    .dishPrice(dish.getDishPrice())
                    .restaurantId(dish.getRestaurant().getRestaurantId())
                    .restaurantName(dish.getRestaurant().getRestaurantName())
                    .restaurantAddress(dish.getRestaurant().getRestaurantAddress())
                    .build();

            dishListDTOList.add(dishListDTO);
        }

        return dishListDTOList;
    }

    public DishDetailDTO getDishDetailByDishIdAndRestaurantId(Long restaurantId, Long dishId) {
        Optional<Dish>dish = dishRepository.findByDishIdAndRestaurantId(restaurantId, dishId);
        if(dish.isEmpty()){
            return null;
        }
        else{
            DishDetailDTO dishDetailDTO = DishDetailDTO.builder()
                    .dishId(dish.get().getDishId())
                    .dishDescription(dish.get().getDishDescription())
                    .dishPrice(dish.get().getDishPrice())
                    .dishName(dish.get().getDishName())
                    .restaurantID(dish.get().getRestaurant().getRestaurantId())
                    .dishImageUrl(dish.get().getDishImageUrl())
                    .build();

            return dishDetailDTO;
        }
    }

    @Transactional
    public boolean deleteDish(Long restaurantId, Long dishId) {
        Optional<Dish>dishByDishId = dishRepository.findById(dishId);

        if(!dishByDishId.isPresent()){
            return false;
        }

        if(Objects.equals(dishByDishId.get().getRestaurant().getRestaurantId(), restaurantId)){
            dishByDishId.get().setDeleted(true);
            List<Cart> cartListOfActiveAndOrderedStatusByDishId = cartRepository.getOrderedAndActiveCartByDishId(dishByDishId.get().getDishId());

            if(cartListOfActiveAndOrderedStatusByDishId == null){
                return true;
            }

            for(Cart cart: cartListOfActiveAndOrderedStatusByDishId){
                if(cart.getStatus() == cartRepository.ORDERED_STATUS){
                    cart.getOrder().setTotalCost(cart.getOrder().getTotalCost() - (cart.getDish().getDishPrice() * cart.getQuantity()));

                }
                cart.setStatus(cartRepository.DELETED_STATUS);
                cart.setLocalDateTime(LocalDateTime.now());
            }
            return true;
        }
        else{
            return false;
        }

    }

    public List<OrderDTO> getOrderListByRestaurantId(Long restaurantId) {

        List<Cart> orderListByRestaurantId = cartRepository.getOrderListByRestaurantId(restaurantId);

        if(orderListByRestaurantId == null){
            return null;
        }
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Cart cart : orderListByRestaurantId){
            OrderDTO orderDTO = OrderDTO.builder()
                    .cartId(cart.getCartId())
                    .orderId(cart.getOrder().getOrderId())
                    .quantity(cart.getQuantity())
                    .totalEarnings(cart.getQuantity() * cart.getDish().getDishPrice())
                    .dishName(cart.getDish().getDishName())
                    .consumerName(cart.getConsumer().getConsumerName())
                    .build();

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;



    }

    @Transactional
    public boolean deliverOrder(Long restaurantId, Long cartId) {
        Optional<Cart> cartByCartId = cartRepository.findById(cartId);

        if(!cartByCartId.isPresent()){
            return false;
        }

        if(cartByCartId.get().getDish().getRestaurant().getRestaurantId() != restaurantId){
            return false;
        }

        if(cartByCartId.get().getStatus() == cartRepository.ORDERED_STATUS) {
            cartByCartId.get().setStatus(cartRepository.DELIVERED_STATUS);
            cartByCartId.get().setLocalDateTime(LocalDateTime.now());

            //give consumer a notification

            //save notification

            return true;

        }
        else{
            return false;
        }
    }
    @Transactional
    public boolean cancelOrder(Long restaurantId, Long cartId){
        Optional<Cart> cartByCartId = cartRepository.findById(cartId);

        if(!cartByCartId.isPresent()){
            return false;
        }

        if(cartByCartId.get().getDish().getRestaurant().getRestaurantId() != restaurantId){
            return false;
        }

        if(cartByCartId.get().getStatus() == cartRepository.ORDERED_STATUS){

            //update bill
            cartByCartId.get().getOrder().setTotalCost(
                    cartByCartId.get().getOrder().getTotalCost() -
                            (cartByCartId.get().getDish().getDishPrice() * cartByCartId.get().getQuantity())
            );
            //set cancelled_status
            cartByCartId.get().setStatus(cartRepository.CANCELLED_STATUS);



            return true;
        }
        else{
            return false;
        }

    }
    @Transactional
    public boolean updateDish(Long restaurantId, Long dishId, UpdateDishDTO updateDishDTO) {
        Optional<Dish> dishByDishIdAndRestaurantId = dishRepository.findByDishIdAndRestaurantId(restaurantId,dishId);

        if(dishByDishIdAndRestaurantId.isEmpty()){
            return false;
        }
        //update operation
        if(updateDishDTO.getDishName()!=null && !updateDishDTO.getDishName().isEmpty()){
            dishByDishIdAndRestaurantId.get().setDishName(updateDishDTO.getDishName());
        }
        if(updateDishDTO.getDishDescription()!=null && !updateDishDTO.getDishDescription().isEmpty()){
            dishByDishIdAndRestaurantId.get().setDishDescription(updateDishDTO.getDishDescription());
        }
        if(updateDishDTO.getDishImageUrl()!=null && !updateDishDTO.getDishImageUrl().isEmpty()){
            dishByDishIdAndRestaurantId.get().setDishDescription(updateDishDTO.getDishImageUrl());
        }
        if(updateDishDTO.getDishPrice() != 0){
            dishByDishIdAndRestaurantId.get().setDishPrice(updateDishDTO.getDishPrice());
        }
        dishRepository.save(dishByDishIdAndRestaurantId.get());

        List<Cart> allActiveAndOrderedCartByDishId = cartRepository.getOrderedAndActiveCartByDishId(dishId);
        if(allActiveAndOrderedCartByDishId.isEmpty()){
            return true; //dish update complete
        }

        for(Cart cart : allActiveAndOrderedCartByDishId){
            if(cart.getStatus() == cartRepository.IN_CART_STATUS){
                cart.setStatus(cartRepository.CANCELLED_STATUS);
            }
            if(cart.getStatus() == cartRepository.ORDERED_STATUS){
                Order order = cart.getOrder();
                order.setTotalCost(order.getTotalCost() - (cart.getDish().getDishPrice() * cart.getQuantity()));
                cart.setStatus(cartRepository.CANCELLED_STATUS);
            }


            }

        return true;
    }
}
