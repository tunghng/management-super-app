package com.im.sso.dto.mapper;

import com.im.sso.dto.model.AppComponentDto;
import com.im.sso.model.AppComponent;
import com.im.sso.model.AppComponent.AppComponentBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T15:39:11+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class AppComponentMapperImpl implements AppComponentMapper {

    @Override
    public AppComponent toModel(AppComponentDto componentDto) {
        if ( componentDto == null ) {
            return null;
        }

        AppComponentBuilder appComponent = AppComponent.builder();

        appComponent.name( componentDto.getName() );
        appComponent.description( componentDto.getDescription() );
        appComponent.urlBase( componentDto.getUrlBase() );

        return appComponent.build();
    }

    @Override
    public AppComponentDto toDto(AppComponent component) {
        if ( component == null ) {
            return null;
        }

        AppComponentDto appComponentDto = new AppComponentDto();

        appComponentDto.setId( component.getId() );
        appComponentDto.setName( component.getName() );
        appComponentDto.setDescription( component.getDescription() );
        appComponentDto.setUrlBase( component.getUrlBase() );

        return appComponentDto;
    }
}
