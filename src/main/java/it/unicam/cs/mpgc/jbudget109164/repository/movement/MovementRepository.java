package it.unicam.cs.mpgc.jbudget109164.repository.movement;

import it.unicam.cs.mpgc.jbudget109164.dto.NewMovementDTO;
import it.unicam.cs.mpgc.jbudget109164.repository.Repository;

import java.util.UUID;

public interface MovementRepository extends Repository<UUID, NewMovementDTO> {
}
