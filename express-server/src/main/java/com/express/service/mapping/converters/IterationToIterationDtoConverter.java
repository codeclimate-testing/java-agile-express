package com.express.service.mapping.converters;

import com.express.domain.Iteration;
import com.express.service.dto.IterationDto;
import com.googlecode.simpleobjectassembler.converter.AbstractObjectConverter;

import java.util.Collections;

public class IterationToIterationDtoConverter extends AbstractObjectConverter<Iteration, IterationDto> {
   @Override
   public void convert(Iteration iteration, IterationDto iterationDto) {
      Collections.sort(iterationDto.getHistory());
      Collections.sort(iterationDto.getBacklog());
   }
}
