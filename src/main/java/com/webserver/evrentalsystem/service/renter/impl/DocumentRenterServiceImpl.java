package com.webserver.evrentalsystem.service.renter.impl;

import com.webserver.evrentalsystem.entity.Document;
import com.webserver.evrentalsystem.entity.DocumentType;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.DataNotFoundException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.mapping.DocumentMapper;
import com.webserver.evrentalsystem.repository.DocumentRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.renter.DocumentRenterService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DocumentRenterServiceImpl implements DocumentRenterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private DocumentMapper documentMapper;

    @Override
    public DocumentDto uploadDocument(String type, String documentNumber, MultipartFile file) {
        User user = userValidation.validateUser();

        // Thư mục uploads ngay trong project root
        Path uploadDir = Paths.get("uploads");
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Sinh tên file duy nhất
            String originalFilename = file.getOriginalFilename();
            String filename = UUID.randomUUID() + "_" + (originalFilename != null ? originalFilename : "file");
            Path filePath = uploadDir.resolve(filename);

            // Lưu file xuống server
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // URL để client truy cập
            String fileUrl = "/uploads/" + filename;

            // Tạo Document entity
            Document doc = new Document();
            doc.setUser(user);
            doc.setType(DocumentType.valueOf(type));
            doc.setDocumentNumber(documentNumber);
            doc.setDocumentUrl(fileUrl);
            doc.setVerified(false);
            doc.setCreatedAt(LocalDateTime.now());

            Document saved = documentRepository.save(doc);
            return documentMapper.toDocumentDto(saved);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file", e);
        }
    }


    @Override
    public List<DocumentDto> getDocuments() {
        User user = userValidation.validateUser();
        return documentRepository.findByUserId(user.getId()).stream()
                .map(documentMapper::toDocumentDto)
                .toList();
    }

    @Override
    public void deleteDocument(Long id) {
        User user = userValidation.validateUser();
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài liệu với ID: " + id));
        if (!doc.getUser().getId().equals(user.getId())) {
            throw new PermissionDeniedException("Bạn không có quyền xoá tài liệu này");
        }
        documentRepository.delete(doc);
    }
}
