package service;

import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    Service service;

    @BeforeEach
    void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @AfterEach
    void tearDown() {
        service.deleteStudent("123");
    }

    @org.junit.jupiter.api.Test
    void testAddStudentSuccessfully() {
        Student student = new Student("123", "name1", 931, "name1@yahoo.com");
        assertEquals(null, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentWithError() {
        Student student = new Student("123", "name1", 931, "name1@yahoo.com");
        service.addStudent(student);
        assertEquals(student, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentThatAlreadyExists() {
        Student student = new Student("123", "name1", 931, "name1@yahoo.com");
        service.addStudent(student);
        assertEquals(student, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentInvalidGroup1() {
        Student student = new Student("123", "name1", -1, "name1@yahoo.com");
        Exception exception = assertThrows(ValidationException.class, ()->{service.addStudent(student);});

        String expectedMessage = "Grupa incorecta!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentEmptyId() {
        Student student = new Student("","",0,"");
        Exception exception = assertThrows(ValidationException.class, ()->{service.addStudent(student);});

        String expectedMessage = "Id incorect!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentEmptyEmail() {
        Student student = new Student("6","georgiana",0,"");
        Exception exception = assertThrows(ValidationException.class, ()->{service.addStudent(student);});

        String expectedMessage = "Email incorect!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentEmptyName() {
        Student student = new Student("6","",0,"random@yahoo.com");
        Exception exception = assertThrows(ValidationException.class, ()->{service.addStudent(student);});

        String expectedMessage = "Nume incorect!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentWithGroupZero() {
        Student student = new Student("123", "name1", 0, "name1@yahoo.com");
        service.addStudent(student);
        assertEquals(student, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentWithGroupMaxInt() {
        Student student = new Student("123", "name1", Integer.MAX_VALUE, "name1@yahoo.com");
        service.addStudent(student);
        assertEquals(student, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentWithGroupMinInt() {
        Student student = new Student("123", "name1", Integer.MIN_VALUE, "name1@yahoo.com");
        service.addStudent(student);
        assertEquals(student, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentWithGroupMaxIntPlus() {
        Student student = new Student("123", "name1", Integer.MAX_VALUE + 1, "name1@yahoo.com");
        service.addStudent(student);
        assertEquals(student, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentWithGroupMinIntMinus() {
        Student student = new Student("123", "name1", Integer.MIN_VALUE - 1, "name1@yahoo.com");
        service.addStudent(student);
        assertEquals(student, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentIdString() {
        Student student = new Student("abc", "name1", 931, "name1@yahoo.com");
        assertEquals(null, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentIdInt() {
        Student student = new Student("123", "name1", 931, "name1@yahoo.com");
        assertEquals(null, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddTemaWithInvalidId() {
        Tema tema = new Tema("","description1",7,5);
        Exception exception = assertThrows(ValidationException.class, ()->{service.addTema(tema);});
        String expectedMessage = "Numar tema invalid!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testAddTemaWithInvalidDeadline() {
        Tema tema = new Tema("1","description1",15,5);
        Exception exception = assertThrows(ValidationException.class, ()->{service.addTema(tema);});
        String expectedMessage = "Deadlineul trebuie sa fie intre 1-14.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
