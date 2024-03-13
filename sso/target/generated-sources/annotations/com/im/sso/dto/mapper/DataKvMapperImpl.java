package com.im.sso.dto.mapper;

import com.im.sso.dto.model.DataKvDto;
import com.im.sso.model.DataKv;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T15:39:11+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class DataKvMapperImpl implements DataKvMapper {

    @Override
    public DataKv toModel(DataKvDto dataKvDto) {
        if ( dataKvDto == null ) {
            return null;
        }

        DataKv dataKv = new DataKv();

        dataKv.setKey( dataKvDto.getKey() );
        dataKv.setValue( dataKvDto.getValue() );
        dataKv.setCreatedAt( dataKvDto.getCreatedAt() );
        dataKv.setUpdatedAt( dataKvDto.getUpdatedAt() );

        return dataKv;
    }

    @Override
    public List<DataKvDto> toDtoList(List<DataKv> dataKvList) {
        if ( dataKvList == null ) {
            return null;
        }

        List<DataKvDto> list = new ArrayList<DataKvDto>( dataKvList.size() );
        for ( DataKv dataKv : dataKvList ) {
            list.add( dataKvToDataKvDto( dataKv ) );
        }

        return list;
    }

    protected DataKvDto dataKvToDataKvDto(DataKv dataKv) {
        if ( dataKv == null ) {
            return null;
        }

        DataKvDto dataKvDto = new DataKvDto();

        dataKvDto.setKey( dataKv.getKey() );
        dataKvDto.setValue( dataKv.getValue() );
        dataKvDto.setCreatedAt( dataKv.getCreatedAt() );
        dataKvDto.setUpdatedAt( dataKv.getUpdatedAt() );

        return dataKvDto;
    }
}
