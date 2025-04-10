package com.notnoop.exceptions;

import com.notnoop.apns.DeliveryError;

public class ApnsDeliveryErrorException
extends ApnsException
{
private final DeliveryError deliveryError;

public ApnsDeliveryErrorException(DeliveryError error) {
this.deliveryError = error;
}

public String getMessage() {
return "Failed to deliver notification with error code " + this.deliveryError.code();
}

public DeliveryError getDeliveryError() {
return this.deliveryError;
}
}

