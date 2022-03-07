package com.callsign.deliveryapi.service.mapper;

import com.callsign.deliveryapi.domain.DeliveryDetails;
import com.callsign.deliveryapi.service.dto.DeliveryDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryDetails} and its DTO {@link DeliveryDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeliveryDetailsMapper extends EntityMapper<DeliveryDetailsDTO, DeliveryDetails> {}
