package com.kkafara.server.config;

import com.kkafara.rt.Result;

public interface ConfigProvider {
  Result<ChatServerConfig, Exception> get();
}
