package muse_kopis.muse.actor.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.actor.domain.Actor;
import muse_kopis.muse.actor.domain.ActorRepository;
import muse_kopis.muse.actor.domain.dto.ActorDto;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.actor.domain.FavoriteActor;
import muse_kopis.muse.actor.domain.FavoriteActorRepository;
import muse_kopis.muse.usergenre.domain.UserGenre;
import muse_kopis.muse.usergenre.domain.UserGenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorService {

    private final FavoriteActorRepository favoriteActorRepository;
    private final OauthMemberRepository oauthMemberRepository;
    private final UserGenreRepository userGenreRepository;
    private final ActorRepository actorRepository;

    @Transactional
    public Long favorite(Long memberId, String actorsName, String actorId, String url) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        UserGenre userGenre = userGenreRepository.getUserGenreByOauthMember(oauthMember);
        Actor actor = actorRepository.findByActorId(actorId);
        FavoriteActor favoriteActor = new FavoriteActor(memberId, actor, userGenre);
        FavoriteActor save = favoriteActorRepository.save(favoriteActor);
        return save.id();
    }

    public List<ActorDto> favorites(Long memberId) {
        return favoriteActorRepository.findAllByMemberId(memberId).stream()
                .map(ActorDto::from)
                .collect(Collectors.toList());
    }

    public List<ActorDto> findActors(String actorName) {
        return actorRepository.findAllByNameIsContaining(actorName);
    }
}
