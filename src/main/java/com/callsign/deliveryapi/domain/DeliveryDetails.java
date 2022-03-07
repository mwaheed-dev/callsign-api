package com.callsign.deliveryapi.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeliveryDetails.
 */
@Entity
@Table(name = "delivery_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeliveryDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "delivery_id")
    private Integer deliveryId;

    @Column(name = "customer_type")
    private String customerType;

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @Column(name = "expected_delivery_time")
    private Instant expectedDeliveryTime;

    @Column(name = "current_distance_from_destination_in_meters")
    private Instant currentDistanceFromDestinationInMeters;

    @Column(name = "time_to_reach_destination")
    private Instant timeToReachDestination;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeliveryDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeliveryId() {
        return this.deliveryId;
    }

    public DeliveryDetails deliveryId(Integer deliveryId) {
        this.setDeliveryId(deliveryId);
        return this;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getCustomerType() {
        return this.customerType;
    }

    public DeliveryDetails customerType(String customerType) {
        this.setCustomerType(customerType);
        return this;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getDeliveryStatus() {
        return this.deliveryStatus;
    }

    public DeliveryDetails deliveryStatus(String deliveryStatus) {
        this.setDeliveryStatus(deliveryStatus);
        return this;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Instant getExpectedDeliveryTime() {
        return this.expectedDeliveryTime;
    }

    public DeliveryDetails expectedDeliveryTime(Instant expectedDeliveryTime) {
        this.setExpectedDeliveryTime(expectedDeliveryTime);
        return this;
    }

    public void setExpectedDeliveryTime(Instant expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public Instant getCurrentDistanceFromDestinationInMeters() {
        return this.currentDistanceFromDestinationInMeters;
    }

    public DeliveryDetails currentDistanceFromDestinationInMeters(Instant currentDistanceFromDestinationInMeters) {
        this.setCurrentDistanceFromDestinationInMeters(currentDistanceFromDestinationInMeters);
        return this;
    }

    public void setCurrentDistanceFromDestinationInMeters(Instant currentDistanceFromDestinationInMeters) {
        this.currentDistanceFromDestinationInMeters = currentDistanceFromDestinationInMeters;
    }

    public Instant getTimeToReachDestination() {
        return this.timeToReachDestination;
    }

    public DeliveryDetails timeToReachDestination(Instant timeToReachDestination) {
        this.setTimeToReachDestination(timeToReachDestination);
        return this;
    }

    public void setTimeToReachDestination(Instant timeToReachDestination) {
        this.timeToReachDestination = timeToReachDestination;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryDetails)) {
            return false;
        }
        return id != null && id.equals(((DeliveryDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryDetails{" +
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
