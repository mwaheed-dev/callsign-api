package com.callsign.deliveryapi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.callsign.deliveryapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryDetailsDTO.class);
        DeliveryDetailsDTO deliveryDetailsDTO1 = new DeliveryDetailsDTO();
        deliveryDetailsDTO1.setId(1L);
        DeliveryDetailsDTO deliveryDetailsDTO2 = new DeliveryDetailsDTO();
        assertThat(deliveryDetailsDTO1).isNotEqualTo(deliveryDetailsDTO2);
        deliveryDetailsDTO2.setId(deliveryDetailsDTO1.getId());
        assertThat(deliveryDetailsDTO1).isEqualTo(deliveryDetailsDTO2);
        deliveryDetailsDTO2.setId(2L);
        assertThat(deliveryDetailsDTO1).isNotEqualTo(deliveryDetailsDTO2);
        deliveryDetailsDTO1.setId(null);
        assertThat(deliveryDetailsDTO1).isNotEqualTo(deliveryDetailsDTO2);
    }
}
