package com.soulmate.domain.attachFile;

import com.soulmate.domain.Pet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("P")
public class PetAttachFile extends AttachFile {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id")
    private Pet pet;

    @Builder
    public PetAttachFile(Long id, String original_name, String saved_name, String dir, Pet pet) {
        super(id, original_name, saved_name, dir);
        this.pet = pet;
    }
}
