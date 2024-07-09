package com.coldev.estore.domain.dto.account.request;

import com.coldev.estore.common.enumerate.Status;
import com.coldev.estore.domain.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AccountFilerRequest extends FilterRequest {

    String username;
    String email;
    String phone;
    Status status;

}
