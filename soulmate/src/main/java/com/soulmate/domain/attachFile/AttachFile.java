package com.soulmate.domain.attachFile;

import com.soulmate.domain.Designer;
import com.soulmate.domain.Pet;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class AttachFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attach_file_id")
    private Long id;

    private int seq;

    private String originalName;

    private String savedName;

    private String dir;

    public PetAttachFile toPetFile() {
        return new PetAttachFile(this);
    }
}
