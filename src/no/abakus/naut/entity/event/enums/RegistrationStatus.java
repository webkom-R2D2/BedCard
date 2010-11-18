package no.abakus.naut.entity.event.enums;

import java.io.Serializable;

public enum RegistrationStatus implements Serializable {
	ADMITTED, 
	PRIMARY_QUEUE, 
	SECONDARY_QUEUE, 
	NOT_REGISTERED;
}
