package muse_kopis.muse.performance.actor;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.performance.castmember.CastMember;
import muse_kopis.muse.performance.castmember.CastMemberRepository;
import muse_kopis.muse.performance.usergenre.UserGenre;
import muse_kopis.muse.performance.usergenre.UserGenreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteActorService {

    private final FavoriteActorRepository favoriteActorRepository;
    private final OauthMemberRepository oauthMemberRepository;
    private final UserGenreRepository userGenreRepository;

    public Long favorite(Long memberId, String actorsName) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        UserGenre userGenre = userGenreRepository.getUserGenreByOauthMember(oauthMember);
        FavoriteActor favoriteActor = new FavoriteActor(userGenre, actorsName);
        FavoriteActor save = favoriteActorRepository.save(favoriteActor);
        return save.id();
    }
}
