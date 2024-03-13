package com.im.sso.dto.mapper;

import com.im.sso.dto.model.DataKvDto;
import com.im.sso.model.DataKv;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DataKvMapper {

    DataKv toModel(DataKvDto dataKvDto);

    List<DataKvDto> toDtoList(List<DataKv> dataKvList);
}
