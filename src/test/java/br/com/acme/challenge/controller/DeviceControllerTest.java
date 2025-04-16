package br.com.acme.challenge.controller;

import br.com.acme.challenge.entity.Device;
import br.com.acme.challenge.service.DeviceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeviceControllerTest {

    private static final String URL = "/v1/devices";
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DeviceService deviceService;

    @Test
    void testCreateDevice_AvailableMonitor() throws Exception {
        Device device = new Device();
        device.setBrand("LG");
        device.setName("Monitor");
        device.setState("available");
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(device))
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testCreateDevice_UnsuportedMediaType() throws Exception {
        Device device = new Device();
        device.setBrand("LG");
        device.setName("Monitor");
        device.setState("in use");
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType("application/xml")
                .content(objectMapper.writeValueAsString(device))
        ).andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
    }

    @Test
    void testListAllDevices() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        List<Device> deviceList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

        assertNotNull(deviceList);
        assertNotEquals(0, deviceList.size());
    }

    @Test
    void testListAllDevicesFilteringByExistingBrand() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL + "?brand=LG")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        List<Device> deviceList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

        assertNotNull(deviceList);
        assertNotEquals(0, deviceList.size());

    }

    @Test
    void testListAllDevicesFilteringByNotExistingBrand() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL + "?brand=EPSON")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        List<Device> deviceList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

        assertNotNull(deviceList);
        assertEquals(0, deviceList.size());

    }

    @Test
    void testListAllDevicesFilteringByMatchState() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL + "?state=available")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        List<Device> deviceList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

        assertNotNull(deviceList);
        assertNotEquals(0, deviceList.size());

    }

    @Test
    void testListAllDevicesFilteringByNotMatchState() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL + "?state=unavailable")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        List<Device> deviceList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

        assertNotNull(deviceList);
        assertEquals(0, deviceList.size());

    }

    @Test
    void testListAllDevicesFilteringByBrandAndMatchState() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL + "?brand=LG&state=available")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        List<Device> deviceList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

        assertNotNull(deviceList);
        assertNotEquals(0, deviceList.size());

    }

    @Test
    void testListAllDevicesBadRequestWrongUrl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v2/devices")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isNotFound()
        ).andReturn();
    }

    @Test
    void testListDeviceById() throws Exception {
        ArrayList<Device> query = (ArrayList<Device>) deviceService.getAllDevices("LG", "available").getBody();
        if(query.size() > 0) {
            Device dev = query.get(0);
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + dev.getId())
                    .contentType("application/json")
            ).andExpect(MockMvcResultMatchers.status().isOk()
            ).andReturn();

            Device device = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertNotNull(device);
            assertEquals(dev.getId(), device.getId());
            assertEquals(dev.getName(), device.getName());
            assertEquals(dev.getBrand(), device.getBrand());
            assertEquals(dev.getState(), device.getState());
            assertEquals(dev.getCreation_time(), device.getCreation_time());
        }
    }

    @Test
    void testUpdateDevice_updateBrand() throws Exception {
        ArrayList<Device> query = (ArrayList<Device>) deviceService.getAllDevices("LG", "available").getBody();
        if(query.size() > 0) {
            Device dev = query.get(0);
            Device device = new Device();
            device.setBrand("EPSON");

            mockMvc.perform(MockMvcRequestBuilders.put(URL + "/" + dev.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(device))
            ).andExpect(MockMvcResultMatchers.status().isOk());

            Device queryUpdated = (Device)  deviceService.getDeviceById(dev.getId()).getBody();

            assertNotEquals(query.get(0).getBrand(), queryUpdated.getBrand());
            assertEquals(query.get(0).getCreation_time(), queryUpdated.getCreation_time());
            assertEquals(query.get(0).getName(), queryUpdated.getName());
            assertEquals(query.get(0).getState(), queryUpdated.getState());

        }
    }

    @Test
    void testUpdateDevice_updateName() throws Exception {
        ArrayList<Device> query = (ArrayList<Device>) deviceService.getAllDevices("EPSON", "available").getBody();
        if(query.size() > 0) {
            Device dev = query.get(0);
            Device device = new Device();
            device.setName("Printer");

            mockMvc.perform(MockMvcRequestBuilders.put(URL + "/" + dev.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(device))
            ).andExpect(MockMvcResultMatchers.status().isOk());

            Device queryUpdated = (Device)  deviceService.getDeviceById(dev.getId()).getBody();

            assertNotEquals(query.get(0).getName(), queryUpdated.getName());
            assertEquals(query.get(0).getCreation_time(), queryUpdated.getCreation_time());
            assertEquals(query.get(0).getBrand(), queryUpdated.getBrand());
            assertEquals(query.get(0).getState(), queryUpdated.getState());

        }
    }

    @Test
    void testUpdateDevice_updateStateToInUse() throws Exception {
        ArrayList<Device> query = (ArrayList<Device>) deviceService.getAllDevices("EPSON", "available").getBody();
        if(query.size() > 0) {
            Device dev = query.get(0);
            Device device = new Device();
            device.setState("in use");

            mockMvc.perform(MockMvcRequestBuilders.put(URL + "/" + dev.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(device))
            ).andExpect(MockMvcResultMatchers.status().isOk());

            Device queryUpdated = (Device)  deviceService.getDeviceById(dev.getId()).getBody();

            assertNotEquals(query.get(0).getState(), queryUpdated.getState());
            assertEquals(query.get(0).getCreation_time(), queryUpdated.getCreation_time());
            assertEquals(query.get(0).getBrand(), queryUpdated.getBrand());
            assertEquals(query.get(0).getName(), queryUpdated.getName());

        }
    }

    @Test
    void testUpdateDevice_updateNameBrandWithInUseState() throws Exception {
        ArrayList<Device> query = (ArrayList<Device>) deviceService.getAllDevices("EPSON", "in use").getBody();
        if(query.size() > 0) {
            Device dev = query.get(0);
            Device device = new Device();
            device.setName("Monitor");
            device.setBrand("LG");

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(URL + "/" + dev.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(device))
            ).andExpect(MockMvcResultMatchers.status().is4xxClientError()
            ).andReturn();;

            assertEquals("The device is in use, is not possible to update", result.getResponse().getContentAsString());

        }
    }

    @Test
    void testDeleteDevice_deleteWithInUseState() throws Exception {
        ArrayList<Device> query = (ArrayList<Device>) deviceService.getAllDevices("EPSON", "in use").getBody();
        if(query.size() > 0) {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/" + query.get(0).getId())
                    .contentType("application/json")
            ).andExpect(MockMvcResultMatchers.status().is4xxClientError()
            ).andReturn();;

            assertEquals("The device is in use, is not possible to delete", result.getResponse().getContentAsString());
        }
    }

    @Test
    void testDeleteDevice() throws Exception {
        ArrayList<Device> query = (ArrayList<Device>) deviceService.getAllDevices("EPSON", "in use").getBody();

        if(query.size() > 0) {
            Device dev = new Device();
            dev.setState("available");
            deviceService.updateDevice(query.get(0).getId(), dev);

            mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/" + query.get(0).getId())
                    .contentType("application/json")
            ).andExpect(MockMvcResultMatchers.status().isOk());

        }
    }

    @Test
    void testDeleteDevice_notFoundDevice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/1000")
                .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}