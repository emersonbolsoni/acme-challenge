package br.com.acme.challenge.controller;

import br.com.acme.challenge.entity.Device;
import br.com.acme.challenge.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public List<Device> getAllDevices(){
        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Long id){
        return deviceService.getDeviceById(id);
    }

    @PostMapping
    public Device createDevice(@RequestBody Device device){
        return deviceService.createDevice(device);
    }

    @PutMapping("/{id}")
    public Device updateDevice(@PathVariable Long id, @RequestBody Device updatedDevice){
        return deviceService.updateDevice(id, updatedDevice);
    }

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
    }

}
