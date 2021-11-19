package ru.bachinin.cardealership.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.bachinin.cardealership.dto.UserRatingDTO;

@FeignClient(name = "${credit-rating.name}")
public interface CreditRatingFeignProxy {
    @PostMapping(value = "/clients", consumes = {MediaType.APPLICATION_XML_VALUE})
    Integer calculateRating(@RequestBody UserRatingDTO clientDTO);
}
