package com.soulmate.web;

import com.soulmate.domain.*;
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

        //then
        System.out.println("fin~");
    }
}