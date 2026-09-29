package com.ice.harmonia.record;

import jakarta.validation.constraints.NotBlank;

public record AliasModificationRequest(
        @NotBlank(message = "Alias name cannot be empty")
        String aliasName
) {
}
