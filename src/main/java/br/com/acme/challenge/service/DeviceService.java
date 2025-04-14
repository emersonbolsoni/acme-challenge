package br.com.acme.challenge.service;

import br.com.acme.challenge.entity.Device;
import br.com.acme.challenge.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevices(){
        return deviceRepository.findAll();
    }

    public Device getDeviceById(Long id){
        return deviceRepository.findById(id).orElse(null);
    }

    public Device createDevice(Device device){
        device.setCreation_time(LocalDateTime.now());
        return deviceRepository.save(device);
    }

    public Device updateDevice(Long id, Device device){
        Device existingDevice = getDeviceById(id);
        if(existingDevice != null){
            existingDevice.setName(device.getName());
            existingDevice.setBrand(device.getBrand());
            existingDevice.setState(device.getState());
            return deviceRepository.save(existingDevice);
        }
        return null;
    }

    public void deleteDevice(Long id){
        deviceRepository.deleteById(id);
    }
}
