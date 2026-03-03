package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    void testCreateAndFind() {
        Car car = new Car();
        car.setCarName("Pajero");
        car.setCarColor("Hitam");
        car.setCarQuantity(10);
        carRepository.create(car);

        assertNotNull(car.getCarId()); // Harus di-generate UUID

        Car savedCar = carRepository.findById(car.getCarId());
        assertEquals("Pajero", savedCar.getCarName());
    }

    @Test
    void testFindAll() {
        Car car1 = new Car();
        carRepository.create(car1);
        Car car2 = new Car();
        carRepository.create(car2);

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
    }

    @Test
    void testFindByIdIfNotFound() {
        assertNull(carRepository.findById("id-ngasal"));
    }

    @Test
    void testUpdate() {
        Car car = new Car();
        car.setCarName("Lambo");
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Ferrari");
        updatedCar.setCarColor("Merah");
        updatedCar.setCarQuantity(5);

        Car result = carRepository.update(car.getCarId(), updatedCar);

        assertNotNull(result);
        assertEquals("Ferrari", result.getCarName());
        assertEquals("Merah", result.getCarColor());
        assertEquals(5, result.getCarQuantity());
    }

    @Test
    void testUpdateIfNotFound() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Ferrari");
        assertNull(carRepository.update("id-palsu", updatedCar));
    }

    @Test
    void testDelete() {
        Car car = new Car();
        carRepository.create(car);
        assertNotNull(carRepository.findById(car.getCarId()));

        carRepository.delete(car.getCarId());
        assertNull(carRepository.findById(car.getCarId()));
    }
}