package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.dto.RequestInvoiceDTO;
import ru.bachinin.cardealership.dto.UpdateInvoiceDTO;
import ru.bachinin.cardealership.dto.pojos.RequestInvoiceVehicle;
import ru.bachinin.cardealership.entities.Invoice;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.entities.VehicleModel;
import ru.bachinin.cardealership.enums.InvoiceStateEnum;
import ru.bachinin.cardealership.enums.VehicleStateEnum;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.exceptions.InvalidStateException;
import ru.bachinin.cardealership.repositories.InvoiceRepository;
import ru.bachinin.cardealership.repositories.VehicleModelRepository;
import ru.bachinin.cardealership.repositories.VehicleRepository;
import ru.bachinin.cardealership.service.UserService;

import javax.validation.Valid;
import java.util.LinkedList;

@RestController
@RequestMapping("/invoices")
public class InvoicesController {

    private final InvoiceRepository invoiceRepository;
    private final UserService userService;
    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public InvoicesController(InvoiceRepository invoiceRepository,
                              UserService userService,
                              VehicleModelRepository vehicleModelRepository,
                              VehicleRepository vehicleRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
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
            throw new EntityNotFoundException(id, Invoice.class.getSimpleName());
        }
    }

    // Получение всех накладных конкретного пользователя
    @GetMapping("/user/{id_user}")
    public Page<Invoice> getAllInvoicesByUserId(@PathVariable Long id_user,
                                                @RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(defaultValue = "id") String sortBy)
            throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Invoice> invoicePage = invoiceRepository.findAllByCreatedBy_Id(id_user, pageable);
        if (invoicePage.hasContent()) {
            return invoicePage;
        } else {
            throw new EntityNotFoundException(id_user, User.class.getSimpleName());
        }
    }

    @PostMapping()
    public Invoice createInvoice(@RequestBody @Valid RequestInvoiceDTO requestInvoiceDto)
            throws EntityNotFoundException {

        Invoice invoice = new Invoice();
        invoice.setInvoiceState(InvoiceStateEnum.CREATED);

        invoice.setCreatedBy(userService.findById(requestInvoiceDto.getId_user()));

        invoice.setVehicles(new LinkedList<>());

        for (RequestInvoiceVehicle requestInvoiceVehicle: requestInvoiceDto.getVehicles()) {
            VehicleModel vehicleModel = vehicleModelRepository.getVehicleModelByName(requestInvoiceVehicle.getVehicle_model());

            if (vehicleModel == null) {
                throw new EntityNotFoundException(requestInvoiceVehicle.getVehicle_model(), VehicleModel.class.getSimpleName());
            }

            Vehicle vehicle = new Vehicle();
            vehicle.setColour(requestInvoiceVehicle.getColour());
            vehicle.setVehicleModel(vehicleModel);
            vehicle.setInvoice(invoice);
            vehicle.setVehicleStateEnum(VehicleStateEnum.CREATED);
            vehicleRepository.save(vehicle);
        }
        return invoiceRepository.save(invoice);
    }

    @PutMapping("/cancel")
    public Invoice cancelInvoice(@RequestParam Long id)
            throws EntityNotFoundException, InvalidStateException {
        if (!invoiceRepository.existsById(id)) {
            throw new EntityNotFoundException(id, Invoice.class.getSimpleName());
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
    public Invoice updateInvoice(@RequestBody @Valid UpdateInvoiceDTO updateInvoiceDTO)
            throws EntityNotFoundException {
        Long id_invoice = updateInvoiceDTO.getId_invoice();

        if (!invoiceRepository.existsById(id_invoice)) {
            throw new EntityNotFoundException(id_invoice, Invoice.class.getSimpleName());
        }

        Invoice oldInvoice = invoiceRepository.getInvoiceById(id_invoice);
        oldInvoice.updateInvoice(updateInvoiceDTO);

        return invoiceRepository.save(oldInvoice);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Long id)
            throws EntityNotFoundException {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, Invoice.class.getSimpleName());
        }
    }
}
