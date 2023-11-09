package com.soulmate.web;

import com.soulmate.config.auth.LoginUser;
import com.soulmate.config.auth.dto.SessionUser;
import com.soulmate.service.PetService;
import com.soulmate.web.dto.response.PetResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my/pets")
public class PetController {

    private final PetService petService;

    @GetMapping("")
    public String findAllMyPets(
            @LoginUser SessionUser user,
            Model model
    ) {
        model.addAttribute("pets", petService.findAll(user));
        return "my/MyPetList";
    }

    @GetMapping("/edit")
    public String registerPet() {
        return "my/MyPetEdit";
    }

    @GetMapping("/{petId}")
    public String findOne(
            @PathVariable(name = "petId") Long petId,
            @LoginUser SessionUser user,
            Model model
    ) {
        try {
            PetResDto find = petService.findOne(petId, user);
            model.addAttribute("pet", find);
            model.addAttribute("isModify", false);
        } catch (NoSuchElementException e) {
            model.addAttribute("error", "반려동물을 찾을 수 없습니다.");
        }

        return "my/MyPetModify";
    }
}
