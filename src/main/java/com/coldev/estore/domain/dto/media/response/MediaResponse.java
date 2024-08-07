package com.coldev.estore.domain.dto.media.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponse implements Serializable {

    private Long id;
    @JsonProperty("media_name")
    private String mediaKey;
    @JsonProperty("media_url")
    private String mediaUrl;
    @JsonProperty("media_type")
    private String mediaType;

}
