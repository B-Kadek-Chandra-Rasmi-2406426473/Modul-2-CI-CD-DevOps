package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("car-123");
        car.setCarName("Civic");
    }

    @Test
    void testCreateCar() {
        carService.create(car);
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testFindAllCars() {
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        Iterator<Car> carIterator = carList.iterator();

        when(carRepository.findAll()).thenReturn(carIterator);

        List<Car> result = carService.findAll();
        assertEquals(1, result.size());
        assertEquals("Civic", result.get(0).getCarName());
    }

    @Test
    void testFindById() {
        when(carRepository.findById("car-123")).thenReturn(car);
        Car result = carService.findById("car-123");
        assertEquals("Civic", result.getCarName());
    }

    @Test
    void testUpdateCar() {
        carService.update("car-123", car);
        verify(carRepository, times(1)).update("car-123", car);
    }

    @Test
    void testDeleteCarById() {
        carService.deleteCarById("car-123");
        verify(carRepository, times(1)).delete("car-123");
    }
}