package com.soulmate.domain.attachFile;

import com.soulmate.domain.Pet;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("P")
@SuperBuilder
public class PetAttachFile extends AttachFile {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public PetAttachFile(AttachFile file) {
        super(file.getId(), file.getSeq(), file.getOriginalName(), file.getSavedName(), file.getDir());
    }

    public void addPet(Pet pet) {
        pet.addFile(this);
        this.pet = pet;
    }
}
