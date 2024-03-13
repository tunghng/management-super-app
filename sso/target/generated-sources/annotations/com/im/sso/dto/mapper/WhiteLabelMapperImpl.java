package com.im.sso.dto.mapper;

import com.im.sso.dto.model.WhiteLabelDto;
import com.im.sso.model.WhiteLabel;
import com.im.sso.model.WhiteLabel.WhiteLabelBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T15:39:12+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class WhiteLabelMapperImpl implements WhiteLabelMapper {

    @Override
    public WhiteLabel toModel(WhiteLabelDto tenantWhiteLabelDto) {
        if ( tenantWhiteLabelDto == null ) {
            return null;
        }

        WhiteLabelBuilder whiteLabel = WhiteLabel.builder();

        whiteLabel.logoImage( tenantWhiteLabelDto.getLogoImage() );
        whiteLabel.appTitle( tenantWhiteLabelDto.getAppTitle() );

        return whiteLabel.build();
    }

    @Override
    public WhiteLabelDto toDto(WhiteLabel whiteLabel) {
        if ( whiteLabel == null ) {
            return null;
        }

        WhiteLabelDto whiteLabelDto = new WhiteLabelDto();

        whiteLabelDto.setId( whiteLabel.getId() );
        whiteLabelDto.setLogoImage( whiteLabel.getLogoImage() );
        whiteLabelDto.setAppTitle( whiteLabel.getAppTitle() );
        whiteLabelDto.setCreatedAt( whiteLabel.getCreatedAt() );
        whiteLabelDto.setUpdatedAt( whiteLabel.getUpdatedAt() );

        return whiteLabelDto;
    }
}
