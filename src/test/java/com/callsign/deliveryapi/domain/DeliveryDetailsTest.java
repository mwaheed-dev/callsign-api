package com.callsign.deliveryapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.callsign.deliveryapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryDetails.class);
        DeliveryDetails deliveryDetails1 = new DeliveryDetails();
        deliveryDetails1.setId(1L);
        DeliveryDetails deliveryDetails2 = new DeliveryDetails();
        deliveryDetails2.setId(deliveryDetails1.getId());
        assertThat(deliveryDetails1).isEqualTo(deliveryDetails2);
        deliveryDetails2.setId(2L);
        assertThat(deliveryDetails1).isNotEqualTo(deliveryDetails2);
        deliveryDetails1.setId(null);
        assertThat(deliveryDetails1).isNotEqualTo(deliveryDetails2);
    }
}
