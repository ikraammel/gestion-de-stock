package com.ikram.gestiondestock.auth;

import com.ikram.gestiondestock.config.JwtService;
import com.ikram.gestiondestock.model.Roles;
import com.ikram.gestiondestock.model.Utilisateur;
import com.ikram.gestiondestock.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request){
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getFirstName());
        utilisateur.setPrenom(request.getLastName());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        Roles roles = new Roles();
        roles.setRoleName(request.getRole() != null ? request.getRole() : "USER");
        roles.setUtilisateur(utilisateur);
        utilisateur.setRoles(List.of(roles));

        userRepository.save(utilisateur);

        String jwtToken = jwtService.generateToken(utilisateur);
        return new AuthenticationResponse(jwtToken);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findUtilisateurByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}