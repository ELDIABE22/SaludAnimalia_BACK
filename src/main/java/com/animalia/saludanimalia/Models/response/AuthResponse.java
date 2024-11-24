package com.animalia.saludanimalia.Models.response;

import com.animalia.saludanimalia.Models.Usuario;

public record AuthResponse(Usuario usuario,
        String token) {
}
