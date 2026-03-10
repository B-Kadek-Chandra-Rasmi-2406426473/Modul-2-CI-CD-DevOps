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

    @Test
    void testCreateCarWithExistingId() {
        Car car = new Car();
        car.setCarId("existing-id-123");
        car.setCarName("Avanza");

        Car result = carRepository.create(car);
        assertEquals("existing-id-123", result.getCarId());
    }

    @Test
    void testFindByIdMultipleCars() {
        Car car1 = new Car();
        car1.setCarId("id-1");
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("id-2");
        carRepository.create(car2);

        Car foundCar = carRepository.findById("id-2");
        assertNotNull(foundCar);
        assertEquals("id-2", foundCar.getCarId());
    }

    @Test
    void testUpdateMultipleCars() {
        Car car1 = new Car();
        car1.setCarId("id-1");
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("id-2");
        carRepository.create(car2);

        Car updatedCar = new Car();
        updatedCar.setCarName("Pajero Sport");

        Car result = carRepository.update("id-2", updatedCar);
        assertNotNull(result);
        assertEquals("id-2", result.getCarId());
        assertEquals("Pajero Sport", result.getCarName());
    }
}