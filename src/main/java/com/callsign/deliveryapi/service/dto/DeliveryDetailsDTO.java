package com.callsign.deliveryapi.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.callsign.deliveryapi.domain.DeliveryDetails} entity.
 */
public class DeliveryDetailsDTO implements Serializable {

    private Long id;

    private Integer deliveryId;

    private String customerType;

    private String deliveryStatus;

    private Instant expectedDeliveryTime;

    private Instant currentDistanceFromDestinationInMeters;

    private Instant timeToReachDestination;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Instant getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(Instant expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public Instant getCurrentDistanceFromDestinationInMeters() {
        return currentDistanceFromDestinationInMeters;
    }

    public void setCurrentDistanceFromDestinationInMeters(Instant currentDistanceFromDestinationInMeters) {
        this.currentDistanceFromDestinationInMeters = currentDistanceFromDestinationInMeters;
    }

    public Instant getTimeToReachDestination() {
        return timeToReachDestination;
    }

    public void setTimeToReachDestination(Instant timeToReachDestination) {
        this.timeToReachDestination = timeToReachDestination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryDetailsDTO)) {
            return false;
        }

        DeliveryDetailsDTO deliveryDetailsDTO = (DeliveryDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryDetailsDTO{" +
            "id=" + getId() +
            ", deliveryId=" + getDeliveryId() +
            ", customerType='" + getCustomerType() + "'" +
            ", deliveryStatus='" + getDeliveryStatus() + "'" +
            ", expectedDeliveryTime='" + getExpectedDeliveryTime() + "'" +
            ", currentDistanceFromDestinationInMeters='" + getCurrentDistanceFromDestinationInMeters() + "'" +
            ", timeToReachDestination='" + getTimeToReachDestination() + "'" +
            "}";
    }
}
