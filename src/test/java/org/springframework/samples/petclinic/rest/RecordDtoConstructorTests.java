package org.springframework.samples.petclinic.rest;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto;
import org.springframework.samples.petclinic.rest.dto.ProblemDetailDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordDtoConstructorTests {

    @Test
    void rejectsStringsAboveSchemaMaximumLength() {
        assertThrows(IllegalArgumentException.class,
            () -> new OwnerFieldsDto("A".repeat(31), "Franklin", "Address", "City", "123"));
    }

    @Test
    void rejectsExclusiveNumericUpperBound() {
        assertThrows(IllegalArgumentException.class,
            () -> new ProblemDetailDto(null, null, 600, null, null, List.of()));
    }
}
