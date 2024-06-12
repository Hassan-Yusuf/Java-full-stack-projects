package com.wileyedge.fullstackschool.dao;

import com.wileyedge.fullstackschool.dao.mappers.StudentMapper;
import com.wileyedge.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Student createNewStudent(Student student) {
        //YOUR CODE STARTS HERE
        final String sql = "INSERT INTO student (fName, lName) VALUES (?, ?)"; //Essentially inserts into student table

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //Then gets the generated SQL key to be used
            ps.setString(1, student.getStudentFirstName());
            ps.setString(2, student.getStudentLastName()); //
            return ps;
        }, keyHolder);

        int newStudentId = keyHolder.getKey().intValue(); //Gets the studentID
        student.setStudentId(newStudentId);
        return student; //Then eventually returns student
        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE
        final String sql = "SELECT * FROM student";
        return jdbcTemplate.query(sql, new StudentMapper());
        //YOUR CODE ENDS HERE
    }

    @Override
    public Student findStudentById(int id) {
        //YOUR CODE STARTS HERE
        final String sql = "SELECT * FROM student WHERE sid = ?";
        return jdbcTemplate.queryForObject(sql, new StudentMapper(), id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateStudent(Student student) {
        //YOUR CODE STARTS HERE
        // Update the course_student table to maintain referential integrity
        final String updateCourseStudentSql = "UPDATE course_student SET student_id = ? WHERE student_id = ?";
        jdbcTemplate.update(updateCourseStudentSql, student.getStudentId(), student.getStudentId());
        // Update the student table
        final String updateStudentSql = "UPDATE student SET fName = ?, lName = ? WHERE sid = ?";
        jdbcTemplate.update(updateStudentSql, student.getStudentFirstName(), student.getStudentLastName(), student.getStudentId());


        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudent(int id) {
        //YOUR CODE STARTS HERE
        final String sql = "DELETE FROM student WHERE sid = ?";
        jdbcTemplate.update(sql, id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE
        final String sql = "INSERT INTO course_student (student_id, course_id) VALUES (?, ?)"; //just adding join to check for consistency
        jdbcTemplate.update(sql, studentId, courseId);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE
        final String sql = "DELETE FROM course_student WHERE student_id = ? AND course_id = ?";
        jdbcTemplate.update(sql, studentId, courseId);
        //YOUR CODE ENDS HERE
    }
}
