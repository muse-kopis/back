package muse_kopis.muse.actor.presentation;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.actor.application.ActorService;
import muse_kopis.muse.actor.domain.dto.FavoriteActorDto;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.actor.domain.dto.CastMemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    /**
     * @apiNote Favorite Actor
     * @param Long memberId (JWT Token)
     * @param ActorDto actor
     * @return Long
     */
    @PostMapping
    @Operation(summary = "관심 배우 등록", description = "관심 배우를 등록합니다.")
    public ResponseEntity<Long> favorite(@Auth Long memberId, @RequestBody CastMemberDto actor) {
        return ResponseEntity.ok().body(actorService.favorite(memberId, actor.actorId(), actor.name()));
    }

    /**
     * @apiNote Actors Favorites
     * @param Long memberId (JWT Token)
     * @return List<ActorDto>
     */
    @GetMapping
    @Operation(summary = "관심 배우 조회", description = "관심 배우를 조회합니다.")
    public ResponseEntity<List<FavoriteActorDto>> favorites(@Auth Long memberId) {
        return ResponseEntity.ok().body(actorService.favorites(memberId));
    }
}
