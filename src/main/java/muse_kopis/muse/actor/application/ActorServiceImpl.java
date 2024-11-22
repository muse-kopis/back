package muse_kopis.muse.actor.application;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.actor.domain.dto.ActorDto;
import muse_kopis.muse.actor.domain.FavoriteActor;
import muse_kopis.muse.actor.domain.FavoriteActorRepository;
import muse_kopis.muse.performance.domain.usergenre.domain.UserGenre;
import muse_kopis.muse.performance.domain.usergenre.domain.UserGenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {

    private final FavoriteActorRepository favoriteActorRepository;
    private final OauthMemberRepository oauthMemberRepository;
    private final UserGenreRepository userGenreRepository;

    public Long favorite(Long memberId, String actorsName) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        UserGenre userGenre = userGenreRepository.getUserGenreByOauthMember(oauthMember);
        FavoriteActor favoriteActor = new FavoriteActor(memberId, actorsName, userGenre);
        FavoriteActor save = favoriteActorRepository.save(favoriteActor);
        return save.id();
    }

    public List<ActorDto> favorites(Long memberId) {
        return favoriteActorRepository.findAllByMemberId(memberId).stream()
                .map(ActorDto::from)
                .collect(Collectors.toList());
    }
}
