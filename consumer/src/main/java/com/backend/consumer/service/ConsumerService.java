package com.backend.consumer.service;

import com.backend.consumer.dto.*;
import com.backend.consumer.entity.*;
import com.backend.consumer.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConsumerService {

    @Autowired
    private ConsumerRepository consumerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;


    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;



    @Value("${serve.image}")
    private String imageServingURL;

    public Consumer registerConsumer(Consumer consumer) {
        Optional<Consumer>consumerByEmailId =
                consumerRepository.findByConsumerEmailIdIgnoreCase(consumer.getConsumerEmailId());

        if(!consumerByEmailId.isPresent()){
            consumerRepository.save(consumer);
            return consumer;
        }
        else{
            return null;
        }
    }

    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurantList = restaurantRepository.findAll();

        if(restaurantList.isEmpty()){
            return null;
        }

        List<RestaurantDTO> restaurantDTOList = new ArrayList<>();

        for(Restaurant restaurant : restaurantList){
            RestaurantDTO restaurantDTO = RestaurantDTO.builder()
                    .restaurantId(restaurant.getRestaurantId())
                    .restaurantName(restaurant.getRestaurantName())
                    .restaurantAddress(restaurant.getRestaurantAddress())
                    .restaurantEmailId(restaurant.getRestaurantEmailId())
                    .build();

            restaurantDTOList.add(restaurantDTO);
        }

        return restaurantDTOList;
    }

    public List<DishListDTO> getDishListFromRestaurantId(Long restaurantId) {
        List<Dish> dishList = dishRepository.getDishListFromRestaurantId(restaurantId);
        if(dishList.isEmpty())
        {
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
                    .build();

            dishListDTOList.add(dishListDTO);
        }

        return dishListDTOList;
    }

    public DishDetailDTO getDishDetailFromDishId(Long dishId) {
        Optional<Dish>dish = dishRepository.getDishDetailFromDishId(dishId);
        if(!dish.isPresent()){
            return null;
        }
        else{
            DishDetailDTO dishDetailDTO = DishDetailDTO.builder()
                    .dishId(dish.get().getDishId())
                    .dishName(dish.get().getDishName())
                    .dishDescription(dish.get().getDishDescription())
                    .dishPrice(dish.get().getDishPrice())
                    .dishImageUrl(dish.get().getDishImageUrl())
                    .restaurantID(dish.get().getRestaurant().getRestaurantId())
                    .build();

            return dishDetailDTO;
        }

    }

    @Transactional
    public boolean updateCart(Long dishId, Long consumerId, QuantityDTO quantityDTO) {
        System.out.println(quantityDTO.getQuantity());
        //Gets the cart item if already exists and in IN_CART_STATUS
        Optional<Cart>activeCartByDishIdAndConsumerId =
                cartRepository.getActiveCartByDishIdAndConsumerId(dishId, consumerId);

        //if the cart is present and in IN_CART_STATUS then set quantity
        if(activeCartByDishIdAndConsumerId.isPresent()){
            System.out.println("Cart found ");
            if(activeCartByDishIdAndConsumerId.get().getDish().isDeleted()){
                activeCartByDishIdAndConsumerId.get().setStatus(cartRepository.DELETED_STATUS);
                System.out.println("Set to deleted status");
                return false;
            }
            //if quantity is zero then delete
            if(quantityDTO.getQuantity() == 0){
                System.out.println("remove cart");
                cartRepository.delete(activeCartByDishIdAndConsumerId.get());
            }
            else{
                //set quantity
                activeCartByDishIdAndConsumerId.get().setQuantity(quantityDTO.getQuantity());
                System.out.println("updated cart");
            }
            return true;
        }
        else{
            //check is consumer and dish exists
            Optional<Consumer> consumerByConsumerId =
                    consumerRepository.findById(consumerId);
            Optional<Dish> dishByDishId =
                    dishRepository.findNotDeletedDishByDishId(dishId);
            //if not return
            if(!consumerByConsumerId.isPresent() || !dishByDishId.isPresent()){
                return false;
            }
            else{

                if(quantityDTO.getQuantity()== 0){
                    return false;
                }
                else{
                    Cart cartToBeSaved = Cart.builder()
                            .quantity(quantityDTO.getQuantity())
                            .dish(dishByDishId.get())
                            .consumer(consumerByConsumerId.get())
                            .build();
                    cartRepository.save(cartToBeSaved);

                    System.out.println("cart saved");

                    return true;
                }

            }
        }

    }

    public List<CartDTO> getActiveCartByConsumerId(Long consumerId) {
        List<Cart> activeCartListByConsumerId = cartRepository.getActiveCartByConsumerId(consumerId);
        if(activeCartListByConsumerId == null){
            return null;
        }
        List<CartDTO> cartDTOList = new ArrayList<>();

        for(Cart cart : activeCartListByConsumerId){
            CartDTO cartDTO = CartDTO.builder()
                    .cartId(cart.getCartId())
                    .consumerId(cart.getConsumer().getConsumerId())
                    .dishId(cart.getDish().getDishId())
                    .dishName(cart.getDish().getDishName())
                    .quantity(cart.getQuantity())
                    .totalPrice((long) (cart.getDish().getDishPrice() * cart.getQuantity()))
                    .build();

            cartDTOList.add(cartDTO);
        }
        return cartDTOList;
    }
    @Transactional
    public boolean checkout(Long consumerId, OrderDTO orderDTO) {
        List<Cart> activeCartListByConsumerId =cartRepository.getActiveCartByConsumerId(consumerId);
        Optional<Consumer>consumer = consumerRepository.findById(consumerId);

        //if cart list not empty and consumer exists then create an order and update IN_CART_STATUS
        //to ORDERED_STATUS
        if(activeCartListByConsumerId!=null && consumer.isPresent()){
            Order newOrder = Order.builder()
                    .phoneNumber(orderDTO.getPhoneNumber())
                    .address(orderDTO.getAddress())
                    .consumer(consumer.get())
                    .orderTime(LocalDateTime.now())
                    .build();
            //calculate total cost
            //set cart items to ordered_status
            //also set the order time in each cart
            double totalCost = 0L;
            for(Cart cart : activeCartListByConsumerId){
                cart.setStatus(cartRepository.ORDERED_STATUS);
                cart.setLocalDateTime(LocalDateTime.now());
                totalCost += (cart.getQuantity() * cart.getDish().getDishPrice());
            }
            //set total cost of order and save
            newOrder.setTotalCost(totalCost);
            orderRepository.save(newOrder);

            //again iterate over cart and set order
            for(Cart cart: activeCartListByConsumerId){
                cart.setOrder(newOrder);
            }
            return true;
        }
        else{
            return false;
        }

    }

    public boolean removeItemFromCart(Long cartId, Long consumerId) {
        Optional<Cart> cartByCartIdAndConsumerId = cartRepository.getActiveCartByCartIdAndConsumerId(cartId, consumerId);
        if(cartByCartIdAndConsumerId.isEmpty()){
            return false;
        }
        cartRepository.delete(cartByCartIdAndConsumerId.get());
        return true;
    }
}
