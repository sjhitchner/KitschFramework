package com.stephenhitchner.common.exceptions;

public enum PartyAtFault {

    /**
     * Indicates the caller/user of an application/service is at fault.
     */
    CLIENT(1),

    /**
     * Indicates a dependency of the application/service is at fault
     */
    DEPENDENCY(0),

    /**
     * Indicates the application itself is at fault
     */
    SELF(0)

    ;

    private final int availabilityScore;

    private PartyAtFault(final int availabilityScore) {
        this.availabilityScore = availabilityScore;
    }

    public final int getAvailabilityScore() {
        return availabilityScore;
    }

}
