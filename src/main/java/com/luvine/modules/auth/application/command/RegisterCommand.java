package com.luvine.modules.auth.application.command;

public record RegisterCommand(
        String email,
        String firstName,
        String lastName,
        String hashedPassword
) {
}