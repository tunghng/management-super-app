package com.im.support.dto.mapper;

import com.im.support.dto.model.LogDto;
import com.im.support.dto.model.LogDto.LogDtoBuilder;
import com.im.support.model.Log;
import com.im.support.service.UserService;
import com.im.support.util.CommonUtils;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T16:12:37+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class LogMapperImpl implements LogMapper {

    @Autowired
    private UserService userService;
    @Autowired
    private CommonUtils commonUtils;

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
        logDto.createdBy( userService.findByUserId( entity.getCreatedBy() ) );
        logDto.actionData( commonUtils.toJsonNode( entity.getActionData() ) );
        logDto.actionStatus( entity.getActionStatus() );
        logDto.actionType( entity.getActionType() );
        logDto.actionFailureDetails( entity.getActionFailureDetails() );
        logDto.createdAt( entity.getCreatedAt() );

        return logDto.build();
    }
}
