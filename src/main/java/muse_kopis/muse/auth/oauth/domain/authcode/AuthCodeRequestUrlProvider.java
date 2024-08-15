package muse_kopis.muse.auth.oauth.domain.authcode;

import muse_kopis.muse.auth.oauth.domain.OauthServerType;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
} // AuthCode URL 제공