package com.soulmate.domain;

import com.soulmate.domain.attachFile.DesignerAttachFile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Designer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "designer_id")
    private Long id;
    private String empNo;
    private String password;
    private String name;
    private String mobile;
    private LocalDateTime enterDate;
    private LocalDateTime leaveDate;
    private String status;
    @OneToMany(mappedBy = "designer")
    private List<DesignerAttachFile> files = new ArrayList<>();

    @Builder
    public Designer(Long id, String empNo, String password, String name, String mobile, LocalDateTime enterDate, LocalDateTime leaveDate, String status, List<DesignerAttachFile> files) {
        this.id = id;
        this.empNo = empNo;
        this.password = password;
        this.name = name;
        this.mobile = mobile;
        this.enterDate = enterDate;
        this.leaveDate = leaveDate;
        this.status = status;
        this.files = files;
    }
}
