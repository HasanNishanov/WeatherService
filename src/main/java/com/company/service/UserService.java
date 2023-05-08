package com.company.service;

import com.company.enums.ProfileStatus;
import com.company.model.dto.UserDTO;
import com.company.model.entity.UserEntity;
import com.company.repository.CityRepository;
import com.company.repository.SubscriptionRepository;
import com.company.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final SubscriptionRepository subscriptionRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, CityRepository cityRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public String encode(String password) {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        return b.encode(password);
    }

//    public Mono<String> subscribeToCity(Long userId, String cityName) {
//        return cityRepository.findByName(cityName)
//                .switchIfEmpty(Mono.error(new NotFoundException("City not found")))
//                .flatMap(city -> userRepository.findById(userId)
//                        .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
//                        .flatMap(subscription-updated.html.html -> {
//                            subscription-updated.html.html.setSubscriptions(cityName);
//                            return userRepository.save(subscription-updated.html.html)
//                                    .flatMap(savedUser -> {
//                                        // Создаем новую запись в таблице Subscriptions
//                                        SubscriptionEntity subscription = new SubscriptionEntity();
//                                        subscription.setUser_id(userId);
//                                        subscription.setCity_name(city.getName());
//                                        return subscriptionRepository.save(subscription);
//                                    })
//                                    .thenReturn("Successfully subscribed to city " + cityName);
//                        }));
//    }
public Mono<Void> updateCitySubscription(Long userId, String cityName) {
    return subscriptionRepository.saveSubscription(Math.toIntExact(userId), cityName);
}

    public Mono<Object> createUser(UserEntity user) {
        return userRepository.findByUsername(user.getUsername())
                .flatMap(foundUser -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken")))
                .switchIfEmpty(Mono.defer(() -> {
                    UserEntity userEntity = modelMapper.map(user, UserEntity.class);
                    userEntity.setPassword(encode(userEntity.getPassword()));
                    userEntity.setRole(ProfileStatus.USER);
                    return userRepository.save(userEntity)
                            .map(savedUser -> modelMapper.map(savedUser, UserDTO.class));
                }))
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create subscription-updated.html.html")));
    }

    public Mono<Integer> updateUserDescription(Long id, String cityName) {
        return userRepository.updateCitySubscription(id,cityName);
    }




    public Mono<Void> deleteUser(Long userId) {
        return userRepository.deleteById(userId);
    }
}


