package com.animalia.saludanimalia.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.animalia.saludanimalia.Models.Cita;
import com.animalia.saludanimalia.Models.Mascota;
import com.animalia.saludanimalia.Models.Usuario;
import com.animalia.saludanimalia.Models.response.ApiResponse;
import com.animalia.saludanimalia.Models.response.AuthResponse;
import com.animalia.saludanimalia.Repository.CitaRepositorio;
import com.animalia.saludanimalia.Repository.MascotaRepositorio;
import com.animalia.saludanimalia.Repository.UsuarioRepositorio;
import com.animalia.saludanimalia.Utils.JwtUtils;

@Service
public class UsuarioServicio implements UserDetailsService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CitaRepositorio citaRepositorio;

    @Autowired
    private MascotaRepositorio mascotaRepositorio;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public ApiResponse<Usuario> registroUsuarioServicio(Usuario usuario) {
        if (usuarioRepositorio.findByEmail(usuario.getEmail()).isPresent()) {
            return ApiResponse.<Usuario>builder()
                    .data(null)
                    .message("El correo ya está registrado")
                    .build();
        }

        if (usuarioRepositorio.findByTelefono(usuario.getTelefono()).isPresent()) {
            return ApiResponse.<Usuario>builder()
                    .data(null)
                    .message("El teléfono ya está registrado")
                    .build();
        }

        Usuario usuarioEntidad = Usuario.builder()
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .contrasena(passwordEncoder.encode(usuario.getContrasena()))
                .build();

        Usuario usuarioGuardado = usuarioRepositorio.save(usuarioEntidad);

        return ApiResponse.<Usuario>builder()
                .data(usuarioGuardado)
                .message("Usuario registrado")
                .build();
    }

    public ApiResponse<AuthResponse> iniciarSesionServicio(Usuario usuario) {
        try {
            String email = usuario.getEmail();
            String password = usuario.getContrasena();

            Authentication authentication = this.authenticate(email, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Usuario usuarioDTA = usuarioRepositorio.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Correo no encontrado"));

            String accessToken = jwtUtils.createToken(authentication);
            AuthResponse authResponse = new AuthResponse(usuarioDTA, accessToken);

            ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                    .data(authResponse)
                    .message("Inicio de sesión exitoso")
                    .build();

            return apiResponse;
        } catch (BadCredentialsException e) {
            return ApiResponse.<AuthResponse>builder()
                    .data(null)
                    .message("Correo o contraseña incorrectos")
                    .build();
        } catch (IllegalStateException e) {
            return ApiResponse.<AuthResponse>builder()
                    .data(null)
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ApiResponse.<AuthResponse>builder()
                    .data(null)
                    .message(e.getMessage())
                    .build();
        }
    }

    public ApiResponse<Usuario> actualizarUsuarioServicio(Integer id, Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(id);

        if (!usuarioOptional.isPresent()) {
            return ApiResponse.<Usuario>builder().data(null).message("Usuario no encontrado").build();
        }

        Usuario usuarioExistente = usuarioOptional.get();

        if (usuarioRepositorio.findByEmail(usuario.getEmail()).isPresent()
                && !usuarioExistente.getEmail().equals(usuario.getEmail())) {
            return ApiResponse.<Usuario>builder()
                    .data(null)
                    .message("El correo ya está registrado")
                    .build();
        }

        if (usuarioRepositorio.findByTelefono(usuario.getTelefono()).isPresent()
                && !usuarioExistente.getTelefono().equals(usuario.getTelefono())) {
            return ApiResponse.<Usuario>builder()
                    .data(null)
                    .message("El teléfono ya está registrado")
                    .build();
        }

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setTelefono(usuario.getTelefono());

        Usuario usuarioGuardado = usuarioRepositorio.save(usuarioExistente);

        return ApiResponse.<Usuario>builder()
                .data(usuarioGuardado)
                .message("Usuario actualizado exitosamente")
                .build();
    }

    public ApiResponse<Void> eliminarUsuarioServicio(Integer id) {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(id);

        if (!usuarioOptional.isPresent()) {
            return ApiResponse.<Void>builder()
                    .data(null)
                    .message("Usuario no encontrado")
                    .build();
        }

        List<Cita> citas = citaRepositorio.findByIdUsuario(id);
        citaRepositorio.deleteAll(citas);

        List<Mascota> mascotas = mascotaRepositorio.findByIdUsuario(id);
        mascotaRepositorio.deleteAll(mascotas);

        usuarioRepositorio.deleteById(id);

        return ApiResponse.<Void>builder()
                .data(null)
                .message("Usuario eliminado exitosamente")
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuarioEntidad = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El correo no existe"));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(
                usuarioEntidad.getEmail(), usuarioEntidad.getContrasena(), authorityList);
    }

    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.loadUserByUsername(email);

        if (userDetails == null) {
            throw new UsernameNotFoundException("Correo no encontrado");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
