package com.soulmate.web;

import com.soulmate.domain.*;
import com.soulmate.domain.attachFile.AttachFile;
import com.soulmate.domain.attachFile.PetAttachFile;
import com.soulmate.domain.repository.AttachFileRepository;
import com.soulmate.domain.repository.DesignerRepository;
import com.soulmate.domain.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MainControllerTest {
    @Autowired
    private AttachFileRepository fileRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private DesignerRepository designerRepository;

    @Test
    public void test () throws Exception {
        //given

        //when
        AttachFile file = AttachFile.builder().originalName("org").build();
        PetAttachFile petAttachFile = file.toPetFile();

        System.out.println(petAttachFile.getOriginalName());

        //then
        System.out.println("fin~");
    }
}