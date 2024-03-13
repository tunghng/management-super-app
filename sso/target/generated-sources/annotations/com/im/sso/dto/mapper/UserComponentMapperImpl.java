package com.im.sso.dto.mapper;

import com.im.sso.dto.model.UserComponentDto;
import com.im.sso.model.UserComponent;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T15:39:12+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class UserComponentMapperImpl implements UserComponentMapper {

    @Override
    public UserComponentDto toDto(UserComponent userComponent) {
        if ( userComponent == null ) {
            return null;
        }

        UserComponentDto userComponentDto = new UserComponentDto();

        userComponentDto.setUserId( appUserToUserId( userComponent ) );
        userComponentDto.setComponentName( componentToComponentName( userComponent ) );
        List<String> list = userComponent.getPermissions();
        if ( list != null ) {
            userComponentDto.setPermissions( new ArrayList<String>( list ) );
        }

        return userComponentDto;
    }
}
