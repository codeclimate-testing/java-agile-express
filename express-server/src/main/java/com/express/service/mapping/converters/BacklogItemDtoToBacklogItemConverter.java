package com.express.service.mapping.converters;

import com.express.dao.BacklogItemDao;
import com.express.domain.BacklogItem;
import com.express.service.dto.BacklogItemDto;
import com.googlecode.simpleobjectassembler.converter.AbstractObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BacklogItemDtoToBacklogItemConverter extends AbstractObjectConverter<BacklogItemDto, BacklogItem> {

   private final BacklogItemDao backlogItemDao;

   @Autowired
   public BacklogItemDtoToBacklogItemConverter(BacklogItemDao backlogItemDao) {
      this.backlogItemDao = backlogItemDao;
   }

   @Override
   public BacklogItem createDestinationObject(BacklogItemDto dto) {
      return dto.getId() == null ? new BacklogItem() : backlogItemDao.findById(dto.getId());
   }
}
