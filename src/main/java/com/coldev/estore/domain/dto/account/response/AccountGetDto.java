package com.coldev.estore.domain.dto.account.response;

import com.coldev.estore.common.enumerate.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountGetDto implements Serializable {

    private String username;
    private String name;
    private String description;
    private String address;
    private String email;
    private String phone;
    private Status status;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("avatar_url")
    private String mediaUrl;


}
