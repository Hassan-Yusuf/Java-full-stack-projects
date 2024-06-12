package com.wileyedge.fullstackschool.dao;

import com.wileyedge.fullstackschool.dao.mappers.TeacherMapper;
import com.wileyedge.fullstackschool.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TeacherDaoImpl implements TeacherDao {

    private final JdbcTemplate jdbcTemplate;

    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Teacher createNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE
        final String sql = "INSERT INTO teacher (tFName, tLName, dept) VALUES (?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, teacher.getTeacherFName());
            ps.setString(2, teacher.getTeacherLName());
            ps.setString(3, teacher.getDept());
            return ps;
        }, keyHolder);

        int newTeacherId = keyHolder.getKey().intValue();
        teacher.setTeacherId(newTeacherId);
        return teacher;
        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE
        final String sql = "SELECT * FROM teacher";
        return jdbcTemplate.query(sql, new TeacherMapper());
        //YOUR CODE ENDS HERE
    }

    @Override
    public Teacher findTeacherById(int id) {
        //YOUR CODE STARTS HERE
        final String sql = "SELECT * FROM teacher WHERE tid = ?";
        return jdbcTemplate.queryForObject(sql, new TeacherMapper(), id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateTeacher(Teacher t) {
        //YOUR CODE STARTS HERE
        // Update the teacher table
        final String updateTeacherSql = "UPDATE teacher SET tFName = ?, tLName = ?, dept = ? WHERE tid = ?";
        jdbcTemplate.update(updateTeacherSql, t.getTeacherFName(), t.getTeacherLName(), t.getDept(), t.getTeacherId());

        // Update the course table to maintain referential integrity
        final String updateCourseSql = "UPDATE course SET teacherId = ? WHERE teacherId = ?";
        jdbcTemplate.update(updateCourseSql, t.getTeacherId(), t.getTeacherId());
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteTeacher(int id) {
        //YOUR CODE STARTS HERE
        String updateCourses = "UPDATE course SET teacherId = NULL WHERE teacherId = ?"; //as teacherId is foreign key
        jdbcTemplate.update(updateCourses, id);
        final String sql = "DELETE FROM teacher WHERE tid = ?";
        jdbcTemplate.update(sql, id);
        //YOUR CODE ENDS HERE
    }
}
