package org.springframework.samples.petclinic.rest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;
import org.springframework.samples.petclinic.rest.controller.v2.OwnerRestControllerV2;
import org.springframework.samples.petclinic.rest.controller.v2.PetRestControllerV2;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class V2RestControllersTests {
    @Autowired
    private OwnerRestControllerV2 ownerRestControllerV2;

    @Autowired
    private PetRestControllerV2 petRestControllerV2;

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private PetMapper petMapper;

    @MockitoBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<OwnerDto> owners;

    private List<PetDto> pets;

    @BeforeEach
    void initOwners() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownerRestControllerV2, petRestControllerV2)
            .setControllerAdvice(new ExceptionControllerAdvice())
            .build();
        owners = new ArrayList<>();

        owners.add(new OwnerDto("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023", 1, null));
        owners.add(new OwnerDto("Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749", 2, null));
        owners.add(new OwnerDto("Eduardo", "Rodriquez", "2693 Commerce St.", "McFarland", "6085558763", 3, null));
        owners.add(new OwnerDto("Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198", 4, null));

        PetTypeDto petType = new PetTypeDto("dog", 2);

        pets = new ArrayList<>();
        pets.add(new PetDto("Rosy", LocalDate.now(), petType, 3, null, null));
        pets.add(new PetDto("Jewel", LocalDate.now(), petType, 4, null, null));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnersPageSuccess() throws Exception {
        var pageRequest = PageRequest.of(0, 2, Sort.by("id"));
        var pageOwners = ownerMapper.toOwners(owners.subList(0, 2)).stream().toList();
        given(this.clinicService.findOwners(null, pageRequest))
            .willReturn(new PageImpl<>(pageOwners, pageRequest, owners.size()));
        this.mockMvc.perform(get("/api/v2/owners?page=0&size=2")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].firstName").value("George"))
            .andExpect(jsonPath("$.content[1].id").value(2))
            .andExpect(jsonPath("$.content[1].firstName").value("Betty"))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(2))
            .andExpect(jsonPath("$.totalElements").value(4))
            .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetPetsPageSuccess() throws Exception {
        var pageRequest = PageRequest.of(0, 5, Sort.by("id"));
        var pagePets = petMapper.toPets(pets).stream().toList();
        given(this.clinicService.findPets(pageRequest))
            .willReturn(new PageImpl<>(pagePets, pageRequest, pets.size()));
        this.mockMvc.perform(get("/api/v2/pets?page=0&size=5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.content[0].id").value(3))
            .andExpect(jsonPath("$.content[0].name").value("Rosy"))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(5))
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(jsonPath("$.totalPages").value(1));
    }
}
