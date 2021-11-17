package ru.bachinin.cardealership.proxies;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.bachinin.cardealership.entities.User;

import javax.validation.Valid;

@FeignClient(name = "${credit-rating.name}")
@RibbonClient(name = "${credit-rating.name}")
public interface CreditRatingFeignProxy {
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE})
    Integer calculateRating(@Valid @RequestBody User clientDTO);

}
