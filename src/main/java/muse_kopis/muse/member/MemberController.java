package muse_kopis.muse.member;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Long> signUp(@RequestBody SignUpRequest signUpRequest) {

        Long id = memberService.signUp(
                signUpRequest.username(),
                signUpRequest.password(),
                signUpRequest.name()
        );
        return ResponseEntity.ok().body(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Long id = memberService.login(loginRequest.username(), loginRequest.password());
        String token = jwtService.createToken(id);
        return ResponseEntity.ok().body(new LoginResponse(token));
    }
}
