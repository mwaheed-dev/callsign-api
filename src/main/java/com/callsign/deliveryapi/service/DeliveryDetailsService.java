package com.callsign.deliveryapi.service;

import com.callsign.deliveryapi.domain.DeliveryDetails;
import com.callsign.deliveryapi.repository.DeliveryDetailsRepository;
import com.callsign.deliveryapi.service.dto.DeliveryDetailsDTO;
import com.callsign.deliveryapi.service.mapper.DeliveryDetailsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeliveryDetails}.
 */
@Service
@Transactional
public class DeliveryDetailsService {

    private final Logger log = LoggerFactory.getLogger(DeliveryDetailsService.class);

    private final DeliveryDetailsRepository deliveryDetailsRepository;

    private final DeliveryDetailsMapper deliveryDetailsMapper;

    public DeliveryDetailsService(DeliveryDetailsRepository deliveryDetailsRepository, DeliveryDetailsMapper deliveryDetailsMapper) {
        this.deliveryDetailsRepository = deliveryDetailsRepository;
        this.deliveryDetailsMapper = deliveryDetailsMapper;
    }

    /**
     * Save a deliveryDetails.
     *
     * @param deliveryDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public DeliveryDetailsDTO save(DeliveryDetailsDTO deliveryDetailsDTO) {
        log.debug("Request to save DeliveryDetails : {}", deliveryDetailsDTO);
        DeliveryDetails deliveryDetails = deliveryDetailsMapper.toEntity(deliveryDetailsDTO);
        deliveryDetails = deliveryDetailsRepository.save(deliveryDetails);
        return deliveryDetailsMapper.toDto(deliveryDetails);
    }

    /**
     * Partially update a deliveryDetails.
     *
     * @param deliveryDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryDetailsDTO> partialUpdate(DeliveryDetailsDTO deliveryDetailsDTO) {
        log.debug("Request to partially update DeliveryDetails : {}", deliveryDetailsDTO);

        return deliveryDetailsRepository
            .findById(deliveryDetailsDTO.getId())
            .map(existingDeliveryDetails -> {
                deliveryDetailsMapper.partialUpdate(existingDeliveryDetails, deliveryDetailsDTO);

                return existingDeliveryDetails;
            })
            .map(deliveryDetailsRepository::save)
            .map(deliveryDetailsMapper::toDto);
    }

    /**
     * Get all the deliveryDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DeliveryDetailsDTO> findAll() {
        log.debug("Request to get all DeliveryDetails");
        return deliveryDetailsRepository
            .findAll()
            .stream()
            .map(deliveryDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one deliveryDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryDetailsDTO> findOne(Long id) {
        log.debug("Request to get DeliveryDetails : {}", id);
        return deliveryDetailsRepository.findById(id).map(deliveryDetailsMapper::toDto);
    }

    /**
     * Delete the deliveryDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DeliveryDetails : {}", id);
        deliveryDetailsRepository.deleteById(id);
    }
}
