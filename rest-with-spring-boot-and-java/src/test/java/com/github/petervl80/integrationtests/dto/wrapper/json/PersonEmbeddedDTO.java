package com.github.petervl80.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.petervl80.integrationtests.dto.PersonDTO;

import java.io.Serializable;
import java.util.List;

public class PersonEmbeddedDTO implements Serializable {

    @JsonProperty("people")
    private List<PersonDTO> people;

    public PersonEmbeddedDTO() {
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(List<PersonDTO> people) {
        this.people = people;
    }
}
