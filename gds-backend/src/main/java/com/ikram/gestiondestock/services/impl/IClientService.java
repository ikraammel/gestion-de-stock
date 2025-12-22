package com.ikram.gestiondestock.services.impl;

import com.ikram.gestiondestock.Validator.ClientValidator;
import com.ikram.gestiondestock.dto.ClientDto;
import com.ikram.gestiondestock.exception.EntityNotFoundException;
import com.ikram.gestiondestock.exception.ErrorCodes;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.model.CommandeClient;
import com.ikram.gestiondestock.repository.ClientRepository;
import com.ikram.gestiondestock.repository.CommandeClientRepository;
import com.ikram.gestiondestock.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IClientService implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CommandeClientRepository commandeClientRepository;

    @Override
    public ClientDto save(ClientDto dto) {
        List<String> errors = ClientValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Client is not valid {}", dto);
            throw new InvalidEntityException("Le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
        }

        return ClientDto.fromEntity(
                clientRepository.save(
                        ClientDto.toEntity(dto)
                )
        );
    }

    @Override
    public ClientDto findById(Integer id) {
        if (id == null) {
            log.error("Client ID is null");
            return null;
        }
        return clientRepository.findById(id)
                .map(ClientDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun Client avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.CLIENT_NOT_FOUND)
                );
    }

    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Client ID is null");
            return;
        }

        List<CommandeClient> commandeClients = commandeClientRepository.findAllByClientId(id);

        if (!commandeClients.isEmpty()) {
            throw new InvalidEntityException(
                    "Impossible de supprimer un client qui a déjà des commandes clients",
                    ErrorCodes.CLIENT_ALREADY_IN_USE
            );
        }

        // On supprime UNIQUEMENT si aucune commande n'existe
        clientRepository.deleteById(id);
    }

}
