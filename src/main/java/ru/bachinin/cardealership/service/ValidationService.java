package ru.bachinin.cardealership.service;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.exceptions.BadParamException;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.exceptions.RequestBodyNotProvidedException;
import ru.bachinin.cardealership.exceptions.ValueNotFoundException;

import java.io.Serializable;
import java.util.Map;

@Service
public interface ValidationService {
    static void checkMapNullOrEmpty(Map<String, ?> requestMap)
            throws RequestBodyNotProvidedException {
        if (requestMap == null || requestMap.isEmpty()) {
            throw new RequestBodyNotProvidedException();
        }
    }

    static void checkMapValue(Map<String, ?> requestMap, String key)
            throws ValueNotFoundException {
        if (requestMap.get(key) == null) {
            throw new ValueNotFoundException(key);
        }
    }

    public static void checkUserValue(User user, String key)
            throws ValueNotFoundException {
        switch (key) {
            case "login":
                if (user.getLogin() == null) {
                    throw new ValueNotFoundException(key);
                }
                break;
            case "password":
                if (user.getPassword() == null) {
                    throw new ValueNotFoundException(key);
                }
                break;
            case "surname":
                if (user.getSurname() == null) {
                    throw new ValueNotFoundException("surname");
                }
                break;
            default:
                break;
        }


    }

    static void checkExistence(PagingAndSortingRepository<? extends Serializable, Long> repository,
                                Long id, String className)
            throws EntityNotFoundException {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(id, className);
        }
    }

    static Long parseLong(Map<String, ?> requestMap, String key) throws BadParamException {
        try {
            return Long.valueOf((Integer) requestMap.get(key));
        } catch (ClassCastException e) {
            throw new BadParamException(key, Long.class.getName());
        }
    }
}
