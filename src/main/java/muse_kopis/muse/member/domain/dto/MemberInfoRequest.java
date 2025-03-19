package muse_kopis.muse.member.domain.dto;

import java.time.LocalDate;
import muse_kopis.muse.member.domain.Address;
import muse_kopis.muse.member.domain.Sex;

public record MemberInfoRequest(
        Address address,
        LocalDate birth,
        Sex sex
) {
}
