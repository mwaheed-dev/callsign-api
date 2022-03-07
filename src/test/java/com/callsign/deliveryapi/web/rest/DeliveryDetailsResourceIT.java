package com.callsign.deliveryapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.callsign.deliveryapi.IntegrationTest;
import com.callsign.deliveryapi.domain.DeliveryDetails;
import com.callsign.deliveryapi.repository.DeliveryDetailsRepository;
import com.callsign.deliveryapi.service.dto.DeliveryDetailsDTO;
import com.callsign.deliveryapi.service.mapper.DeliveryDetailsMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeliveryDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryDetailsResourceIT {

    private static final Integer DEFAULT_DELIVERY_ID = 1;
    private static final Integer UPDATED_DELIVERY_ID = 2;

    private static final String DEFAULT_CUSTOMER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPECTED_DELIVERY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECTED_DELIVERY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TIME_TO_REACH_DESTINATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_TO_REACH_DESTINATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/delivery-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryDetailsRepository deliveryDetailsRepository;

    @Autowired
    private DeliveryDetailsMapper deliveryDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryDetailsMockMvc;

    private DeliveryDetails deliveryDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryDetails createEntity(EntityManager em) {
        DeliveryDetails deliveryDetails = new DeliveryDetails()
            .deliveryId(DEFAULT_DELIVERY_ID)
            .customerType(DEFAULT_CUSTOMER_TYPE)
            .deliveryStatus(DEFAULT_DELIVERY_STATUS)
            .expectedDeliveryTime(DEFAULT_EXPECTED_DELIVERY_TIME)
            .currentDistanceFromDestinationInMeters(DEFAULT_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS)
            .timeToReachDestination(DEFAULT_TIME_TO_REACH_DESTINATION);
        return deliveryDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryDetails createUpdatedEntity(EntityManager em) {
        DeliveryDetails deliveryDetails = new DeliveryDetails()
            .deliveryId(UPDATED_DELIVERY_ID)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .deliveryStatus(UPDATED_DELIVERY_STATUS)
            .expectedDeliveryTime(UPDATED_EXPECTED_DELIVERY_TIME)
            .currentDistanceFromDestinationInMeters(UPDATED_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS)
            .timeToReachDestination(UPDATED_TIME_TO_REACH_DESTINATION);
        return deliveryDetails;
    }

    @BeforeEach
    public void initTest() {
        deliveryDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliveryDetails() throws Exception {
        int databaseSizeBeforeCreate = deliveryDetailsRepository.findAll().size();
        // Create the DeliveryDetails
        DeliveryDetailsDTO deliveryDetailsDTO = deliveryDetailsMapper.toDto(deliveryDetails);
        restDeliveryDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryDetails testDeliveryDetails = deliveryDetailsList.get(deliveryDetailsList.size() - 1);
        assertThat(testDeliveryDetails.getDeliveryId()).isEqualTo(DEFAULT_DELIVERY_ID);
        assertThat(testDeliveryDetails.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
        assertThat(testDeliveryDetails.getDeliveryStatus()).isEqualTo(DEFAULT_DELIVERY_STATUS);
        assertThat(testDeliveryDetails.getExpectedDeliveryTime()).isEqualTo(DEFAULT_EXPECTED_DELIVERY_TIME);
        assertThat(testDeliveryDetails.getCurrentDistanceFromDestinationInMeters())
            .isEqualTo(DEFAULT_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS);
        assertThat(testDeliveryDetails.getTimeToReachDestination()).isEqualTo(DEFAULT_TIME_TO_REACH_DESTINATION);
    }

    @Test
    @Transactional
    void createDeliveryDetailsWithExistingId() throws Exception {
        // Create the DeliveryDetails with an existing ID
        deliveryDetails.setId(1L);
        DeliveryDetailsDTO deliveryDetailsDTO = deliveryDetailsMapper.toDto(deliveryDetails);

        int databaseSizeBeforeCreate = deliveryDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeliveryDetails() throws Exception {
        // Initialize the database
        deliveryDetailsRepository.saveAndFlush(deliveryDetails);

        // Get all the deliveryDetailsList
        restDeliveryDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryId").value(hasItem(DEFAULT_DELIVERY_ID)))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE)))
            .andExpect(jsonPath("$.[*].deliveryStatus").value(hasItem(DEFAULT_DELIVERY_STATUS)))
            .andExpect(jsonPath("$.[*].expectedDeliveryTime").value(hasItem(DEFAULT_EXPECTED_DELIVERY_TIME.toString())))
            .andExpect(
                jsonPath("$.[*].currentDistanceFromDestinationInMeters")
                    .value(hasItem(DEFAULT_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS.toString()))
            )
            .andExpect(jsonPath("$.[*].timeToReachDestination").value(hasItem(DEFAULT_TIME_TO_REACH_DESTINATION.toString())));
    }

    @Test
    @Transactional
    void getDeliveryDetails() throws Exception {
        // Initialize the database
        deliveryDetailsRepository.saveAndFlush(deliveryDetails);

        // Get the deliveryDetails
        restDeliveryDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryDetails.getId().intValue()))
            .andExpect(jsonPath("$.deliveryId").value(DEFAULT_DELIVERY_ID))
            .andExpect(jsonPath("$.customerType").value(DEFAULT_CUSTOMER_TYPE))
            .andExpect(jsonPath("$.deliveryStatus").value(DEFAULT_DELIVERY_STATUS))
            .andExpect(jsonPath("$.expectedDeliveryTime").value(DEFAULT_EXPECTED_DELIVERY_TIME.toString()))
            .andExpect(
                jsonPath("$.currentDistanceFromDestinationInMeters").value(DEFAULT_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS.toString())
            )
            .andExpect(jsonPath("$.timeToReachDestination").value(DEFAULT_TIME_TO_REACH_DESTINATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryDetails() throws Exception {
        // Get the deliveryDetails
        restDeliveryDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliveryDetails() throws Exception {
        // Initialize the database
        deliveryDetailsRepository.saveAndFlush(deliveryDetails);

        int databaseSizeBeforeUpdate = deliveryDetailsRepository.findAll().size();

        // Update the deliveryDetails
        DeliveryDetails updatedDeliveryDetails = deliveryDetailsRepository.findById(deliveryDetails.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryDetails are not directly saved in db
        em.detach(updatedDeliveryDetails);
        updatedDeliveryDetails
            .deliveryId(UPDATED_DELIVERY_ID)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .deliveryStatus(UPDATED_DELIVERY_STATUS)
            .expectedDeliveryTime(UPDATED_EXPECTED_DELIVERY_TIME)
            .currentDistanceFromDestinationInMeters(UPDATED_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS)
            .timeToReachDestination(UPDATED_TIME_TO_REACH_DESTINATION);
        DeliveryDetailsDTO deliveryDetailsDTO = deliveryDetailsMapper.toDto(updatedDeliveryDetails);

        restDeliveryDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeUpdate);
        DeliveryDetails testDeliveryDetails = deliveryDetailsList.get(deliveryDetailsList.size() - 1);
        assertThat(testDeliveryDetails.getDeliveryId()).isEqualTo(UPDATED_DELIVERY_ID);
        assertThat(testDeliveryDetails.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testDeliveryDetails.getDeliveryStatus()).isEqualTo(UPDATED_DELIVERY_STATUS);
        assertThat(testDeliveryDetails.getExpectedDeliveryTime()).isEqualTo(UPDATED_EXPECTED_DELIVERY_TIME);
        assertThat(testDeliveryDetails.getCurrentDistanceFromDestinationInMeters())
            .isEqualTo(UPDATED_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS);
        assertThat(testDeliveryDetails.getTimeToReachDestination()).isEqualTo(UPDATED_TIME_TO_REACH_DESTINATION);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryDetails() throws Exception {
        int databaseSizeBeforeUpdate = deliveryDetailsRepository.findAll().size();
        deliveryDetails.setId(count.incrementAndGet());

        // Create the DeliveryDetails
        DeliveryDetailsDTO deliveryDetailsDTO = deliveryDetailsMapper.toDto(deliveryDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryDetails() throws Exception {
        int databaseSizeBeforeUpdate = deliveryDetailsRepository.findAll().size();
        deliveryDetails.setId(count.incrementAndGet());

        // Create the DeliveryDetails
        DeliveryDetailsDTO deliveryDetailsDTO = deliveryDetailsMapper.toDto(deliveryDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryDetails() throws Exception {
        int databaseSizeBeforeUpdate = deliveryDetailsRepository.findAll().size();
        deliveryDetails.setId(count.incrementAndGet());

        // Create the DeliveryDetails
        DeliveryDetailsDTO deliveryDetailsDTO = deliveryDetailsMapper.toDto(deliveryDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryDetailsWithPatch() throws Exception {
        // Initialize the database
        deliveryDetailsRepository.saveAndFlush(deliveryDetails);

        int databaseSizeBeforeUpdate = deliveryDetailsRepository.findAll().size();

        // Update the deliveryDetails using partial update
        DeliveryDetails partialUpdatedDeliveryDetails = new DeliveryDetails();
        partialUpdatedDeliveryDetails.setId(deliveryDetails.getId());

        partialUpdatedDeliveryDetails
            .customerType(UPDATED_CUSTOMER_TYPE)
            .expectedDeliveryTime(UPDATED_EXPECTED_DELIVERY_TIME)
            .currentDistanceFromDestinationInMeters(UPDATED_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS);

        restDeliveryDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryDetails))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeUpdate);
        DeliveryDetails testDeliveryDetails = deliveryDetailsList.get(deliveryDetailsList.size() - 1);
        assertThat(testDeliveryDetails.getDeliveryId()).isEqualTo(DEFAULT_DELIVERY_ID);
        assertThat(testDeliveryDetails.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testDeliveryDetails.getDeliveryStatus()).isEqualTo(DEFAULT_DELIVERY_STATUS);
        assertThat(testDeliveryDetails.getExpectedDeliveryTime()).isEqualTo(UPDATED_EXPECTED_DELIVERY_TIME);
        assertThat(testDeliveryDetails.getCurrentDistanceFromDestinationInMeters())
            .isEqualTo(UPDATED_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS);
        assertThat(testDeliveryDetails.getTimeToReachDestination()).isEqualTo(DEFAULT_TIME_TO_REACH_DESTINATION);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryDetailsWithPatch() throws Exception {
        // Initialize the database
        deliveryDetailsRepository.saveAndFlush(deliveryDetails);

        int databaseSizeBeforeUpdate = deliveryDetailsRepository.findAll().size();

        // Update the deliveryDetails using partial update
        DeliveryDetails partialUpdatedDeliveryDetails = new DeliveryDetails();
        partialUpdatedDeliveryDetails.setId(deliveryDetails.getId());

        partialUpdatedDeliveryDetails
            .deliveryId(UPDATED_DELIVERY_ID)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .deliveryStatus(UPDATED_DELIVERY_STATUS)
            .expectedDeliveryTime(UPDATED_EXPECTED_DELIVERY_TIME)
            .currentDistanceFromDestinationInMeters(UPDATED_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS)
            .timeToReachDestination(UPDATED_TIME_TO_REACH_DESTINATION);

        restDeliveryDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryDetails))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeUpdate);
        DeliveryDetails testDeliveryDetails = deliveryDetailsList.get(deliveryDetailsList.size() - 1);
        assertThat(testDeliveryDetails.getDeliveryId()).isEqualTo(UPDATED_DELIVERY_ID);
        assertThat(testDeliveryDetails.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testDeliveryDetails.getDeliveryStatus()).isEqualTo(UPDATED_DELIVERY_STATUS);
        assertThat(testDeliveryDetails.getExpectedDeliveryTime()).isEqualTo(UPDATED_EXPECTED_DELIVERY_TIME);
        assertThat(testDeliveryDetails.getCurrentDistanceFromDestinationInMeters())
            .isEqualTo(UPDATED_CURRENT_DISTANCE_FROM_DESTINATION_IN_METERS);
        assertThat(testDeliveryDetails.getTimeToReachDestination()).isEqualTo(UPDATED_TIME_TO_REACH_DESTINATION);
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryDetails() throws Exception {
        int databaseSizeBeforeUpdate = deliveryDetailsRepository.findAll().size();
        deliveryDetails.setId(count.incrementAndGet());

        // Create the DeliveryDetails
        DeliveryDetailsDTO deliveryDetailsDTO = deliveryDetailsMapper.toDto(deliveryDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryDetails() throws Exception {
        int databaseSizeBeforeUpdate = deliveryDetailsRepository.findAll().size();
        deliveryDetails.setId(count.incrementAndGet());

        // Create the DeliveryDetails
        DeliveryDetailsDTO deliveryDetailsDTO = deliveryDetailsMapper.toDto(deliveryDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryDetails() throws Exception {
        int databaseSizeBeforeUpdate = deliveryDetailsRepository.findAll().size();
        deliveryDetails.setId(count.incrementAndGet());

        // Create the DeliveryDetails
        DeliveryDetailsDTO deliveryDetailsDTO = deliveryDetailsMapper.toDto(deliveryDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryDetails in the database
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliveryDetails() throws Exception {
        // Initialize the database
        deliveryDetailsRepository.saveAndFlush(deliveryDetails);

        int databaseSizeBeforeDelete = deliveryDetailsRepository.findAll().size();

        // Delete the deliveryDetails
        restDeliveryDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryDetails> deliveryDetailsList = deliveryDetailsRepository.findAll();
        assertThat(deliveryDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
