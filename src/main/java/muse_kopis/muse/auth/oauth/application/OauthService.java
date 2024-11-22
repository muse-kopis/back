package muse_kopis.muse.auth.oauth.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.auth.oauth.domain.OauthServerType;
import muse_kopis.muse.auth.oauth.domain.TierImageURL;
import muse_kopis.muse.auth.oauth.domain.UserTier;
import muse_kopis.muse.auth.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import muse_kopis.muse.auth.oauth.domain.client.OauthMemberClientComposite;
import muse_kopis.muse.auth.oauth.domain.dto.LoginDto;
import muse_kopis.muse.auth.oauth.domain.dto.UserInfo;
import muse_kopis.muse.heart.domain.Heart;
import muse_kopis.muse.heart.domain.HeartRepository;
import muse_kopis.muse.performance.domain.usergenre.domain.UserGenreRepository;
import muse_kopis.muse.performance.domain.usergenre.application.UserGenreServiceImpl;
import muse_kopis.muse.ticketbook.domain.TicketBook;
import muse_kopis.muse.ticketbook.domain.TicketBookRepository;
import muse_kopis.muse.photo.domain.PhotoRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final TierImageURL tierImageURL;
    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberRepository oauthMemberRepository;
    private final UserGenreServiceImpl userGenreService;
    private final HeartRepository heartRepository;
    private final TicketBookRepository ticketBookRepository;
    private final PhotoRepository photoRepository;
    private final UserGenreRepository userGenreRepository;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public LoginDto login(OauthServerType oauthServerType, String authCode) {
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> {
                        oauthMember.updateUserTier(UserTier.NEWBIE, tierImageURL.getNewbie());
                        OauthMember save = oauthMemberRepository.save(oauthMember);
                        userGenreService.initGenre(oauthMember);
                        return save;
                    }
                );
        return new LoginDto(saved.id(), saved.isNewUser());
    }

    public String updateUsername(Long memberId, String newUsername) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        oauthMember.updateUsername(newUsername);
        oauthMemberRepository.save(oauthMember);
        return newUsername;
    }

    public UserInfo getInfo(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        return new UserInfo(oauthMember.username(), oauthMember.userTier(), oauthMember.tierImageUrl());
    }

    public void updateUserState(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        oauthMember.updateOldUser(false);
        oauthMemberRepository.save(oauthMember);
    }

    public void deleteUser(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        List<TicketBook> ticketBooks = ticketBookRepository.findAllByOauthMember(oauthMember);
        List<Heart> hearts = heartRepository.findByOauthMember(oauthMember);
        ticketBooks.forEach(ticketBook -> photoRepository
                .deleteAll(
                        photoRepository.findAllByTicketBook(ticketBook)
                )
        );
        userGenreRepository.delete(userGenreRepository.getUserGenreByOauthMember(oauthMember));
        heartRepository.deleteAll(hearts);
        ticketBookRepository.deleteAll(ticketBooks);
        oauthMemberRepository.delete(oauthMember);
    }
}