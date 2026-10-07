package org.springframework.samples.petclinic.mapper;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.controller.v1.dto.VetDto;
import org.springframework.stereotype.Component;

/**
 * Map Vet Entity to VetDto.
 */
@Component
public class VetMapper {

    public VetDto toVetDto(Vet vet) {
        VetDto vetDto = new VetDto();
        BeanUtils.copyProperties(vet, vetDto);
        // New field: email
        vetDto.setEmail(vet.getEmail());
        return vetDto;
    }

    public Collection<VetDto> toVetDtoCollection(Collection<Vet> vets) {
        return vets.stream().map(this::toVetDto).collect(Collectors.toList());
    }

}
