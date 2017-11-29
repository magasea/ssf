package com.shellshellfish.aaas.account.service;

import java.util.HashMap;

public interface ResourceManagerService {
	HashMap<String, Object> response(String pagename, String[] argv);
}
