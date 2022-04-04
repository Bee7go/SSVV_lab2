package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.time.LocalDate;

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
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addStudent(student);
        });

        String expectedMessage = "Grupa incorecta!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentEmptyId() {
        Student student = new Student("", "", 0, "");
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addStudent(student);
        });

        String expectedMessage = "Id incorect!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentEmptyEmail() {
        Student student = new Student("6", "georgiana", 0, "");
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addStudent(student);
        });

        String expectedMessage = "Email incorect!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentEmptyName() {
        Student student = new Student("6", "", 0, "random@yahoo.com");
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addStudent(student);
        });

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
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addStudent(student);
        });

        String expectedMessage = "Grupa incorecta!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @org.junit.jupiter.api.Test
    void testAddStudentWithGroupMaxIntPlus() {
        Student student = new Student("123", "name1", Integer.MAX_VALUE + 1, "name1@yahoo.com");
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addStudent(student);
        });

        String expectedMessage = "Grupa incorecta!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
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
        assertEquals(student, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentIdInt() {
        Student student = new Student("123", "name1", 931, "name1@yahoo.com");
        assertEquals(null, service.addStudent(student));
    }

    @org.junit.jupiter.api.Test
    void testAddTemaWithInvalidId() {
        Tema tema = new Tema("", "description1", 7, 5);
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        String expectedMessage = "Numar tema invalid!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testAddTemaWithInvalidDeadline() {
        Tema tema = new Tema("1", "description1", 15, 5);
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        String expectedMessage = "Deadlineul trebuie sa fie intre 1-14.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    @org.junit.jupiter.api.Test
    void testStatementAddTema() {
        Tema tema = new Tema("test", "description1", 10, 5);
        service.addTema(tema);
        assertEquals(tema, service.addTema(tema));

    }

    @org.junit.jupiter.api.Test
    void testInvalidIdAddTema() {
        Tema tema = new Tema("", "description1", 10, 5);
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertTrue(exception.getMessage().contains("Numar tema invalid!"));
    }

    @org.junit.jupiter.api.Test
    void testNullIdAddTema() {
        Tema tema2 = new Tema(null, "description1", 10, 5); // Implica modificari in cod
        Exception exception2 = assertThrows(ValidationException.class, () -> {
            service.addTema(tema2);
        });
        assertTrue(exception2.getMessage().contains("Numar tema invalid!"));
    }

    @org.junit.jupiter.api.Test
    void testInvalidDescriptionAddTema() {
        Tema tema3 = new Tema("1", "", 10, 5);
        Exception exception3 = assertThrows(ValidationException.class, () -> {
            service.addTema(tema3);
        });
        assertTrue(exception3.getMessage().contains("Descriere invalida!"));
    }

    @org.junit.jupiter.api.Test
    void testNegativeDeadlineAddTema() {
        Tema tema4 = new Tema("1", "description1", -1, 5);
        Exception exception4 = assertThrows(ValidationException.class, () -> {
            service.addTema(tema4);
        });
        assertTrue(exception4.getMessage().contains("Deadlineul trebuie sa fie intre 1-14."));
    }

    @org.junit.jupiter.api.Test
    void testInvalidDeadlineAddTema() {
        Tema tema5 = new Tema("1", "description1", 15, 10);
        Exception exception5 = assertThrows(ValidationException.class, () -> {
            service.addTema(tema5);
        });
        assertTrue(exception5.getMessage().contains("Deadlineul trebuie sa fie intre 1-14."));
    }

    @org.junit.jupiter.api.Test
    void testNegativePresentedWeekAddTema() {
        Tema tema6 = new Tema("1", "description1", 5, -1);
        Exception exception6 = assertThrows(ValidationException.class, () -> {
            service.addTema(tema6);
        });
        assertTrue(exception6.getMessage().contains("Saptamana primirii trebuie sa fie intre 1-14."));
    }

    @org.junit.jupiter.api.Test
    void testInvalidPresentedWeekAddTema() {
        Tema tema7 = new Tema("1", "description1", 5, 15);
        Exception exception7 = assertThrows(ValidationException.class, () -> {
            service.addTema(tema7);
        });
        assertTrue(exception7.getMessage().contains("Saptamana primirii trebuie sa fie intre 1-14."));
    }

    @org.junit.jupiter.api.Test
    void testAlreadyExistingAddTema() {
        Tema tema8 = new Tema("1", "description1", 10, 5);
        assertEquals(tema8, service.addTema(tema8));
    }

    @org.junit.jupiter.api.Test
    void addGrade() {
        LocalDate dataPredare = LocalDate.of(Integer.parseInt("2019"), Integer.parseInt("10"), Integer.parseInt("7"));
        Nota nota = new Nota("123", "1", "1", 8.5,  dataPredare);
        Exception exception6 = assertThrows(ValidationException.class, () -> {
            service.addNota(nota, "feedback1");
        });
        assertTrue(exception6.getMessage().contains("Studentul nu mai poate preda aceasta tema!"));
    }

    @org.junit.jupiter.api.Test
    void testAddStudentAddGradeAddTema() {
        Student student = new Student("12345", "name1", 931, "name1@yahoo.com");
        assertEquals(null, service.addStudent(student));

        Tema tema = new Tema("test12345", "description1", 3, 1);
        service.addTema(tema);
        assertEquals(tema, service.addTema(tema));

        LocalDate dataPredare = LocalDate.of(Integer.parseInt("2018"), Integer.parseInt("10"), Integer.parseInt("12"));
        Nota nota = new Nota("12345", "12345", "test12345", 8.5,  dataPredare);
        assertEquals(6.0, service.addNota(nota,"feedback1"));
        service.deleteStudent("12345");
    }
}
