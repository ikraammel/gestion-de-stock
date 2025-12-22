package com.ikram.gestiondestock.dto;

import com.ikram.gestiondestock.model.CommandeClient;
import com.ikram.gestiondestock.model.EtatCommande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommandeClientDto {
    private Integer id;
    private String code;
    private Instant dateCommande;
    private ClientDto client;

    private List<LigneCommandeClientDto> ligneCommandeClient;
    private EtatCommande etatCommande;
    private Integer entrepriseId;

    public static CommandeClientDto fromEntity(CommandeClient commandeClient) {
        if (commandeClient == null) {
            return null;
        }
        return CommandeClientDto.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateCommande(commandeClient.getDateCommande())
                .etatCommande(commandeClient.getEtatCommande())
                .client(ClientDto.fromEntity(commandeClient.getClient()))
                .ligneCommandeClient(
                        commandeClient.getCommandeClients() != null ?
                                commandeClient.getCommandeClients().stream()
                                        .map(LigneCommandeClientDto::fromEntity) // On convertit chaque ligne en DTO
                                        .collect(Collectors.toList()) : null
                )
                .entrepriseId(commandeClient.getEntrepriseId())
                .build();
    }

    public static CommandeClient toEntity(CommandeClientDto dto) {
        if (dto == null) {
            return null;
        }
        CommandeClient commandeClient = new CommandeClient();
        commandeClient.setId(dto.getId());
        commandeClient.setCode(dto.getCode());
        commandeClient.setClient(ClientDto.toEntity(dto.getClient()));
        commandeClient.setDateCommande(dto.getDateCommande());
        commandeClient.setEntrepriseId(dto.getEntrepriseId());
        commandeClient.setEtatCommande(dto.getEtatCommande());
        return commandeClient;
    }
    public boolean isCommandeLivree(){
        return EtatCommande.LIVREE.equals(this.etatCommande);
    }
}
