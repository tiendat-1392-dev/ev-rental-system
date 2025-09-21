package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Document;
import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DocumentMapper {
    DocumentDto toDocumentDto(Document document);
}
