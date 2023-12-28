package ru.example.ivtserver.entities.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.Partner;
import ru.example.ivtserver.utils.ImagePathConstant;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Value
public class PartnerDto {

    UUID id;

    String href;

    String urlImg;

    public static PartnerDto of(Partner partner) {
        return PartnerDto.builder()
                .id(partner.getId())
                .href(partner.getHref())
                .urlImg(ImagePathConstant.BASE_PARTNER_PATH.concat("/").concat(partner.getImgName()))
                .build();
    }
}
