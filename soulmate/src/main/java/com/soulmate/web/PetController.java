package com.soulmate.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PetController {

    @GetMapping("/my/pets")
    public String findAllMyPets() {
        return "my/MyPetList";
    }

    @GetMapping("/my/pets/edit")
    public String registerPet() {
        return "my/MyPetEdit";
    }

}
