package br.com.acme.challenge.service;

import br.com.acme.challenge.entity.Device;
import br.com.acme.challenge.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public ResponseEntity getAllDevices(String brand, String state){
        if(brand == null && state == null){
            return ResponseEntity.ok(deviceRepository.findAll());
        } else if(brand == null && state != null){
            return ResponseEntity.ok(deviceRepository.findAllByState(state));
        } else if(brand != null && state == null){
            return ResponseEntity.ok(deviceRepository.findAllByBrand(brand));
        } else {
            return ResponseEntity.ok(deviceRepository.findAllByBrandAndState(brand, state));
        }
    }
    public ResponseEntity getAllDevicesByBrand(String brand){
        return ResponseEntity.ok(deviceRepository.findAllByBrand(brand));
    }
    public ResponseEntity getAllDevicesByState(String state){
        return ResponseEntity.ok(deviceRepository.findAllByState(state));
    }
    public ResponseEntity getDeviceById(Long id){
        return ResponseEntity.ok(deviceRepository.findById(id).orElse(null));
    }

    public ResponseEntity createDevice(Device device){
        device.setCreation_time(LocalDateTime.now());
        deviceRepository.save(device);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity updateDevice(Long id, Device device){
        Device existingDevice = deviceRepository.findById(id).orElse(null);
        if(existingDevice != null && existingDevice.getState().equalsIgnoreCase("in use")){
            return ResponseEntity.status(422).body("The device is in use, is not possible to update");
        } else if (existingDevice == null){
            return ResponseEntity.notFound().build();
        } else {
            existingDevice.setName(device.getName() != null ? device.getName() : existingDevice.getName());
            existingDevice.setBrand(device.getBrand() != null ? device.getBrand() : existingDevice.getBrand());
            existingDevice.setState(device.getState() != existingDevice.getState() ? device.getState() : existingDevice.getState());
            deviceRepository.save(existingDevice);
            return ResponseEntity.status(201).build();
        }
    }

    public ResponseEntity deleteDevice(Long id){
        Device existingDevice = deviceRepository.findById(id).orElse(null);
        if(existingDevice != null && existingDevice.getState().equalsIgnoreCase("in use")){
            return ResponseEntity.status(422).body("The device is in use, is not possible to delete");
        } else if (existingDevice == null){
            return ResponseEntity.notFound().build();
        } else {
            deviceRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
    }
}
