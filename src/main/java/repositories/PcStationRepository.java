package repositories;

import models.PcStation;

import java.util.List;
import java.util.Optional;

public interface PcStationRepository {
    PcStation create(PcStation pc);
    Optional<PcStation> findById(int id);
    List<PcStation> findAllActive();
}
