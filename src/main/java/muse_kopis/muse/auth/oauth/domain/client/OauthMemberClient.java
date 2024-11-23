package muse_kopis.muse.auth.oauth.domain.client;

import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthServerType;

public interface OauthMemberClient {

    OauthServerType supportServer();
    OauthMember fetch(String code);
}