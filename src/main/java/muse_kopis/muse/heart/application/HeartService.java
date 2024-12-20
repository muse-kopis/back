package muse_kopis.muse.heart.application;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.common.like.HeartDuplicatedException;
import muse_kopis.muse.common.like.NotFoundHeartException;
import muse_kopis.muse.heart.domain.Heart;
import muse_kopis.muse.heart.domain.HeartRepository;
import muse_kopis.muse.performance.domain.Performance;
import muse_kopis.muse.performance.domain.PerformanceRepository;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import muse_kopis.muse.ticketbook.domain.dto.UserGenreEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final PerformanceRepository performanceRepository;
    private final OauthMemberRepository oauthMemberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void like(Long memberId, Long performanceId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        if (heartRepository.existsByOauthMemberAndPerformance(oauthMember, performance)) {
            throw new HeartDuplicatedException("이미 좋아요를 눌렀습니다.");
        }
        eventPublisher.publishEvent(new UserGenreEvent(memberId, performanceId));
        heartRepository.save(new Heart(oauthMember, performance));
    }

    @Transactional
    public void unlike(Long memberId, Long performanceId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        Heart heart = heartRepository.findByOauthMemberAndPerformance(oauthMember, performance)
                .orElseThrow(() -> new NotFoundHeartException("좋아요 기록이 없습니다."));
        heartRepository.delete(heart);
    }

    @Transactional
    public List<PerformanceResponse> getMembersLikePerformanceList(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundHeartException("찾을 수 없는 회원입니다."));
        List<Heart> hearts = heartRepository.findByOauthMember(oauthMember);
        return hearts.stream()
                .map(it -> PerformanceResponse.from(it.getPerformance()))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean getMemberIsLikePerformance(Long memberId, Long performanceId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        return heartRepository.findByOauthMemberAndPerformance(oauthMember, performance).isPresent();
    }
}
