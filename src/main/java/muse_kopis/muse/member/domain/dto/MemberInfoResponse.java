package muse_kopis.muse.member.domain.dto;

import java.time.LocalDate;
import lombok.Builder;
import muse_kopis.muse.member.domain.Address;
import muse_kopis.muse.member.domain.MemberInfo;
import muse_kopis.muse.member.domain.Sex;
import muse_kopis.muse.usergenre.domain.dto.UserGenreResponse;

@Builder
public record MemberInfoResponse(
        LocalDate birth,
        Sex sex,
        Address address,
        UserGenreResponse userGenre
) {
    public static MemberInfoResponse from(MemberInfo memberInfo) {
        return MemberInfoResponse.builder()
                .birth(memberInfo.birth())
                .sex(memberInfo.sex())
                .userGenre(UserGenreResponse.from(memberInfo.userGenre()))
                .address(memberInfo.address())
                .build();
    }
}
