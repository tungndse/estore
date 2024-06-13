package com.coldev.estore.domain.dto.media.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponse {

    private String mediaKeys;
    private String mediaUrls;
    private String mediaType;

}
