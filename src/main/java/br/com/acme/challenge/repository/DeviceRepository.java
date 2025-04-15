package br.com.acme.challenge.repository;

import br.com.acme.challenge.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findAllByBrand(String brand);
    List<Device> findAllByState(String state);

    List<Device> findAllByBrandAndState(String brand, String state);

}
