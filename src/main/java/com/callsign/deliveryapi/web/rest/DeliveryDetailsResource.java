package com.callsign.deliveryapi.web.rest;

import com.callsign.deliveryapi.repository.DeliveryDetailsRepository;
import com.callsign.deliveryapi.service.DeliveryDetailsService;
import com.callsign.deliveryapi.service.dto.DeliveryDetailsDTO;
import com.callsign.deliveryapi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.callsign.deliveryapi.domain.DeliveryDetails}.
 */
@RestController
@RequestMapping("/api")
public class DeliveryDetailsResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryDetailsResource.class);

    private static final String ENTITY_NAME = "callSignDeliveryDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryDetailsService deliveryDetailsService;

    private final DeliveryDetailsRepository deliveryDetailsRepository;

    public DeliveryDetailsResource(DeliveryDetailsService deliveryDetailsService, DeliveryDetailsRepository deliveryDetailsRepository) {
        this.deliveryDetailsService = deliveryDetailsService;
        this.deliveryDetailsRepository = deliveryDetailsRepository;
    }

    /**
     * {@code POST  /delivery-details} : Create a new deliveryDetails.
     *
     * @param deliveryDetailsDTO the deliveryDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryDetailsDTO, or with status {@code 400 (Bad Request)} if the deliveryDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-details")
    public ResponseEntity<DeliveryDetailsDTO> createDeliveryDetails(@RequestBody DeliveryDetailsDTO deliveryDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryDetails : {}", deliveryDetailsDTO);
        if (deliveryDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliveryDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliveryDetailsDTO result = deliveryDetailsService.save(deliveryDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/delivery-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delivery-details/:id} : Updates an existing deliveryDetails.
     *
     * @param id the id of the deliveryDetailsDTO to save.
     * @param deliveryDetailsDTO the deliveryDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-details/{id}")
    public ResponseEntity<DeliveryDetailsDTO> updateDeliveryDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeliveryDetailsDTO deliveryDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryDetails : {}, {}", id, deliveryDetailsDTO);
        if (deliveryDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeliveryDetailsDTO result = deliveryDetailsService.save(deliveryDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /delivery-details/:id} : Partial updates given fields of an existing deliveryDetails, field will ignore if it is null
     *
     * @param id the id of the deliveryDetailsDTO to save.
     * @param deliveryDetailsDTO the deliveryDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliveryDetailsDTO> partialUpdateDeliveryDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeliveryDetailsDTO deliveryDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryDetails partially : {}, {}", id, deliveryDetailsDTO);
        if (deliveryDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliveryDetailsDTO> result = deliveryDetailsService.partialUpdate(deliveryDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /delivery-details} : get all the deliveryDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryDetails in body.
     */
    @GetMapping("/delivery-details")
    public List<DeliveryDetailsDTO> getAllDeliveryDetails() {
        log.debug("REST request to get all DeliveryDetails");
        return deliveryDetailsService.findAll();
    }

    /**
     * {@code GET  /delivery-details/:id} : get the "id" deliveryDetails.
     *
     * @param id the id of the deliveryDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-details/{id}")
    public ResponseEntity<DeliveryDetailsDTO> getDeliveryDetails(@PathVariable Long id) {
        log.debug("REST request to get DeliveryDetails : {}", id);
        Optional<DeliveryDetailsDTO> deliveryDetailsDTO = deliveryDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryDetailsDTO);
    }

    /**
     * {@code DELETE  /delivery-details/:id} : delete the "id" deliveryDetails.
     *
     * @param id the id of the deliveryDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delivery-details/{id}")
    public ResponseEntity<Void> deleteDeliveryDetails(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryDetails : {}", id);
        deliveryDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
