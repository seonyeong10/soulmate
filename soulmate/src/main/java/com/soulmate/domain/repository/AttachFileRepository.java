package com.soulmate.domain.repository;

import com.soulmate.domain.attachFile.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {
}
