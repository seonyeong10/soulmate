package com.soulmate.domain.attachFile;

import com.soulmate.domain.Designer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("D")
public class DesignerAttachFile extends AttachFile {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id")
    private Designer designer;

    @Builder
    public DesignerAttachFile(Long id, String original_name, String saved_name, String dir, Designer designer) {
        super(id, original_name, saved_name, dir);
        this.designer = designer;
    }
}
