package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.dto.RequestInvoiceDto;
import ru.bachinin.cardealership.entities.Invoice;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.entities.VehicleModel;
import ru.bachinin.cardealership.enums.InvoiceStateEnum;
import ru.bachinin.cardealership.enums.VehicleStateEnum;
import ru.bachinin.cardealership.exceptions.BadParamException;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.exceptions.InvalidStateException;
import ru.bachinin.cardealership.exceptions.RequestBodyNotProvidedException;
import ru.bachinin.cardealership.exceptions.ValueNotFoundException;
import ru.bachinin.cardealership.repositories.InvoiceRepository;
import ru.bachinin.cardealership.repositories.UserRepository;
import ru.bachinin.cardealership.repositories.VehicleModelRepository;
import ru.bachinin.cardealership.repositories.VehicleRepository;
import ru.bachinin.cardealership.service.ValidationService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invoices")
public class InvoicesController {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleRepository vehicleRepository;

    private final String className = Invoice.class.getName();
    private final String vehicleModelClassName = VehicleModel.class.getName();
    private final String userClassName = User.class.getName();

    @Autowired
    public InvoicesController(InvoiceRepository invoiceRepository,
                              UserRepository userRepository,
                              VehicleModelRepository vehicleModelRepository,
                              VehicleRepository vehicleRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.vehicleModelRepository = vehicleModelRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // Получение списка всех накладных
    @GetMapping()
    public Iterable<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // Получение конкретной накладной
    @GetMapping("/{id}")
    public Invoice getInvoices(@PathVariable Long id) throws EntityNotFoundException {
        if (invoiceRepository.existsById(id)) {
            return invoiceRepository.getInvoiceById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @PostMapping()
    public Invoice createInvoice(@RequestBody() RequestInvoiceDto requestInvoiceDto)
            throws EntityNotFoundException, ValueNotFoundException {

        Invoice invoice = new Invoice();
        invoice.setInvoiceState(InvoiceStateEnum.CREATED);

        invoice.setCreatedBy(userRepository.findUserById(requestInvoiceDto.getId()));

        invoice.setVehicles(new LinkedList<>());
        invoice.setCreatedAt(LocalDate.now());

        List<LinkedHashMap<String, String>> requestVehicleList = requestInvoiceDto.getRequestVehicleList();

        String keyColour = "colour";
        String keyVehicleModel = "vehicle_model";

        for (LinkedHashMap<String, String> linkedHashMap : requestVehicleList) {
            ValidationService.checkMapValue(linkedHashMap, keyColour);
            ValidationService.checkMapValue(linkedHashMap, keyVehicleModel);

            VehicleModel vehicleModel = vehicleModelRepository.getVehicleModelByName(linkedHashMap.get(keyVehicleModel));

            if (vehicleModel == null) {
                throw new EntityNotFoundException(linkedHashMap.get(keyVehicleModel), vehicleModelClassName);
            }

            Vehicle vehicle = new Vehicle();
            vehicle.setColour(linkedHashMap.get(keyColour));
            vehicle.setVehicleModel(vehicleModel);
            vehicle.setInvoice(invoice);
            vehicle.setCreatedAt(LocalDate.now());
            vehicle.setVehicleStateEnum(VehicleStateEnum.CREATED);
            vehicleRepository.save(vehicle);
        }
        return invoiceRepository.save(invoice);
    }

    @PutMapping("/cancel")
    public Invoice cancelInvoice(@RequestParam Long id)
            throws EntityNotFoundException, InvalidStateException {
        if (!invoiceRepository.existsById(id)) {
            throw new EntityNotFoundException(id, className);
        }

        Invoice invoice = invoiceRepository.getInvoiceById(id);

        if (invoice.getInvoiceState() == InvoiceStateEnum.CANCELED &&
                invoice.getInvoiceState() == InvoiceStateEnum.DONE) {
            throw new InvalidStateException("invoice");
        }

        invoice.setInvoiceState(InvoiceStateEnum.CANCELED);
        return invoiceRepository.save(invoice);
    }

    @PutMapping()
    public Invoice updateInvoice(@RequestBody(required = false) Map<String, ?> requestMap)
            throws EntityNotFoundException, RequestBodyNotProvidedException, ValueNotFoundException, BadParamException {
        ValidationService.checkMapNullOrEmpty(requestMap);

        String keyIdInvoice = "id_invoice";
        String keyUser = "id_user";
        String keyInvoice = "invoice";
        ValidationService.checkMapValue(requestMap, keyUser);
        ValidationService.checkMapValue(requestMap, keyIdInvoice);
        ValidationService.checkMapValue(requestMap, keyInvoice);

        Long id_invoice = ValidationService.parseLong(requestMap, keyIdInvoice);
        Long id_user = ValidationService.parseLong(requestMap, keyUser);

        if (!invoiceRepository.existsById(id_invoice)) {
            throw new EntityNotFoundException(id_invoice, className);
        }

        if (!userRepository.existsById(id_user)) {
            throw new EntityNotFoundException(id_user, userClassName);
        }

        Map<String, ?> updateInvoiceValues;
        try {
            updateInvoiceValues = (Map<String, ?>) requestMap.get(keyInvoice);
        } catch (ClassCastException e) {
            throw new BadParamException(keyInvoice, "Map<String, ?>>");
        }

        Invoice oldInvoice = invoiceRepository.getInvoiceById(id_invoice);

        String keyCreatedAt = "created_at";
        String keyState = "state";

        ValidationService.checkMapValue(updateInvoiceValues, keyCreatedAt);
        ValidationService.checkMapValue(updateInvoiceValues, keyState);

        try {
            oldInvoice.setCreatedAt(LocalDate.parse((String) updateInvoiceValues.get(keyCreatedAt)));
        }
        catch (ClassCastException|DateTimeParseException e) {
            throw new BadParamException(keyCreatedAt, LocalDate.class.getSimpleName());
        }

        try {
            oldInvoice.setInvoiceState(InvoiceStateEnum.valueOf((String) updateInvoiceValues.get(keyState)));
        } catch (ClassCastException e) {
            throw new BadParamException(keyState, InvoiceStateEnum.class.getName());
        }


        oldInvoice.setUpdatedAt(LocalDate.now());
        return invoiceRepository.save(oldInvoice);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Long id) throws EntityNotFoundException {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }
}
