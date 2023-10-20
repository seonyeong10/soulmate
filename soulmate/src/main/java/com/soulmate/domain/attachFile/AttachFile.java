package com.soulmate.domain.attachFile;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attach_file_id")
    private Long id;
    private String original_name;
    private String saved_name;
    private String dir;

    public AttachFile(Long id, String original_name, String saved_name, String dir) {
        this.id = id;
        this.original_name = original_name;
        this.saved_name = saved_name;
        this.dir = dir;
    }
}
