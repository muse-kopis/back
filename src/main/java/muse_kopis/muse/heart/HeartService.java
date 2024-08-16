package muse_kopis.muse.heart;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.common.HeartDuplicatedException;
import muse_kopis.muse.common.NotFoundHeartException;
import muse_kopis.muse.common.NotFoundPerformanceException;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.PerformanceRepository;
import muse_kopis.muse.performance.dto.PerformanceResponse;
import muse_kopis.muse.performance.usergenre.UserGenreService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final PerformanceRepository performanceRepository;
    private final OauthMemberRepository oauthMemberRepository;
    private final UserGenreService userGenreService;

    @Transactional
    public void like(Long memberId, String performanceName, String venue) {
        OauthMember oauthMember = oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundHeartException("찾을 수 없는 회원입니다."));
        Performance performance = performanceRepository.findByPerformanceNameAndVenue(performanceName, venue)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
        if (heartRepository.existsByOauthMemberAndPerformance(oauthMember, performance)) {
            throw new HeartDuplicatedException("이미 좋아요를 눌렀습니다.");
        }
        userGenreService.updateGenre(performance, oauthMember);
        heartRepository.save(new Heart(oauthMember, performance));
    }

    @Transactional
    public void unlike(Long memberId, String performanceName, String venue) {
        OauthMember oauthMember = oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundHeartException("찾을 수 없는 회원입니다."));
        Performance performance = performanceRepository.findByPerformanceNameAndVenue(performanceName, venue)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
        Heart heart = heartRepository.findByOauthMemberAndPerformance(oauthMember, performance)
                .orElseThrow(() -> new NotFoundHeartException("좋아요 기록이 없습니다."));
        heartRepository.delete(heart);
    }

    public List<PerformanceResponse> getMembersLikePerformanceList(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundHeartException("찾을 수 없는 회원입니다."));
        List<Heart> hearts = heartRepository.findByOauthMember(oauthMember);
        return hearts.stream()
                .map(it -> PerformanceResponse.from(it.getPerformance()))
                .collect(Collectors.toList());
    }
}
