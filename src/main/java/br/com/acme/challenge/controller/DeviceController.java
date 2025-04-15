package br.com.acme.challenge.controller;

import br.com.acme.challenge.entity.Device;
import br.com.acme.challenge.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/devices", produces = {"application/json"})
@Tag(name = "devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    @Operation(summary = "List all devices", method = "GET")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="Success returned all devices"),
            @ApiResponse(responseCode="400", description="Bad Request, please send correct information"),
            @ApiResponse(responseCode="500", description="Server problems detected"),
    })
    public List<Device> getAllDevices(){
        return deviceService.getAllDevices();
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "List a device based on ID", method = "GET")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="Success returned the device by id"),
            @ApiResponse(responseCode="400", description="Bad Request, please send correct information"),
            @ApiResponse(responseCode="500", description="Server problems detected"),
    })
    public Device getDeviceById(@PathVariable Long id){
        return deviceService.getDeviceById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new device", method = "POST")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="Created with success"),
            @ApiResponse(responseCode="400", description="Bad Request, please send correct information"),
            @ApiResponse(responseCode="415", description="Unsupported Media Type"),
            @ApiResponse(responseCode="500", description="Server problems detected"),
    })
    public ResponseEntity createDevice(@RequestBody Device device){
        return deviceService.createDevice(device);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update device information", method = "PUT")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="Updated with success"),
            @ApiResponse(responseCode="400", description="Bad Request, please send correct information"),
            @ApiResponse(responseCode="422", description="Some business rule was violated"),
            @ApiResponse(responseCode="500", description="Server problems detected"),
    })
    public Device updateDevice(@PathVariable Long id, @RequestBody Device updatedDevice){
        return deviceService.updateDevice(id, updatedDevice);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete device", method = "DELETE")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="Deleted with success"),
            @ApiResponse(responseCode="400", description="Bad Request, please send correct information"),
            @ApiResponse(responseCode="422", description="Some business rule was violated"),
            @ApiResponse(responseCode="500", description="Server problems detected"),
    })
    public void deleteDevice(@PathVariable(value = "id") Long id) {
        deviceService.deleteDevice(id);
    }

}
