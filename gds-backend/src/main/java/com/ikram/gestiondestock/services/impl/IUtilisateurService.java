package com.ikram.gestiondestock.services.impl;

import com.ikram.gestiondestock.Validator.UtilisateurValidator;
import com.ikram.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.ikram.gestiondestock.dto.UtilisateurDto;
import com.ikram.gestiondestock.exception.EntityNotFoundException;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import com.ikram.gestiondestock.model.Utilisateur;
import com.ikram.gestiondestock.repository.UtilisateurRepository;
import com.ikram.gestiondestock.services.UtilisateurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IUtilisateurService implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {

        List<String> errors = UtilisateurValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException(
                    "L'utilisateur n'est pas valide",
                    ErrorCodes.UTILISATEUR_NOT_VALID,
                    errors
            );
        }

        if (userAlreadyExists(dto.getEmail())) {
            throw new InvalidEntityException(
                    "Un autre utilisateur avec le même email existe déjà",
                    ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
                    Collections.singletonList("Email déjà utilisé")
            );
        }

        // ✅ TOUJOURS encoder
        dto.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));

        return UtilisateurDto.fromEntity(
                utilisateurRepository.save(UtilisateurDto.toEntity(dto))
        );
    }


    private boolean userAlreadyExists(String email) {
        Optional<Utilisateur> user = utilisateurRepository.findUtilisateurByEmail(email);
        return user.isPresent();
    }

    @Override
    public UtilisateurDto findById(Integer id) {
        if (id == null) {
            log.error("Utilisateur ID is null");
            return null;
        }
        return utilisateurRepository.findById(id)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }

    @Override
    public UtilisateurDto findByEmail(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'email = " + email + " n' ete trouve dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Utilisateur ID is null");
            return;
        }
        utilisateurRepository.deleteById(id);
    }

    @Override
    public UtilisateurDto changerMotDePasse(String email, ChangerMotDePasseUtilisateurDto dto) {

        if (dto == null) {
            throw new InvalidOperationException(
                    "Aucune information fournie",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID
            );
        }

        if (!StringUtils.hasLength(dto.getMotDePasse()) ||
                !StringUtils.hasLength(dto.getConfirmMotDePasse())) {
            throw new InvalidOperationException(
                    "Mot de passe invalide",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID
            );
        }

        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
            throw new InvalidOperationException(
                    "Les mots de passe ne sont pas identiques",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID
            );
        }

        // ✅ UTILISATEUR IDENTIFIÉ VIA JWT
        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur avec l'email " + email + " non trouvé",
                        ErrorCodes.UTILISATEUR_NOT_FOUND
                ));

        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));

        return UtilisateurDto.fromEntity(utilisateurRepository.save(utilisateur));
    }

}