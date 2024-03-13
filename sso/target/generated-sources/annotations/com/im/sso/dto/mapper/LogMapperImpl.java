package com.im.sso.dto.mapper;

import com.im.sso.dto.model.LogDto;
import com.im.sso.dto.model.LogDto.LogDtoBuilder;
import com.im.sso.model.Log;
import com.im.sso.service.UserMapperService;
import com.im.sso.util.CommonUtils;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T15:39:11+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class LogMapperImpl implements LogMapper {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private UserMapperService userMapperService;

    @Override
    public LogDto toDto(Log entity) {
        if ( entity == null ) {
            return null;
        }

        LogDtoBuilder logDto = LogDto.builder();

        logDto.id( entity.getId() );
        logDto.tenantId( entity.getTenantId() );
        logDto.entityId( entity.getEntityId() );
        logDto.entityType( entity.getEntityType() );
        logDto.createdBy( userMapperService.findById( entity.getCreatedBy() ) );
        logDto.actionData( commonUtils.toJsonNode( entity.getActionData() ) );
        logDto.actionStatus( entity.getActionStatus() );
        logDto.actionType( entity.getActionType() );
        logDto.actionFailureDetails( entity.getActionFailureDetails() );
        logDto.createdAt( entity.getCreatedAt() );

        return logDto.build();
    }
}
