package com.jorgedelarosa.aimiddleware.application.port.out;

import java.util.Map;

public interface ComfyUiOutPort {

    String queuePrompt(Map<String, Object> workflow, String clientId, String relativePath);
}