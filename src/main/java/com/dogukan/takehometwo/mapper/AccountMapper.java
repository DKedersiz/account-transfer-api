package com.dogukan.takehometwo.mapper;

import com.dogukan.takehometwo.dto.request.RequestCreateAccount;
import com.dogukan.takehometwo.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity (RequestCreateAccount requestCreateAccount);

    RequestCreateAccount toDTO (Account account);
}
