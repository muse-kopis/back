package muse_kopis.muse.performance.actor;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.performance.actor.dto.Actor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actors")
@RequiredArgsConstructor
public class FavoriteActorController {

    private final FavoriteActorService favoriteActorService;

    @PostMapping
    public ResponseEntity<Long> favorite(@Auth Long memberId, @RequestBody Actor actor) {
        return ResponseEntity.ok().body(favoriteActorService.favorite(memberId, actor.name()));
    }
}
