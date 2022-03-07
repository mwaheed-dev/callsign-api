package com.callsign.deliveryapi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryDetailsMapperTest {

    private DeliveryDetailsMapper deliveryDetailsMapper;

    @BeforeEach
    public void setUp() {
        deliveryDetailsMapper = new DeliveryDetailsMapperImpl();
    }
}
