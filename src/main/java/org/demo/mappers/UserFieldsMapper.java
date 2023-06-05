package org.demo.mappers;

import org.demo.defaultpackage.shelterservice.model.DonateDto;
import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.demo.entity.DonateEntity;
import org.demo.entity.GroupEntity;
import org.demo.entity.PetEntity;
import org.demo.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = GroupEntity.class)

public interface UserFieldsMapper {

    UserFieldsMapper MAPPER = Mappers.getMapper(UserFieldsMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "lastModifiedTimestamp", ignore = true)
    @Mapping(target = "groups", expression = "java(new GroupEntity(dtoEnumToEntity(userDto.getGroup())))")
    UserEntity userDtoToEntity(UserDto userDto);

    GroupEntity.GroupEnum dtoEnumToEntity(UserDto.GroupEnum userGroupEnum);

    UserDto.GroupEnum entityEnumToDto(GroupEntity.GroupEnum entityGroupEnum);

    @InheritInverseConfiguration
    @Mapping(target = "group", ignore = true)
    UserDto userEntityToDto(UserEntity users);

    @Mapping(target = "donate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "lastModifiedTimestamp", ignore = true)
    @Mapping(target = "booked", ignore = true)
    PetEntity petDtoToEntity(PetDto petDto);

    @InheritInverseConfiguration
    PetDto petEntityToDto(PetEntity pets);

    @Mapping(target = "petId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "lastModifiedTimestamp", ignore = true)
    @Mapping(target = "userId", ignore = true)
    DonateEntity donateDtoToEntity(DonateDto donateDto);

    @InheritInverseConfiguration
    DonateDto donateEntityToDto(DonateEntity donateEntity);

}
