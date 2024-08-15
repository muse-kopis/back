package muse_kopis.muse.heart;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.common.HeartDuplicatedException;
import muse_kopis.muse.common.NotFoundHeartException;
import muse_kopis.muse.common.NotFoundMemberException;
import muse_kopis.muse.common.NotFoundPerformanceException;
import muse_kopis.muse.member.Member;
import muse_kopis.muse.member.MemberRepository;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.PerformanceRepository;
import muse_kopis.muse.performance.dto.PerformanceResponse;
import org.springframework.stereotype.Service;

@Service
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final PerformanceRepository performanceRepository;
    private final OauthMemberRepository oauthMemberRepository;

    public HeartService(HeartRepository heartRepository, MemberRepository memberRepository,
                        PerformanceRepository performanceRepository, OauthMemberRepository oauthMemberRepository) {
        this.heartRepository = heartRepository;
        this.memberRepository = memberRepository;
        this.performanceRepository = performanceRepository;
        this.oauthMemberRepository = oauthMemberRepository;
    }

    @Transactional
    public void like(Long memberId, String performanceName, String venue) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new NotFoundMemberException("찾을 수 없는 회원입니다."));
        OauthMember oauthMember = oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundHeartException("찾을 수 없는 회원입니다."));
        Performance performance = performanceRepository.findByPerformanceNameAndVenue(performanceName, venue)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
        if (heartRepository.findByOauthMemberAndPerformance(oauthMember, performance).isPresent()) {
            throw new HeartDuplicatedException("이미 좋아요를 눌렀습니다.");
        }
        heartRepository.save(new Heart(oauthMember, performance));
    }

    @Transactional
    public void unlike(Long memberId, String performanceName, String venue) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new NotFoundMemberException("찾을 수 없는 회원입니다."));
        OauthMember oauthMember = oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundHeartException("찾을 수 없는 회원입니다."));
        Performance performance = performanceRepository.findByPerformanceNameAndVenue(performanceName, venue)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
        Heart heart = heartRepository.findByOauthMemberAndPerformance(oauthMember, performance)
                .orElseThrow(() -> new NotFoundHeartException("좋아요 기록이 없습니다."));
        heartRepository.delete(heart);
    }

    public List<PerformanceResponse> getMembersLikePerformanceList(Long memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new NotFoundMemberException("찾을 수 없는 회원입니다."));
        OauthMember oauthMember = oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundHeartException("찾을 수 없는 회원입니다."));
        List<Heart> hearts = heartRepository.findByOauthMember(oauthMember);
        return hearts.stream()
                .map(it -> PerformanceResponse.from(it.getPerformance()))
                .collect(Collectors.toList());
    }
}
